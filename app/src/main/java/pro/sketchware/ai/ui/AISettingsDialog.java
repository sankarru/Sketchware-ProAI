package pro.sketchware.ai.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import pro.sketchware.ai.AIConfig;

/**
 * AI Settings Dialog for managing API Keys
 */
public class AISettingsDialog {
    
    private Context context;
    private AlertDialog dialog;
    private LinearLayout settingsContainer;
    
    public AISettingsDialog(Context context) {
        this.context = context;
    }
    
    /**
     * Show the AI Settings Dialog
     */
    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("AI Settings - Configure Models");
        
        // Create settings interface
        View settingsView = createSettingsInterface();
        builder.setView(settingsView);
        
        builder.setNegativeButton("Close", (dialogInterface, which) -> {
            dialog.dismiss();
        });
        
        dialog = builder.create();
        dialog.show();
    }
    
    /**
     * Create settings interface
     */
    private View createSettingsInterface() {
        ScrollView scrollView = new ScrollView(context);
        
        settingsContainer = new LinearLayout(context);
        settingsContainer.setOrientation(LinearLayout.VERTICAL);
        settingsContainer.setPadding(15, 15, 15, 15);
        
        // Add info text
        TextView infoText = new TextView(context);
        infoText.setText("Configure API Keys for AI models:");
        infoText.setTextSize(14);
        infoText.setPadding(0, 0, 0, 15);
        settingsContainer.addView(infoText);
        
        // Add settings for each model
        AIConfig.AIModel[] models = AIConfig.getInstance().getAvailableModels();
        for (AIConfig.AIModel model : models) {
            addModelSetting(model);
        }
        
        scrollView.addView(settingsContainer);
        return scrollView;
    }
    
    /**
     * Add settings for a specific model
     */
    private void addModelSetting(AIConfig.AIModel model) {
        LinearLayout modelLayout = new LinearLayout(context);
        modelLayout.setOrientation(LinearLayout.VERTICAL);
        modelLayout.setPadding(10, 10, 10, 10);
        modelLayout.setBackgroundColor(0xFFF5F5F5);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 0, 5);
        modelLayout.setLayoutParams(params);
        
        // Model name
        TextView modelName = new TextView(context);
        modelName.setText(model.getDisplayName());
        modelName.setTextSize(14);
        modelName.setTypeface(null, android.graphics.Typeface.BOLD);
        modelName.setTextColor(0xFF1976D2);
        modelLayout.addView(modelName);
        
        // Model ID text
        TextView modelId = new TextView(context);
        modelId.setText("Model ID: " + model.getModelId());
        modelId.setTextSize(11);
        modelId.setTextColor(0xFF666666);
        modelLayout.addView(modelId);
        
        // API Key input
        EditText apiKeyInput = new EditText(context);
        String existingKey = AIConfig.getInstance().getApiKey(model);
        apiKeyInput.setText(existingKey.isEmpty() ? "" : "••••••••");
        apiKeyInput.setHint("Enter your API key");
        apiKeyInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        apiKeyInput.setPadding(10, 10, 10, 10);
        
        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        inputParams.setMargins(0, 8, 0, 8);
        apiKeyInput.setLayoutParams(inputParams);
        modelLayout.addView(apiKeyInput);
        
        // Status indicator
        TextView statusText = new TextView(context);
        boolean isConfigured = !existingKey.isEmpty();
        statusText.setText(isConfigured ? "✓ Configured" : "✗ Not configured");
        statusText.setTextSize(11);
        statusText.setTextColor(isConfigured ? 0xFF4CAF50 : 0xFFF44336);
        modelLayout.addView(statusText);
        
        // Save button
        Button saveButton = new Button(context);
        saveButton.setText("Save API Key");
        saveButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        
        final AIConfig.AIModel finalModel = model;
        final TextView finalStatusText = statusText;
        
        saveButton.setOnClickListener(v -> {
            String apiKey = apiKeyInput.getText().toString().trim();
            if (apiKey.isEmpty()) {
                Toast.makeText(context, "API Key cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            
            AIConfig.getInstance().setApiKey(finalModel, apiKey);
            
            // Update status
            finalStatusText.setText("✓ Configured");
            finalStatusText.setTextColor(0xFF4CAF50);
            
            // Mask the API key
            apiKeyInput.setText("••••••••");
            
            Toast.makeText(context, model.getDisplayName() + " API Key saved!", 
                    Toast.LENGTH_SHORT).show();
        });
        modelLayout.addView(saveButton);
        
        settingsContainer.addView(modelLayout);
    }
    
    /**
     * Check and display AI configuration status
     */
    public static void showConfigurationStatus(Context context) {
        AIConfig config = AIConfig.getInstance();
        StringBuilder status = new StringBuilder("AI Configuration Status:\n\n");
        
        AIConfig.AIModel[] models = config.getAvailableModels();
        for (AIConfig.AIModel model : models) {
            String apiKey = config.getApiKey(model);
            status.append(model.getDisplayName())
                    .append(": ")
                    .append(apiKey.isEmpty() ? "Not configured" : "Configured")
                    .append("\n");
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("AI Status")
                .setMessage(status.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
