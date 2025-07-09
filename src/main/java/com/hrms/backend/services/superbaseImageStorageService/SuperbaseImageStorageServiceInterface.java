package com.hrms.backend.services.superbaseImageStorageService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SuperbaseImageStorageServiceInterface {

    String uploadImage(MultipartFile image,String bucketName);
}
