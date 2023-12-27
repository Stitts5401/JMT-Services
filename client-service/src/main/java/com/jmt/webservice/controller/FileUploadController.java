package com.jmt.webservice.controller;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.jmt.webservice.service.GoogleCloudStorageService;
import net.minidev.json.JSONObject;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Controller
@RequestMapping("/file")
public class FileUploadController {
    private final GoogleCloudStorageService gcsService;

    public FileUploadController(GoogleCloudStorageService gcsService) {
        this.gcsService = gcsService;
    }

    @PostMapping("/uploadProfilePhoto")
    public Mono<String> updateProfilePhoto(@RequestPart("file") Mono<FilePart> filePartMono, @RequestPart("oldImageGuid") String oldImageGuid, Model model, @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {

        return gcsService.replaceProfilePicture(oauthTokenMono, filePartMono, extractBlobNameFromUrl(oldImageGuid))
                .map(fileUrl -> {
                    // Success: Add the file URL to the model and redirect to the user account info page
                    model.addAttribute("fileUrl", fileUrl);

                    return "redirect:/account/info";
                })
                .onErrorResume(e -> {
                    // Error: Add an error message to the model and redirect to the user account info page
                    model.addAttribute("uploadError", "Failed to upload profile photo.");
                    return Mono.just("redirect:/account/info");
                });
    }

    @PostMapping("/addJobPicture")
    public Mono<String> addJobPhotos(
            @RequestPart("files") Flux<FilePart> fileParts, // Note the change here to Flux<FilePart>
            @RequestPart("id") String id, Model model,
            @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {

        return fileParts
                .flatMap(filePart -> gcsService.addJobPhoto(oauthTokenMono, Mono.just(filePart), Integer.valueOf(id)))
                .collectList()
                .flatMap(jobInfos -> {
                    // All files uploaded successfully
                    model.addAttribute("jobInfos", jobInfos);
                    // Assuming jobInfos is not empty and has a getId method
                    return Mono.just("redirect:/jobs/edit/" + jobInfos.get(0).getId());
                })
                .doOnError(e -> {
                    // Error: Add an error message to the model
                    model.addAttribute("uploadError", "Failed to upload profile photos.");
                })
                .onErrorResume(e -> {
                    // Error handling with a redirect, similar to your existing logic
                    model.addAttribute("uploadError", "Failed to upload profile photos.");
                    return Mono.just("redirect:/account/jobs");
                });
    }

    @PostMapping("/removeJobPicture")
    public Mono<String> removeJobPhoto(
            @RequestPart("href") String href, @RequestPart("id") String id, Model model,
            @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {

        return  gcsService.deleteJobPhoto(oauthTokenMono, extractBlobNameFromUrl(href), Integer.valueOf(id))
                .map(jobInfo -> {
                    // Success: Add the file URL to the model and redirect to the user account info page
                    model.addAttribute("jobInfo", jobInfo);
                    return "redirect:/jobs/edit/" + jobInfo.getId();
                }).doOnError(e -> {
                    // Error: Add an error message to the model and redirect to the user account info page
                    model.addAttribute("uploadError", "Failed to upload profile photo.");
                }).onErrorResume(e -> {
                    // Error: Add an error message to the model and redirect to the user account info page
                    model.addAttribute("uploadError", "Failed to upload profile photo.");
                    return Mono.just("redirect:/jobs/edit/");
                });
    }

    private String extractBlobNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.indexOf("?"));
    }
}
