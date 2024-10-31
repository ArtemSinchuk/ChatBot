package chatbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Artem Sinchuk
 * @version 1.3
 */
public class ChatGPTClient {

    private static final Logger logger = Logger.getLogger(ChatGPTClient.class.getName());
    private static Scanner userInputScanner = new Scanner(System.in);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MODEL = "gpt-4o";
    private static final int MAX_TOKENS = 150;
    private static List<ObjectNode> chatHistory = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Type 'exit' to exit");
        while (true) {
            System.out.print("Enter your prompt: ");
            String userPrompt = userInputScanner.nextLine();

            try {
                String response = sendMessageToChatGPT(userPrompt);

                if ("exit".equalsIgnoreCase(userPrompt)) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("ChatGPT Response: " + response);

                    ObjectNode botMessage = objectMapper.createObjectNode();
                    botMessage.put("role", "assistant");
                    botMessage.put("content", response);
                    chatHistory.add(botMessage);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "An error occurred while communicating with ChatGPT", e);
            }
        }
    }

    /**
     * Sends a message to the ChatGPT API and processes the response.
     */
    public static String sendMessageToChatGPT(String userInput) {
        if (!NetworkUtil.isInternetAvailable()) {
            logger.severe("No internet connection available.");
            return "Error: No internet connection available.";
        }
    
        if (userInput == null || userInput.trim().isEmpty()) {
            logger.warning("Error: Please enter a valid prompt.");
            return "Error: Invalid prompt.";
        }

        int retries = 5;
        int delay = 2000;
    
        for (int attempt = 0; attempt < retries; attempt++) {
        try {
            HttpURLConnection connection = createConnection();
            sendRequest(connection, userInput);
            return processResponse(connection);

        } catch (IOException e) {
            logger.log(Level.WARNING, "Network error occurred (Attempt " + (attempt + 1) + "): " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.log(Level.SEVERE, "Error with API or response format: " + e.getMessage(), e);
            break;  // In such cases, there's no point in continuing to try
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
    }
    
        logger.severe("All attempts to send request failed.");
        return "Error occurred while communicating with ChatGPT.";
    }

    /**
     * Creates a new HttpURLConnection instance to the specified API URL.
     */
    public static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(Config.getApiUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + Config.getApiKey());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);

        return connection;
    }

    /**
     * Sends a request to the ChatGPT API with the provided user input.
     */
    public static void sendRequest(HttpURLConnection connection, String userInput) throws Exception {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", MODEL);
        
        ObjectNode message = objectMapper.createObjectNode();
        message.put("role", "user");
        message.put("content", userInput);
        chatHistory.add(message);
        
        requestBody.putArray("messages").addAll(chatHistory);
        requestBody.put("max_tokens", MAX_TOKENS);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(requestBody);
            os.write(input, 0, input.length);
        }
    }

    /**
     * Processes the response received from the ChatGPT API.
     */
    public static String processResponse(HttpURLConnection connection) throws Exception {
        int statusCode = connection.getResponseCode();
        if (statusCode != HttpURLConnection.HTTP_OK) {
            try (Scanner errorScanner = new Scanner(connection.getErrorStream(), "UTF-8")) {
                errorScanner.useDelimiter("\\A");
                String errorMessage = errorScanner.hasNext() ? errorScanner.next() : "";
                throw new Exception("Error response from ChatGPT: " + errorMessage);
            }
        }

        try (Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8")) {
            scanner.useDelimiter("\\A");
            String jsonResponse = scanner.hasNext() ? scanner.next() : "";

            ObjectNode responseJson = (ObjectNode) objectMapper.readTree(jsonResponse);
    
            if (!responseJson.has("choices") || !responseJson.withArray("choices").has(0)) {
                throw new IllegalStateException("No choices returned in the response.");
            }
            ObjectNode firstChoice = (ObjectNode) responseJson.withArray("choices").get(0);
            if (!firstChoice.has("message") || !firstChoice.get("message").has("content")) {
                throw new IllegalStateException("Invalid response format.");
            }
            String chatGPTReply = firstChoice.get("message").get("content").asText();
            return chatGPTReply;
        }
    }
}