package com.hrms.backend.services.superbaseImageStorageService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SupabaseImageStorageServiceInterface {

    String uploadImage(MultipartFile file) throws IOException;
}
