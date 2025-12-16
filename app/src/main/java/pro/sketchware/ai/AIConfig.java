package pro.sketchware.ai;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for AI Models and API Keys
 * Manages ChatGPT, Gemini models and their API keys
 */
public class AIConfig {
    
    public enum AIModel {
        CHATGPT_5_2("ChatGPT 5.2", "chatgpt-5.2", "https://api.openai.com/v1/chat/completions"),
        GEMINI_2_5_FLASH("Gemini 2.5 Flash", "gemini-2.5-flash", "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent"),
        GEMINI_3_PRO_PREVIEW("Gemini 3 Pro Preview", "gemini-3-pro-preview", "https://generativelanguage.googleapis.com/v1/models/gemini-3-pro-preview:generateContent");
        
        private final String displayName;
        private final String modelId;
        private final String endpoint;
        
        AIModel(String displayName, String modelId, String endpoint) {
            this.displayName = displayName;
            this.modelId = modelId;
            this.endpoint = endpoint;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getModelId() {
            return modelId;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
    }
    
    private static AIConfig instance;
    private Map<AIModel, String> apiKeys = new HashMap<>();
    private AIModel selectedModel;
    private boolean aiModeEnabled = false;
    
    private AIConfig() {
        // Initialize with default models
    }
    
    public static synchronized AIConfig getInstance() {
        if (instance == null) {
            instance = new AIConfig();
        }
        return instance;
    }
    
    /**
     * Set API Key for a specific AI Model
     */
    public void setApiKey(AIModel model, String apiKey) {
        apiKeys.put(model, apiKey);
    }
    
    /**
     * Get API Key for a specific AI Model
     */
    public String getApiKey(AIModel model) {
        return apiKeys.getOrDefault(model, "");
    }
    
    /**
     * Check if all required models have API keys configured
     */
    public boolean isAIConfigured() {
        for (AIModel model : AIModel.values()) {
            if (!apiKeys.containsKey(model) || apiKeys.get(model).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Set the currently selected AI Model
     */
    public void setSelectedModel(AIModel model) {
        this.selectedModel = model;
    }
    
    /**
     * Get the currently selected AI Model
     */
    public AIModel getSelectedModel() {
        return selectedModel;
    }
    
    /**
     * Enable or disable AI Mode
     */
    public void setAIModeEnabled(boolean enabled) {
        this.aiModeEnabled = enabled;
    }
    
    /**
     * Check if AI Mode is enabled
     */
    public boolean isAIModeEnabled() {
        return aiModeEnabled && isAIConfigured();
    }
    
    /**
     * Get all available AI Models
     */
    public AIModel[] getAvailableModels() {
        return AIModel.values();
    }
}
