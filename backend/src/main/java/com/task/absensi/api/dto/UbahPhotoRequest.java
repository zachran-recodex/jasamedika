package com.task.absensi.api.dto;

import javax.validation.constraints.NotBlank;

public class UbahPhotoRequest {
    @NotBlank
    private String photoBase64;

    private String contentType;

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
