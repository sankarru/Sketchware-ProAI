package pro.sketchware.ai;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import pro.sketchware.ai.ui.AIChatDialog;
import pro.sketchware.ai.ui.AISettingsDialog;

/**
 * AI Integration Helper
 * Provides easy methods to integrate AI Mode into existing activities
 */
public class AIIntegration {
    
    /**
     * Add AI Chat button to editor toolbar
     * Call this in your editor activity's onCreate or when initializing toolbar
     * 
     * Example:
     * ImageButton aiButton = AIIntegration.createAIChatButton(context, editorToolbar, codingAgent);
     */
    public static ImageButton createAIChatButton(Context context, CodingAgent codingAgent) {
        ImageButton aiChatButton = new ImageButton(context);
        aiChatButton.setImageResource(android.R.drawable.ic_menu_send);
        aiChatButton.setContentDescription("AI Chat");
        aiChatButton.setOnClickListener(v -> {
            if (codingAgent != null && AIConfig.getInstance().isAIModeEnabled()) {
                AIChatDialog chatDialog = new AIChatDialog(context, codingAgent);
                chatDialog.show();
            }
        });
        return aiChatButton;
    }
    
    /**
     * Add AI Settings menu item handler
     * Call this in your settings/preferences activity
     * 
     * Example:
     * aiSettingsMenuItem.setOnClickListener(AIIntegration.createAISettingsClickListener(context));
     */
    public static View.OnClickListener createAISettingsClickListener(Context context) {
        return v -> {
            AISettingsDialog settingsDialog = new AISettingsDialog(context);
            settingsDialog.show();
        };
    }
    
    /**
     * Initialize AI Configuration from SharedPreferences
     * Call this once in your app's startup (e.g., in Application.onCreate)
     * 
     * Example:
     * AIIntegration.initializeAIConfig(context);
     */
    public static void initializeAIConfig(Context context) {
        // Future: Load saved API keys from SharedPreferences
        // For now, this is a placeholder for initialization logic
    }
    
    /**
     * Check if AI Mode is properly configured
     * Use this before showing AI-related UI elements
     * 
     * Example:
     * if (AIIntegration.isAIConfigured()) {
     *     showAIModeOption();
     * }
     */
    public static boolean isAIConfigured() {
        return AIConfig.getInstance().isAIConfigured();
    }
    
    /**
     * Create a new Coding Agent for a project
     * Call this when user creates/opens an AI Mode project
     * 
     * Example:
     * CodingAgent agent = AIIntegration.createCodingAgent(
     *     "MyApp", "/path/to/project", AIConfig.AIModel.CHATGPT_5_2
     * );
     */
    public static CodingAgent createCodingAgent(
            String projectName, 
            String projectPath, 
            AIConfig.AIModel model) {
        
        if (!AIConfig.getInstance().isAIConfigured()) {
            throw new IllegalStateException("AI not configured. Please add API keys.");
        }
        
        CodingAgent agent = new CodingAgent(projectName, projectPath, model);
        AIConfig.getInstance().setAIModeEnabled(true);
        return agent;
    }
    
    /**
     * Format project info for AI context
     * Use this to provide additional context to the AI
     * 
     * Example:
     * String context = AIIntegration.formatProjectInfo(projectName, targetSDK);
     * agent.chat("I want to: " + userRequest + "\n" + context);
     */
    public static String formatProjectInfo(String projectName, int targetSDK) {
        return String.format(
            "\n[PROJECT INFO]\n" +
            "Project Name: %s\n" +
            "Target SDK: %d\n" +
            "Min SDK: 21\n" +
            "Type: Android Application",
            projectName, targetSDK
        );
    }
}
