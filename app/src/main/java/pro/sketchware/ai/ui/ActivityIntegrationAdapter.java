package pro.sketchware.ai.ui;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import pro.sketchware.ai.AIConfig;
import pro.sketchware.ai.AIIntegration;
import pro.sketchware.ai.CodingAgent;

/**
 * Activity Integration Adapter
 * Provides ready-to-use implementations for integrating AI Mode into existing activities
 * 
 * USAGE:
 * 1. In MainActivity - for "+" button:
 *    ActivityIntegrationAdapter.integratePlusButton(activity, plusButton);
 * 
 * 2. In Settings Activity - for AI Settings:
 *    ActivityIntegrationAdapter.integrateAISettings(settingsActivity);
 * 
 * 3. In Editor Activity - for AI Chat:
 *    ActivityIntegrationAdapter.integrateChatButton(editorActivity, toolbar, codingAgent);
 */
public class ActivityIntegrationAdapter {
    
    /**
     * Integrate AI Mode project creation into the "+" button
     * 
     * Usage in MainActivity:
     *     Button plusButton = findViewById(R.id.plus_button);
     *     ActivityIntegrationAdapter.integratePlusButton(MainActivity.this, plusButton);
     */
    public static void integratePlusButton(Activity activity, Button plusButton) {
        plusButton.setOnClickListener(v -> {
            NewProjectDialog dialog = new NewProjectDialog(activity);
            dialog.showModeSelectionDialog((projectName, aiMode, model) -> {
                // Callback when project is created
                // Your existing createProject() method
                // Call it like: activity.createProject(projectName, aiMode, model);
                
                // For now, show a success message
                android.widget.Toast.makeText(activity, 
                    "Project: " + projectName + " (" + (aiMode ? "AI" : "Standard") + ")",
                    android.widget.Toast.LENGTH_SHORT).show();
            });
        });
    }
    
    /**
     * Integrate AI Settings menu
     * 
     * Usage in Settings Activity:
     *     ActivityIntegrationAdapter.integrateAISettings(SettingsActivity.this);
     * 
     * Or for a button:
     *     Button aiSettingsBtn = findViewById(R.id.ai_settings_btn);
     *     aiSettingsBtn.setOnClickListener(ActivityIntegrationAdapter.getAISettingsClickListener(context));
     */
    public static void integrateAISettings(Activity activity) {
        // This can be called from onCreate or whenever needed
        // It prepares the settings dialog for use
    }
    
    /**
     * Get click listener for AI Settings button/menu item
     * 
     * Usage:
     *     aiSettingsButton.setOnClickListener(
     *         ActivityIntegrationAdapter.getAISettingsClickListener(context)
     *     );
     */
    public static android.view.View.OnClickListener getAISettingsClickListener(Activity activity) {
        return v -> {
            AISettingsDialog settingsDialog = new AISettingsDialog(activity);
            settingsDialog.show();
        };
    }
    
    /**
     * Integrate AI Chat button into editor toolbar
     * 
     * Usage in Editor Activity:
     *     CodingAgent codingAgent = project.getCodingAgent();
     *     Toolbar toolbar = findViewById(R.id.editor_toolbar);
     *     ActivityIntegrationAdapter.integrateChatButton(EditorActivity.this, toolbar, codingAgent);
     */
    public static void integrateChatButton(Activity activity, Toolbar toolbar, CodingAgent codingAgent) {
        if (codingAgent == null) return;
        
        ImageButton aiChatButton = new ImageButton(activity);
        aiChatButton.setImageResource(android.R.drawable.ic_menu_send);
        aiChatButton.setContentDescription("AI Chat");
        aiChatButton.setOnClickListener(v -> {
            AIChatDialog chatDialog = new AIChatDialog(activity, codingAgent);
            chatDialog.show();
        });
        
        toolbar.addView(aiChatButton);
    }
    
