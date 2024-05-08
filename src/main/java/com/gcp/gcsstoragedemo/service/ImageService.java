package com.gcp.gcsstoragedemo.service;

import com.gcp.gcsstoragedemo.dto.ImageDto;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final Storage storage;

    @Value("${gcp.bucket}")
    private String bucketName;

    public ImageDto uploadFileToGCS(MultipartFile file) throws IOException {
        if (file.isEmpty() && !isValidFileExtension(file.getOriginalFilename())) {
            throw new RuntimeException("Invalid File");
        }
        BlobId blobId = BlobId.of(bucketName, file.getOriginalFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        byte[] data = file.getBytes();
        Blob blob = storage.create(blobInfo, data);
        return new ImageDto(blob.getName(),blob.getMediaLink());
    }

    public byte[] downloadFileFromGCS(String imageName) throws IOException {
        Blob blob = storage.get(bucketName, imageName);
        if (ObjectUtils.isEmpty(blob)) {
            throw new FileNotFoundException("File not found in Google Cloud Storage.");
        }
        return blob.getContent();
    }

    private boolean isValidFileExtension(String fileName) {
        Set<String> VALID_EXTENSIONS = new HashSet<>(Arrays.asList(".png", ".jpeg", ".jpg"));
        String extension = getFileExtension(fileName);
         return extension != null &&
                 VALID_EXTENSIONS.stream()
                .anyMatch(validExtension -> validExtension.equalsIgnoreCase(extension));
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return (lastDotIndex != -1) ? fileName.substring(lastDotIndex).toLowerCase() : null;
    }
}
