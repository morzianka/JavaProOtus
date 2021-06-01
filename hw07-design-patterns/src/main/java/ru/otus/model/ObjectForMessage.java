package ru.otus.model;

import java.util.List;

public class ObjectForMessage {
    private final List<String> data;

    private ObjectForMessage(Builder builder) {
        this.data = builder.data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .data(data);
    }

    public Iterable<String> getData() {
        return data;
    }

    public static class Builder {
        private List<String> data;

        public Builder data(List<String> data) {
            this.data = data;
            return this;
        }

        public ObjectForMessage build() {
            return new ObjectForMessage(this);
        }
    }
}
