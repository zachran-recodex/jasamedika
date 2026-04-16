package com.task.absensi.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class UbahPhotoRequest {
    @NotBlank
    @JsonProperty("Photo")
    @JsonAlias({"photo", "photoBase64", "Photo"})
    private String photo;

    private String contentType;

    @JsonIgnore
    public String getPhotoBase64() {
        return photo;
    }

    @JsonIgnore
    public void setPhotoBase64(String photoBase64) {
        this.photo = photoBase64;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
