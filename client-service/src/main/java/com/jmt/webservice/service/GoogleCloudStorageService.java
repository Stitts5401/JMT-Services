package com.jmt.webservice.service;

import com.google.cloud.storage.*;
import com.jmt.model.JobInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    @Value("${spring.cloud.gcp.bucket.job-pictures}")
    private String jobPictureBucket;
    @Value("${spring.cloud.gcp.bucket.profile-pictures}")
    private String profilePictureBucket;

    public Mono<String> generateSignedUrl(String objectName, boolean bucket) {
        return Mono.fromCallable(() -> {
            String bucketF = bucket ? jobPictureBucket : profilePictureBucket;
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketF, objectName).build();
            URL signedUrl = storage.signUrl(blobInfo, 1, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature());
            return signedUrl.toString();
        }).subscribeOn(Schedulers.boundedElastic());
    }
    public Mono<String> replaceProfilePicture(Mono<OAuth2AuthenticationToken> oauthTokenMono, Mono<FilePart> filePartMono, String oldImageGuid) {
        return oauthTokenMono.flatMap(oauthToken -> filePartMono.flatMap(filePart -> {
            Mono<InputStream> inputStreamMono = DataBufferUtils.join(filePart.content())
                    .map(DataBuffer::asInputStream);

            return inputStreamMono.flatMap(inputStream -> {
                String blobName = UUID.randomUUID().toString();
                BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(profilePictureBucket, blobName)).build();

                return Mono.fromCallable(() -> {
                            // Perform the upload
                            storage.create(blobInfo, inputStream);
                            return blobName;
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(createdBlobName -> {
                            // After a successful upload, attempt to update the database
                            return accountService.updateImage(oauthToken, createdBlobName)
                                    .thenReturn(String.format("https://storage.googleapis.com/%s/%s", profilePictureBucket, createdBlobName))
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
                                storage.delete(BlobId.of(profilePictureBucket, oldImageGuid));
                                log.info("Old Blob deleted successfully: {}", oldImageGuid);
                            } catch (Exception ex) {
                                log.error("Error deleting the old blob: {}", ex.getMessage());
                            }
                        })
                        .doOnError(error -> {
                            // If there's an error after the upload, attempt to delete the blob from storage
                            log.error("Attempting to delete the blob due to an error: {}", error.getMessage());
                            try {
                                storage.delete(BlobId.of(profilePictureBucket, blobName));
                                log.info("Blob deleted successfully.");
                            } catch (Exception ex) {
                                log.error("Error deleting the blob: {}", ex.getMessage());
                            }
                        });
            });
        }));
    }
    public Mono<JobInfo> deleteJobPhoto(Mono<OAuth2AuthenticationToken> oauthTokenMono, String guid, Integer id) {
        return oauthTokenMono.flatMap(oauthToken -> {
                    // After a successful upload, attempt to update the database
                    return accountService.removeJobImage(oauthToken, new JSONObject()
                         .appendField("id", id)
                         .appendField("guid", guid ))
                            .doOnSuccess(url -> log.info("Image updated successfully: {}", url))
                            .doOnError(error -> log.error("Error updating image: {}", error.getMessage()));
                })
                .doOnSuccess(url -> {
                    try {
                        storage.delete(BlobId.of(jobPictureBucket, guid));
                        log.info("Old Blob deleted successfully: {}",guid);
                    } catch (Exception ex) {
                        log.error("Error deleting the old blob: {}", ex.getMessage());
                    }
                })
                .doOnError(error -> {
                    // If there's an error after the upload, attempt to delete the blob from storage
                    log.error("Attempting to delete the blob due to an error: {}", error.getMessage());
                    try {
                        storage.delete(BlobId.of(jobPictureBucket, guid));
                        log.info("Blob deleted successfully.");
                    } catch (Exception ex) {
                        log.error("Error deleting the blob: {}", ex.getMessage());
                    }
                });
    }
    public Mono<JobInfo> addJobPhoto(Mono<OAuth2AuthenticationToken> oauthTokenMono,  Mono<FilePart> filePartMono, Integer id) {
        return oauthTokenMono.flatMap(oauthToken -> filePartMono.flatMap(filePart -> {
            Mono<InputStream> inputStreamMono = DataBufferUtils.join(filePart.content())
                    .map(DataBuffer::asInputStream);

            return inputStreamMono.flatMap(inputStream -> {
                String blobName = UUID.randomUUID().toString();
                BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(jobPictureBucket, blobName)).build();

                storage.create(blobInfo, inputStream);
                return accountService
                            .addJobImage(oauthToken, new JSONObject()
                                    .appendField("guid", blobName)
                                    .appendField("id", id));
                });
        }));
    }

}