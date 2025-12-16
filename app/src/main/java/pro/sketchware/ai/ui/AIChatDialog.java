package pro.sketchware.ai.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import pro.sketchware.ai.CodingAgent;

/**
 * Chat Dialog for AI Coding Agent
 * Provides interface similar to Cursor for code generation and modification
 */
public class AIChatDialog {
    
    private Context context;
    private CodingAgent codingAgent;
    private AlertDialog dialog;
    private LinearLayout chatContainer;
    private EditText messageInput;
    private Button sendButton;
    private ScrollView scrollView;
    
    public AIChatDialog(Context context, CodingAgent codingAgent) {
        this.context = context;
        this.codingAgent = codingAgent;
    }
    
    /**
     * Show the AI Chat Dialog
     */
    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("AI Coding Agent - " + codingAgent.getSelectedModel().getDisplayName());
        
        // Create chat interface
        View chatView = createChatInterface();
        builder.setView(chatView);
        
        builder.setNegativeButton("Close", (dialogInterface, which) -> {
            dialog.dismiss();
        });
        
        dialog = builder.create();
        dialog.show();
        
        // Load chat history
        loadChatHistory();
    }
    
    /**
     * Create the chat interface
     */
    private View createChatInterface() {
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(10, 10, 10, 10);
        
        // Chat history scroll view
        scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        
        chatContainer = new LinearLayout(context);
        chatContainer.setOrientation(LinearLayout.VERTICAL);
        chatContainer.setPadding(5, 5, 5, 5);
        
        scrollView.addView(chatContainer);
        mainLayout.addView(scrollView);
        
        // Input area
        LinearLayout inputLayout = new LinearLayout(context);
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        inputLayout.setPadding(0, 10, 0, 0);
        
        messageInput = new EditText(context);
        messageInput.setHint("Ask the AI to generate or modify code...");
        messageInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        messageInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | 
                android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        inputLayout.addView(messageInput);
        
        sendButton = new Button(context);
        sendButton.setText("Send");
        sendButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT));
        sendButton.setOnClickListener(v -> sendMessage());
        inputLayout.addView(sendButton);
        
        mainLayout.addView(inputLayout);
        
        return mainLayout;
    }
    
    /**
     * Load chat history from conversation
     */
    private void loadChatHistory() {
        chatContainer.removeAllViews();
        
        List<CodingAgent.Message> history = codingAgent.getConversationHistory();
        for (CodingAgent.Message message : history) {
            if (message.role.equals("system")) continue;
            
            addMessageToChatUI(message.content, message.role);
        }
        
        // Auto scroll to bottom
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
    
    /**
     * Add message to chat UI
     */
    private void addMessageToChatUI(String content, String role) {
        TextView messageView = new TextView(context);
        messageView.setText(content);
        messageView.setTextSize(12);
        messageView.setPadding(10, 10, 10, 10);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        messageView.setLayoutParams(params);
        
        if (role.equals("user")) {
            messageView.setBackgroundColor(0xFFE3F2FD);  // Light blue
            messageView.setTextColor(0xFF1976D2);  // Blue text
        } else if (role.equals("assistant")) {
            messageView.setBackgroundColor(0xFFF3E5F5);  // Light purple
            messageView.setTextColor(0xFF7B1FA2);  // Purple text
        }
        
        chatContainer.addView(messageView);
    }
    
    /**
     * Send message to AI
     */
    private void sendMessage() {
        String userMessage = messageInput.getText().toString().trim();
        if (userMessage.isEmpty()) {
            Toast.makeText(context, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Add user message to UI
        addMessageToChatUI(userMessage, "user");
        messageInput.setText("");
        
        // Send to AI in background
        new ChatTask().execute(userMessage);
    }
    
    /**
     * Async task for sending messages to AI
     */
    private class ChatTask extends AsyncTask<String, Void, String> {
        private Exception error;
        
        @Override
        protected String doInBackground(String... params) {
            try {
                return codingAgent.chat(params[0]);
            } catch (Exception e) {
                error = e;
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(String result) {
            if (error != null) {
                Toast.makeText(context, "Error: " + error.getMessage(), 
                        Toast.LENGTH_LONG).show();
                addMessageToChatUI("Error: " + error.getMessage(), "assistant");
            } else if (result != null) {
                addMessageToChatUI(result, "assistant");
            }
            
            // Auto scroll to bottom
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }
    
    /**
     * Clear chat history
     */
    public void clearHistory() {
        codingAgent.clearHistory();
        chatContainer.removeAllViews();
        messageInput.setText("");
    }
}
