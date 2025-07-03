package com.poc.whtsapp.message.service;


import com.opencsv.CSVReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class WhatsAppService {

    @Async
    public CompletableFuture<ResponseEntity<List<String>>> sendTemplateMessageFromFile(
            String template, String languageCode, String token, String url, String templateType, MultipartFile file) {

        List<String> responses = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> allRows = reader.readAll();

            if (!allRows.isEmpty()) {
                allRows.remove(0); // Remove header
            }

            for (String[] row : allRows) {
                if (row.length >= 3) {
                    String name = row[0].trim();
                    String phone = row[1].trim();
                    String imageUrl = row[2].trim();

                    ResponseEntity<String> response = sendWhatsAppMessage(
                            name, phone, imageUrl, template, languageCode, token, url, templateType
                    );
                    responses.add("✅ " + phone + " → " + response.getStatusCode());
                } else {
                    responses.add("⚠️ Malformed row skipped: " + String.join(",", row));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            responses.add("❌ Error occurred: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(ResponseEntity.ok(responses));
    }

    private ResponseEntity<String> sendWhatsAppMessage(String name, String phone, String imageUrl, String template, String languageCode, String token, String apiUrl, String templateType) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> body = Map.of();
        if (templateType.equals("image")) {

             body = Map.of(
                    "messaging_product", "whatsapp",
                    "to", phone,
                    "type", "template",
                    "template", Map.of(
                            "name", template,
                            "language", Map.of("code", languageCode),
                            "components", List.of(
                                    Map.of(
                                            "type", "header",
                                            "parameters", List.of(
                                                    Map.of("type", "image", "image", Map.of("link", imageUrl))
                                            )
                                    ),
                                    Map.of(
                                            "type", "body",
                                            "parameters", List.of(
                                                    Map.of("type", "text", "text", name)
                                            )
                                    )
                            )
                    )
            );
        } else if(templateType.equals("normal")) {
            body = Map.of(
                    "messaging_product", "whatsapp",
                    "to", phone,
                    "type", "template",
                    "template", Map.of(
                            "name", template,
                            "language", Map.of("code", languageCode)
                    )
            );

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
        System.out.println("✅ Sent to " + phone + ": " + response.getStatusCode());
       return response;
    }


}


