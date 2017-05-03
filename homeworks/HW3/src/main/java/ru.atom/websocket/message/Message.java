package ru.atom.websocket.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.websocket.util.JsonHelper;

public class Message {
    private final Topic topic;
    private final JsonNode data;

    public Message(Topic topic, String data) {
        this.topic = topic;
        this.data = JsonHelper.getJsonNode(JsonHelper.toJson(data));
    }

    @JsonCreator
    public Message(@JsonProperty("topic") Topic topic, @JsonProperty("data") JsonNode data) {
        this.topic = topic;
        this.data = data;
    }

    public Topic getTopic() {
        return topic;
    }

    public String getData() {
        return data.toString();
    }

    public JsonNode getDataJ() {
        return data;
    }
}