    /**
     * Integrate AI Chat button into a custom layout
     * 
     * Usage:
     *     LinearLayout toolbar = findViewById(R.id.editor_toolbar);
     *     ActivityIntegrationAdapter.integrateChatButton(
     *         EditorActivity.this, 
     *         toolbar, 
     *         codingAgent
     *     );
     */
    public static void integrateChatButton(Activity activity, LinearLayout toolbar, CodingAgent codingAgent) {
        if (codingAgent == null) return;
        
        ImageButton aiChatButton = new ImageButton(activity);
        aiChatButton.setImageResource(android.R.drawable.ic_menu_send);
        aiChatButton.setContentDescription("AI Chat");
        aiChatButton.setOnClickListener(v -> {
            AIChatDialog chatDialog = new AIChatDialog(activity, codingAgent);
            chatDialog.show();
        });
        
        // Add with layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);
        toolbar.addView(aiChatButton, params);
    }
    
    /**
     * Check if a project is in AI Mode before showing chat button
     * 
     * Usage:
     *     if (ActivityIntegrationAdapter.isAIModeProject(project)) {
     *         ActivityIntegrationAdapter.integrateChatButton(activity, toolbar, project.getCodingAgent());
     *     }
     */
    public static boolean isAIModeProject(Object project) {
        // This is a placeholder - implement based on your Project class
        try {
            java.lang.reflect.Method method = project.getClass().getMethod("isAIMode");
            return (boolean) method.invoke(project);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get Coding Agent from project
     * 
     * Usage:
     *     CodingAgent agent = ActivityIntegrationAdapter.getCodingAgent(project);
     */
    public static CodingAgent getCodingAgent(Object project) {
        try {
            java.lang.reflect.Method method = project.getClass().getMethod("getCodingAgent");
            return (CodingAgent) method.invoke(project);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Full integration example for Editor Activity
     * 
     * Usage in Editor Activity onCreate():
     *     ActivityIntegrationAdapter.setupEditorAI(this, toolbar, currentProject);
     */
    public static void setupEditorAI(Activity activity, Toolbar toolbar, Object project) {
        if (isAIModeProject(project)) {
            CodingAgent agent = getCodingAgent(project);
            if (agent != null) {
                integrateChatButton(activity, toolbar, agent);
            }
        }
    }
    
    /**
     * Initialize AI on app startup
     * 
     * Usage in Application.onCreate():
     *     ActivityIntegrationAdapter.initializeAI(getApplicationContext());
     */
    public static void initializeAI(android.content.Context context) {
        // Load saved API keys from SharedPreferences
        android.content.SharedPreferences prefs = context.getSharedPreferences(
            "ai_config", 
            android.content.Context.MODE_PRIVATE
        );
        
        AIConfig config = AIConfig.getInstance();
        
        // Load ChatGPT key
        String chatgptKey = prefs.getString("chatgpt_key", "");
        if (!chatgptKey.isEmpty()) {
            config.setApiKey(AIConfig.AIModel.CHATGPT_5_2, chatgptKey);
        }
        
        // Load Gemini keys
        String gemini25Key = prefs.getString("gemini_2_5_key", "");
        if (!gemini25Key.isEmpty()) {
            config.setApiKey(AIConfig.AIModel.GEMINI_2_5_FLASH, gemini25Key);
        }
        
        String gemini3Key = prefs.getString("gemini_3_key", "");
        if (!gemini3Key.isEmpty()) {
            config.setApiKey(AIConfig.AIModel.GEMINI_3_PRO_PREVIEW, gemini3Key);
        }
    }
    
    /**
     * Save API keys to SharedPreferences
     * 
     * Usage:
     *     ActivityIntegrationAdapter.saveAPIKey(context, "chatgpt_key", apiKey);
     */
    public static void saveAPIKey(android.content.Context context, String modelKey, String apiKey) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(
            "ai_config",
            android.content.Context.MODE_PRIVATE
        );
        prefs.edit().putString(modelKey, apiKey).apply();
    }
}
