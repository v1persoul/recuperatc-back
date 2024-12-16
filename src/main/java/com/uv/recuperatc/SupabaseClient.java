package com.uv.recuperatc;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Service
public class SupabaseClient {
    private static final Logger logger = LoggerFactory.getLogger(SupabaseClient.class);

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api-key}")
    private String supabaseApiKey;

    private OkHttpClient client = new OkHttpClient();

    public String getData(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(supabaseUrl + endpoint)
                .addHeader("apikey", supabaseApiKey)
                .addHeader("Authorization", "Bearer " + supabaseApiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public String postData(String endpoint, String jsonInputString) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonInputString, JSON);
    
        Request request = new Request.Builder()
                .url(supabaseUrl + endpoint)
                .post(body) // Usa POST para enviar datos
                .addHeader("apikey", supabaseApiKey)
                .addHeader("Authorization", "Bearer " + supabaseApiKey)
                .build();
    
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string(); // Devuelve la respuesta como un String
        }
    }

    public String updateData(String endpoint, String jsonInputString) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonInputString, JSON);
    
        Request request = new Request.Builder()
                .url(supabaseUrl + endpoint)
                .patch(body) // Cambia a .put(body) si es necesario
                .addHeader("apikey", supabaseApiKey)
                .addHeader("Authorization", "Bearer " + supabaseApiKey)
                .build();
    
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string(); // Devuelve la respuesta como un String
        }
    }

    public void validateToken(String token) throws IOException {
        // Implementa la validación del token si es necesario
    }
}