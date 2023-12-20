package com.jmt.webservice.controller;

import com.jmt.webservice.service.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileUploadController {
    private final GoogleCloudStorageService gcsService;

    public FileUploadController(GoogleCloudStorageService gcsService) {
        this.gcsService = gcsService;
    }

    @PostMapping("/uploadProfilePhoto")
    public Mono<String> uploadProfilePhoto(
            @RequestPart("file") Mono<FilePart> filePartMono,
            @RequestPart("oldImageGuid") String oldImageGuid,
            Model model,
            @AuthenticationPrincipal Mono<OAuth2AuthenticationToken> oauthTokenMono) {

        return gcsService.uploadFile(oauthTokenMono, filePartMono, extractBlobNameFromUrl(oldImageGuid))
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

    private String extractBlobNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.indexOf("?"));
    }
}
