package com.poc.whtsapp.message.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WebhookPayload {

    public String object;
    public List<Entry> entry;

    @Data
    public static class Entry {
        public String id;
        public List<Change> changes;
    }

    @Data
    public static class Change {
        public String field;
        public Value value;
    }

    @Data
    public static class Value {
        public List<Status> statuses;
    }

    @Data
    public static class Status {
        public String id;
        public String status;
        public String recipient_id;
        public String timestamp;
        public List<Map<String, Object>> errors; // optional error info
    }
}
