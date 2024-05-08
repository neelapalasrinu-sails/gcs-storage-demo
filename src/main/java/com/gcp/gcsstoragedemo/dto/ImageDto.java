package com.gcp.gcsstoragedemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {
    private String imageName;
    private String imageUrl;
}
