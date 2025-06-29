package com.poc.whtsapp.message.controller;


import com.poc.whtsapp.message.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    @GetMapping("/bulk-template-form")
    public String showForm(Model model) {
        return "bulk-template";
    }

    @ResponseBody
    @PostMapping(value = "/bulk-template", consumes = "multipart/form-data")
    public CompletableFuture<ResponseEntity<List<String>>> sendBulkTemplate(
            @RequestParam("template") String template,
            @RequestParam("code") String languageCode,
            @RequestParam("token") String token,
            @RequestParam("url") String apiUrl,
            @RequestParam("templateType") String templateType,
            @RequestParam("file") MultipartFile file) {

        return whatsAppService.sendTemplateMessageFromFile(template, languageCode, token, apiUrl, templateType, file)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    List<String> errorResponse = List.of("❌ Error sending messages: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                });
    }
}

