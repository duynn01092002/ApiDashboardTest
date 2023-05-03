package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String clientId = "H8J1NKema4LrrUu6TYq6kH5if1JX6UyQ";
        String clientSecret = "RimknsnMuXAzi6gzWqinaUyLMgS95tbp";

        // Tạo payload dưới dạng JSON
        String payload = String.format("{\"clientId\":\"%s\",\"clientSecret\":\"%s\"}", clientId, clientSecret);

        // Tạo request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://cads-api.fpt.vn/fiber-detection/v2/getToken"))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .header("Content-Type", "application/json")
                .build();

        // Tạo client
        HttpClient client = HttpClient.newHttpClient();

        // Gửi request và nhận response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JSONObject responseJson = new JSONObject(response.body());
        String access_token = responseJson.getString("access_token");

        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://cads-api.fpt.vn/fiber-detection/v2/using_json_inf/2022/12"))
                .header("Authorization", "Bearer " + access_token)
                .GET()
                .build();

        // Gửi request và nhận response
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        // In ra status code và body của response
        System.out.println("Status code: " + response2.statusCode());
        System.out.println("Response body: " + response2.body());
    }
}