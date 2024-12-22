package chatbot;

public class Config {
    private static final String apiKey = System.getenv("OPENAI_API_KEY"); // use your own API key
    private static final String url = "https://api.openai.com/v1/chat/completions";

    public static String getApiKey() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key not set in environment variables! Set the 'OPENAI_API_KEY' environment variable.");
        }
        return apiKey;
    }

    public static String getApiUrl() {
        return url;
    }
}
