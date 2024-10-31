package chatbot;

import java.util.List;

public class ChatRequest {
    private String model;
    private List<ChatMessage> messages;
    private int max_tokens;

    public ChatRequest(String model, List<ChatMessage> messages, int max_tokens) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = max_tokens;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public int getMaxTokens() {
        return max_tokens;
    }

    public void setMaxTokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }
}
