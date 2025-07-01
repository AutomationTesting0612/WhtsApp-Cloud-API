package com.poc.whtsapp.message.controller;

import com.poc.whtsapp.message.dto.WebhookPayload;
import com.poc.whtsapp.message.entity.MessageStatus;
import com.poc.whtsapp.message.repository.MessageStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/webhook")
public class WhatsAppWebhookController {



    @Autowired
    private MessageStatusRepository repository;

    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody WebhookPayload payload) {
        payload.entry.forEach(entry -> entry.changes.forEach(change -> {
            if ("messages".equals(change.field) && change.value.statuses != null) {
                change.value.statuses.forEach(status -> {
                    MessageStatus msg = MessageStatus.builder()
                            .messageId(status.id)
                            .recipientId(status.recipient_id)
                            .status(status.status)
                            .timestamp(status.timestamp)
                            .build();

                    if (status.errors != null && !status.errors.isEmpty()) {
                        msg.setErrorReason(status.errors.get(0).get("code") + " - " + status.errors.get(0).get("title"));
                    }

                    repository.save(msg);
                });
            }
        }));
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestParam("hub.challenge") String challenge) {

        String VERIFY_TOKEN = "meatyhamhock";
        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(403).body("Verification failed");
        }
    }

    @GetMapping("/status")
    public String viewMessageStatus(Model model) {
        model.addAttribute("messages", repository.findAll());
        return "message_status";
    }

}
