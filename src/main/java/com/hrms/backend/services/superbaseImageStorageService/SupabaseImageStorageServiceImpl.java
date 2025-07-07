package com.hrms.backend.services.superbaseImageStorageService;

import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseImageStorageServiceImpl implements SupabaseImageStorageServiceInterface {

    private static final String SUPABASE_URL = "https://<project>.supabase.co";
    private static final String ANON_KEY = "<your-anon-key>";
    private static final String BUCKET = "profile-images";

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String endpoint = SUPABASE_URL + "/storage/v1/object/" + BUCKET + "/" + fileName;

        OkHttpClient client = new OkHttpClient();
        RequestBody body =
                RequestBody.create(file.getBytes(), MediaType.parse(Objects.requireNonNull(file.getContentType())));

        Request request = new Request.Builder()
                .url(endpoint)
                .put(body)
                .addHeader("apikey", ANON_KEY)
                .addHeader("Authorization", "Bearer " + ANON_KEY)
                .build();

        try (Response res = client.newCall(request).execute()) {
            if (!res.isSuccessful()) {
                assert res.body() != null;
                throw new IOException("Upload failed: " + res.body().string());
            }
            return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET + "/" + fileName;
        }
    }
}

