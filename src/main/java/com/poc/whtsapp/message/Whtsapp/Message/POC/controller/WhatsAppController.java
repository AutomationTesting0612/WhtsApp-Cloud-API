package com.poc.whtsapp.message.Whtsapp.Message.POC.controller;

import com.poc.whtsapp.message.Whtsapp.Message.POC.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    @PostMapping("/bulk-template")
    public CompletableFuture<ResponseEntity<List<String>>> sendBulkTemplate(@RequestBody Map<String, Object> body) {
        String template = (String) body.get("template");
        String languageCode = (String) body.get("code");
        String token = (String) body.get("token");
        String apiUrl = (String) body.get("url");
        String templateType = (String) body.get("templateType");

        return whatsAppService.sendTemplateMessage(template, languageCode, token, apiUrl, templateType)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    List<String> errorResponse = List.of("❌ Error sending messages: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                });
    }
//    @PostMapping("/bulk-template")
//    public ResponseEntity<List<String>> sendBulkTemplate(@RequestBody Map<String, Object> body) {
//        String template = (String) body.get("template");
//        String languageCode = (String) body.get("code");
//        String token = (String) body.get("token");
//        String apiUrl = (String) body.get("url");
//        String templateType = (String) body.get("templateType");
//
//        try {
//            // Await the async method to complete
//            CompletableFuture<ResponseEntity<List<String>>> future =
//                    whatsAppService.sendTemplateMessage(template, languageCode, token, apiUrl,templateType);
//
//            // Block to get the actual result (you can make this non-blocking in future if needed)
//            ResponseEntity<List<String>> response = future.get();
//
//            return response;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            List<String> errorResponse = List.of("❌ Error sending messages: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
}
