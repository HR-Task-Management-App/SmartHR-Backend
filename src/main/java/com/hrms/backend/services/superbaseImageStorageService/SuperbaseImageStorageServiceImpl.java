package com.hrms.backend.services.superbaseImageStorageService;

import com.hrms.backend.exceptions.BadApiRequestException;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class SuperbaseImageStorageServiceImpl implements SuperbaseImageStorageServiceInterface {

    private static final String SUPABASE_URL = "https://mmawsfmqsxzjolszazph.supabase.co";
    private static final String ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1tYXdzZm1xc3h6am9sc3phenBoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTE3ODg0MDYsImV4cCI6MjA2NzM2NDQwNn0.MEf8nKmj6qL5AymrUvhBCu_ll2_sk9dO_19CL_AOeW0";

    //all extension image allowed
    @Override
    public String uploadImage(MultipartFile image,String bucketName)  {
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        String endpoint = SUPABASE_URL + "/storage/v1/object/" + bucketName + "/" + fileName;

        OkHttpClient client = new OkHttpClient();

        try {
            RequestBody body = RequestBody.create(
                    image.getBytes(),
                    MediaType.parse(Objects.requireNonNull(image.getContentType()))
            );

            Request request = new Request.Builder()
                    .url(endpoint)
                    .put(body)
                    .addHeader("apikey", ANON_KEY)
                    .addHeader("Authorization", "Bearer " + ANON_KEY)
                    .build();

            try (Response res = client.newCall(request).execute()) {
                if (!res.isSuccessful()) {
                    String error = res.body() != null ? res.body().string() : "Unknown error";
                    throw new BadApiRequestException("Image upload failed: " + error);
                }
                return SUPABASE_URL + "/storage/v1/object/public/" + bucketName + "/" + fileName;
            }

        } catch (IOException e) {
            throw new BadApiRequestException("Failed to upload image, try again later!!");
        }
    }
}

