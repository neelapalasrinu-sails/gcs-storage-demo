package com.gcp.gcsstoragedemo.controller;

import com.gcp.gcsstoragedemo.dto.ImageDto;
import com.gcp.gcsstoragedemo.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageUploadService;

    @PostMapping("/uploadImage")
    public ImageDto uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
       return imageUploadService.uploadFileToGCS(file);
    }

    @GetMapping("/getFile/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) throws IOException {
        byte[] image = imageUploadService.downloadFileFromGCS(filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
