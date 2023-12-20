package com.jmt.webservice.service;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCloudStorageService {


    private final Storage storage;
    private final AccountService accountService;

    @Value("${spring.cloud.gcp.bucket.name}")
    private String bucketName;

    public Mono<String> generateSignedUrl(String objectName) {
        return Mono.fromCallable(() -> {
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();
            URL signedUrl = storage.signUrl(blobInfo, 1, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature());
            return signedUrl.toString();
        }).subscribeOn(Schedulers.boundedElastic());
    }
    public Mono<String> uploadFile(Mono<OAuth2AuthenticationToken> oauthTokenMono, Mono<FilePart> filePartMono, String oldImageGuid) {
        return oauthTokenMono.flatMap(oauthToken -> filePartMono.flatMap(filePart -> {
            Mono<InputStream> inputStreamMono = DataBufferUtils.join(filePart.content())
                    .map(DataBuffer::asInputStream);

            return inputStreamMono.flatMap(inputStream -> {
                String blobName = UUID.randomUUID().toString();
                BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, blobName)).build();

                return Mono.fromCallable(() -> {
                            // Perform the upload
                            storage.create(blobInfo, inputStream);
                            return blobName;
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(createdBlobName -> {
                            // After a successful upload, attempt to update the database
                            return accountService.updateImage(oauthToken, createdBlobName)
                                    .thenReturn(String.format("https://storage.googleapis.com/%s/%s", bucketName, createdBlobName))
                                    .doOnSuccess(url -> log.info("Image updated successfully: {}", url))
                                    .doOnError(error -> log.error("Error updating image: {}", error.getMessage()));
                        })
                        .onErrorResume(e -> {
                            // If there's an error during the upload, log and return an error without trying to delete
                            log.error("Error during file upload: {}", e.getMessage());
                            return Mono.error(e);
                        })
                        .doOnSuccess(url -> {
                            try {
                                storage.delete(BlobId.of(bucketName, oldImageGuid));
                                log.info("Old Blob deleted successfully: {}", oldImageGuid);
                            } catch (Exception ex) {
                                log.error("Error deleting the old blob: {}", ex.getMessage());
                            }
                        })
                        .doOnError(error -> {
                            // If there's an error after the upload, attempt to delete the blob from storage
                            log.error("Attempting to delete the blob due to an error: {}", error.getMessage());
                            try {
                                storage.delete(BlobId.of(bucketName, blobName));
                                log.info("Blob deleted successfully.");
                            } catch (Exception ex) {
                                log.error("Error deleting the blob: {}", ex.getMessage());
                            }
                        });
            });
        }));
    }

}