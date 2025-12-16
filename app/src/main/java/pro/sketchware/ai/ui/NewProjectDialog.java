package pro.sketchware.ai.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import pro.sketchware.ai.AIConfig;
import pro.sketchware.ai.CodingAgent;

/**
 * Dialog for creating a new project with AI Mode selection
 */
public class NewProjectDialog {
    
    private Context context;
    private AlertDialog dialog;
    private OnProjectCreatedListener listener;
    
    public interface OnProjectCreatedListener {
        void onProjectCreated(String projectName, boolean aiMode, AIConfig.AIModel selectedModel);
        void onCancelled();
    }
    
    public NewProjectDialog(Context context) {
        this.context = context;
    }
    
    /**
     * Show dialog to select AI Mode or Standard Mode
     */
    public void showModeSelectionDialog(OnProjectCreatedListener listener) {
        this.listener = listener;
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Create New Project");
        
        String[] modes = {"Standard Mode", "AI Mode"};
        builder.setItems(modes, (dialogInterface, which) -> {
            if (which == 0) {
                // Standard Mode
                showStandardProjectDialog(false, null);
            } else {
                // AI Mode
                if (!AIConfig.getInstance().isAIConfigured()) {
                    Toast.makeText(context, "AI not configured. Please add API keys in Settings.", 
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                showModelSelectionDialog();
            }
        });
        
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
            if (listener != null) {
                listener.onCancelled();
            }
        });
        
        dialog = builder.create();
        dialog.show();
    }
    
    /**
     * Show dialog to select AI Model
     */
    private void showModelSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select AI Model");
        builder.setMessage("Choose which AI model to use for this project");
        
        AIConfig.AIModel[] models = AIConfig.getInstance().getAvailableModels();
        String[] modelNames = new String[models.length];
        for (int i = 0; i < models.length; i++) {
            modelNames[i] = models[i].getDisplayName();
        }
        
        builder.setItems(modelNames, (dialogInterface, which) -> {
            AIConfig.AIModel selectedModel = models[which];
            AIConfig.getInstance().setSelectedModel(selectedModel);
            
            // Show standard project dialog with AI mode enabled
            showStandardProjectDialog(true, selectedModel);
        });
        
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
            showModeSelectionDialog(listener);
        });
        
        AlertDialog modelDialog = builder.create();
        modelDialog.show();
    }
    
    /**
     * Show standard project creation dialog
     */
    private void showStandardProjectDialog(boolean aiMode, AIConfig.AIModel selectedModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(aiMode ? "Create AI Project" : "Create Project");
        
        // Create form layout
        View formView = LayoutInflater.from(context).inflate(
                android.R.layout.activity_list_item, null);
        EditText projectNameInput = new EditText(context);
        projectNameInput.setHint("Project Name");
        projectNameInput.setTextColor(context.getResources().getColor(android.R.color.black));
        projectNameInput.setHintTextColor(context.getResources().getColor(android.R.color.darker_gray));
        
        builder.setView(projectNameInput);
        
        builder.setPositiveButton("Create", (dialogInterface, which) -> {
            String projectName = projectNameInput.getText().toString().trim();
            
            if (projectName.isEmpty()) {
                Toast.makeText(context, "Project name cannot be empty", 
                        Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (listener != null) {
                listener.onProjectCreated(projectName, aiMode, selectedModel);
            }
        });
        
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
            if (aiMode) {
                showModelSelectionDialog();
            } else {
                showModeSelectionDialog(listener);
            }
        });
        
        AlertDialog projectDialog = builder.create();
        projectDialog.show();
    }
}
