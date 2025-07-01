package com.poc.whtsapp.message.dto;

import java.util.List;
import java.util.Map;

public class WebhookPayload {

    public String object;
    public List<Entry> entry;

    public static class Entry {
        public String id;
        public List<Change> changes;
    }

    public static class Change {
        public String field;
        public Value value;
    }

    public static class Value {
        public List<Status> statuses;
    }

    public static class Status {
        public String id;
        public String status;
        public String recipient_id;
        public String timestamp;
        public List<Map<String, String>> errors; // optional error info
    }
}
