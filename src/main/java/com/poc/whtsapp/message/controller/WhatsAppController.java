package com.poc.whtsapp.message.controller;


import com.poc.whtsapp.message.service.WhatsAppService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Controller
@RequestMapping("/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    private Path lastGeneratedFile;

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/bulk-template-form")
    public String showForm(Model model) {
        return "bulk-template";
    }

    @GetMapping("/extract")
    public String showExtract(Model model) {
        return "upload";
    }

    @PostMapping("/extract")
    public String extractColumns(@RequestParam("file") MultipartFile file,
                                 @RequestParam("columns") String columnInput,
                                 @RequestParam("url") String url,
                                 Model model) throws IOException {

        List<String> columns = Arrays.stream(columnInput.split(","))
                .map(String::trim)
                .filter(c -> !c.isEmpty())
                .toList();

        if (columns.isEmpty()) {
            model.addAttribute("message", "Please provide column names.");
            return "upload";
        }

        Path tempDir = Files.createTempDirectory("csv-output");
        String outputFileName = "filtered_" + System.currentTimeMillis() + ".csv";
        Path outputPath = tempDir.resolve(outputFileName);
        this.lastGeneratedFile = outputPath;

        try (
                Reader reader = new InputStreamReader(file.getInputStream());
                CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
                BufferedWriter writer = Files.newBufferedWriter(outputPath);
                CSVPrinter printer = new CSVPrinter(writer,
                        CSVFormat.DEFAULT.withHeader(
                                Stream.concat(columns.stream(), Stream.of("URL")).toArray(String[]::new))
                )) {

            for (CSVRecord record : parser) {
                List<String> values = new ArrayList<>();
                for (String col : columns) {
                    values.add(record.get(col));
                }
                values.add(url);
                printer.printRecord(values);
            }

        } catch (IllegalArgumentException | IOException e) {
            model.addAttribute("message", "Error: " + e.getMessage());
            return "upload";
        }

        model.addAttribute("message", "CSV processed successfully!");
        model.addAttribute("downloadLink", "/whatsapp/download");
        return "upload";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadCsv() throws IOException {
        if (lastGeneratedFile == null || !Files.exists(lastGeneratedFile)) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(lastGeneratedFile.toFile()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + lastGeneratedFile.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
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

