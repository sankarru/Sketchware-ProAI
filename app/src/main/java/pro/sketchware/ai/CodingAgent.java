package pro.sketchware.ai;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Coding Agent - AI-powered code generation and modification
 * Works like Cursor - has full project access to create and edit apps
 */
public class CodingAgent {
    
    public static class Message {
        public String role;  // "user" or "assistant"
        public String content;
        public long timestamp;
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }
        
        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            json.put("role", role);
            json.put("content", content);
            json.put("timestamp", timestamp);
            return json;
        }
    }
    
    private String projectName;
    private String projectPath;
    private AIConfig.AIModel selectedModel;
    private List<Message> conversationHistory;
    private AIAPIClient apiClient;
    private ProjectContext projectContext;
    
    public CodingAgent(String projectName, String projectPath, AIConfig.AIModel model) {
        this.projectName = projectName;
        this.projectPath = projectPath;
        this.selectedModel = model;
        this.conversationHistory = new ArrayList<>();
        this.apiClient = new AIAPIClient(model);
        this.projectContext = new ProjectContext(projectPath);
        
        // System prompt for the coding agent
        initializeSystemPrompt();
    }
    
    /**
     * Initialize the system prompt for the coding agent
     */
    private void initializeSystemPrompt() {
        String systemPrompt = "You are an expert Android app developer and Sketchware Pro specialist. " +
                "You have full access to the project structure and can generate, modify, and refactor code. " +
                "Your tasks include:\n" +
                "- Creating new activities and components\n" +
                "- Generating layout XML files\n" +
                "- Writing Java code for logic and event handlers\n" +
                "- Configuring AndroidManifest.xml\n" +
                "- Managing project dependencies\n" +
                "- Debugging and optimizing code\n" +
                "Always provide clear, well-commented code and explain your changes.";
        
        conversationHistory.add(new Message("system", systemPrompt));
    }
    
    /**
     * Send a message to the AI and get response
     */
    public String chat(String userMessage) throws Exception {
        // Add user message to history
        conversationHistory.add(new Message("user", userMessage));
        
        // Build project context for the AI
        String context = projectContext.getProjectContext();
        String enhancedMessage = userMessage + "\n\n[PROJECT CONTEXT]\n" + context;
        
        // Call AI API
        String response = apiClient.generateResponse(conversationHistory, enhancedMessage);
        
        // Add assistant response to history
        conversationHistory.add(new Message("assistant", response));
        
        // Parse and apply code changes if needed
        parseAndApplyChanges(response);
        
        return response;
    }
    
    /**
     * Parse AI response and apply code/file changes to the project
     */
    private void parseAndApplyChanges(String response) {
        // Look for code blocks in the response
        // Format: ```language\ncode\n```
        
        String[] lines = response.split("\n");
        StringBuilder currentCode = new StringBuilder();
        String currentLanguage = "";
        boolean inCodeBlock = false;
        String targetFile = "";
        
        for (String line : lines) {
            if (line.startsWith("```")) {
                if (!inCodeBlock) {
                    // Start of code block
                    inCodeBlock = true;
                    currentLanguage = line.substring(3).trim();
                } else {
                    // End of code block
                    inCodeBlock = false;
                    if (!currentCode.toString().isEmpty()) {
                        // Apply the code change
                        projectContext.applyCodeChange(targetFile, currentCode.toString(), currentLanguage);
                    }
                    currentCode = new StringBuilder();
                }
            } else if (line.startsWith("FILE:") && inCodeBlock) {
                targetFile = line.substring(5).trim();
            } else if (inCodeBlock) {
                currentCode.append(line).append("\n");
            }
        }
    }
    
    /**
     * Get conversation history
     */
    public List<Message> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }
    
    /**
     * Clear conversation history
     */
    public void clearHistory() {
        conversationHistory.clear();
        initializeSystemPrompt();
    }
    
    /**
     * Get project context
     */
    public ProjectContext getProjectContext() {
        return projectContext;
    }
    
    /**
     * Get selected AI model
     */
    public AIConfig.AIModel getSelectedModel() {
        return selectedModel;
    }
}
