package pro.sketchware.ai;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * API Client for communicating with AI Services
 * Supports ChatGPT and Google Gemini APIs
 */
public class AIAPIClient {
    
    private AIConfig.AIModel model;
    private String apiKey;
    private static final int TIMEOUT_MS = 30000;
    
    public AIAPIClient(AIConfig.AIModel model) {
        this.model = model;
        this.apiKey = AIConfig.getInstance().getApiKey(model);
    }
    
    /**
     * Generate response from AI model
     */
    public String generateResponse(List<CodingAgent.Message> conversationHistory, String userMessage) throws Exception {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new Exception("API Key not configured for " + model.getDisplayName());
        }
        
        switch (model) {
            case CHATGPT_5_2:
                return callChatGPT(conversationHistory, userMessage);
            case GEMINI_2_5_FLASH:
            case GEMINI_3_PRO_PREVIEW:
                return callGemini(userMessage);
            default:
                throw new Exception("Unknown model: " + model.getDisplayName());
        }
    }
    
    /**
     * Call ChatGPT API
     */
    private String callChatGPT(List<CodingAgent.Message> conversationHistory, String userMessage) throws Exception {
        String endpoint = model.getEndpoint();
        
        // Build request body
        JSONArray messagesArray = new JSONArray();
        for (CodingAgent.Message msg : conversationHistory) {
            if (!msg.role.equals("system")) {  // Skip system message in request
                JSONObject msgObj = new JSONObject();
                msgObj.put("role", msg.role);
                msgObj.put("content", msg.content);
                messagesArray.put(msgObj);
            }
        }
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model.getModelId());
        requestBody.put("messages", messagesArray);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 4096);
        
        return makeAPIRequest(endpoint, requestBody.toString(), "Bearer " + apiKey);
    }
    
    /**
     * Call Google Gemini API
     */
    private String callGemini(String userMessage) throws Exception {
        String endpoint = model.getEndpoint() + "?key=" + apiKey;
        
        // Build request body for Gemini
        JSONObject requestBody = new JSONObject();
        JSONArray contentsArray = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray partsArray = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", userMessage);
        partsArray.put(part);
        content.put("parts", partsArray);
        contentsArray.put(content);
        requestBody.put("contents", contentsArray);
        
        JSONObject generationConfig = new JSONObject();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 4096);
        requestBody.put("generationConfig", generationConfig);
        
        return makeAPIRequest(endpoint, requestBody.toString(), null);
    }
    
    /**
     * Make HTTP request to API
     */
    private String makeAPIRequest(String urlString, String jsonBody, String authHeader) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        
        if (authHeader != null) {
            connection.setRequestProperty("Authorization", authHeader);
        }
        
        connection.setDoOutput(true);
        
        // Send request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // Read response
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("API Error: " + responseCode + " - " + connection.getResponseMessage());
        }
        
        // Parse response
        return parseResponse(connection);
    }
    
    /**
     * Parse API response
     */
    private String parseResponse(HttpURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        
        String responseBody = response.toString();
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        // Extract content based on API type
        if (model == AIConfig.AIModel.CHATGPT_5_2) {
            return jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        } else {
            // Gemini response format
            return jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        }
    }
    
    /**
     * Update API Key
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        AIConfig.getInstance().setApiKey(model, apiKey);
    }
}
