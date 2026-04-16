package com.task.absensidesktop;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class ApiClient {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final Gson gson;
    private String baseUrl;
    private String token;

    public ApiClient(String baseUrl) {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
        this.baseUrl = normalizeBaseUrl(baseUrl);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public JsonElement post(String path, JsonObject body) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(body), JSON);
        Request.Builder builder = new Request.Builder()
                .url(baseUrl + path)
                .post(requestBody);
        applyAuth(builder);

        Response response = httpClient.newCall(builder.build()).execute();
        return handleResponse(response);
    }

    public JsonElement post(String path) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(baseUrl + path)
                .post(RequestBody.create(new byte[0], null));
        applyAuth(builder);

        Response response = httpClient.newCall(builder.build()).execute();
        return handleResponse(response);
    }

    public JsonElement get(String pathWithQuery) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(baseUrl + pathWithQuery)
                .get();
        applyAuth(builder);

        Response response = httpClient.newCall(builder.build()).execute();
        return handleResponse(response);
    }

    private void applyAuth(Request.Builder builder) {
        if (token != null && !token.trim().isEmpty()) {
            builder.header("Authorization", "Bearer " + token.trim());
        }
    }

    private JsonElement handleResponse(Response response) throws IOException {
        String responseBody = response.body() != null ? response.body().string() : "";
        JsonElement parsed = responseBody.isEmpty() ? new JsonObject() : gson.fromJson(responseBody, JsonElement.class);
        if (!response.isSuccessful()) {
            IOException ex = new IOException("HTTP " + response.code() + ": " + responseBody);
            ex.addSuppressed(new Throwable("status=" + response.code()));
            throw ex;
        }
        return parsed;
    }

    private static String normalizeBaseUrl(String baseUrl) {
        String trimmed = baseUrl == null ? "" : baseUrl.trim();
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}

