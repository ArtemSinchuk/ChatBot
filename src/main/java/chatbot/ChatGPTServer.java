package chatbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Collections;

public class ChatGPTServer {

    private static final ObjectMapper objectMapper = new ObjectMapper();  // Jackson object for JSON handling

    // Method to send a request to OpenAI API and get the response
    public String getResponseFromChatGPT(String userMessage) {
        try {
            URL url = new URL(Config.getApiUrl());  // Fetch API URL from Config
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + Config.getApiKey());  // Replace with your API key
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create a ChatMessage object (role: "user", content: userMessage)
            ChatMessage message = new ChatMessage("user", userMessage);

            // Create a ChatRequest object with model and messages
            ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", Collections.singletonList(message), 100);  // Assuming max_tokens is 100

            // Use ObjectMapper to convert ChatRequest object to JSON string
            String jsonInputString = objectMapper.writeValueAsString(chatRequest);

            // Send the request body (JSON) to the API
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response from ChatGPT API
            InputStream responseStream = conn.getInputStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"An error occurred while communicating with ChatGPT\"}";
        }
    }
}
