# Sketchware Pro AI Mode Implementation Guide

## Overview
This guide explains how to integrate the AI Mode feature into the existing Sketchware Pro codebase.

## Core Components Created

### 1. **AIConfig.java**
- Location: `app/src/main/java/pro/sketchware/ai/AIConfig.java`
- Purpose: Manages AI model configuration and API keys
- Models:
  - ChatGPT 5.2
  - Google Gemini 2.5 Flash
  - Google Gemini 3 Pro Preview

### 2. **CodingAgent.java**
- Location: `app/src/main/java/pro/sketchware/ai/CodingAgent.java`
- Purpose: The main AI agent that handles code generation and project modifications
- Features:
  - Conversation history tracking
  - Full project access
  - Code parsing and application
  - Works like Cursor editor

### 3. **AIAPIClient.java**
- Location: `app/src/main/java/pro/sketchware/ai/AIAPIClient.java`
- Purpose: Handles API communication with ChatGPT and Gemini
- Supports:
  - ChatGPT API calls
  - Google Gemini API calls
  - Error handling
  - Timeout management

### 4. **ProjectContext.java**
- Location: `app/src/main/java/pro/sketchware/ai/ProjectContext.java`
- Purpose: Maintains project structure and enables AI to access/modify files
- Features:
  - Project file loading
  - File modification
  - File creation
  - Recursive directory traversal

### 5. **NewProjectDialog.java**
- Location: `app/src/main/java/pro/sketchware/ai/ui/NewProjectDialog.java`
- Purpose: UI for project creation with AI/Standard mode selection
- Flow:
  1. Mode selection dialog
  2. (If AI) Model selection dialog
  3. Project details dialog

### 6. **AIChatDialog.java**
- Location: `app/src/main/java/pro/sketchware/ai/ui/AIChatDialog.java`
- Purpose: Chat interface for the AI Coding Agent
- Features:
  - Message history display
  - Async message sending
  - Auto-scrolling
  - Syntax highlighting

### 7. **AISettingsDialog.java**
- Location: `app/src/main/java/pro/sketchware/ai/ui/AISettingsDialog.java`
- Purpose: Settings interface for configuring API keys
- Features:
  - Per-model API key configuration
  - Configuration status display
  - Password field masking

### 8. **AboutTeamActivity.java**
- Location: `app/src/main/java/pro/sketchware/ai/ui/AboutTeamActivity.java`
- Purpose: About/Team information with three tabs
- Tabs:
  - AI (new) - AI Mode features
  - Old Modders (renamed) - Original team
  - Modders (new) - Current modders (justinsanjp, etc)

## Integration Steps

### Step 1: Add Permission to AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Step 2: Modify Main Activity (MainActivity or equivalent)
Add these imports:
```java
import pro.sketchware.ai.AIConfig;
import pro.sketchware.ai.ui.NewProjectDialog;
import pro.sketchware.ai.ui.AISettingsDialog;
```

Modify the "+" button (new project) handler:
```java
plusButton.setOnClickListener(v -> {
    NewProjectDialog dialog = new NewProjectDialog(MainActivity.this);
    dialog.showModeSelectionDialog((projectName, aiMode, model) -> {
        // Create project
        createProject(projectName, aiMode, model);
    });
});
```

### Step 3: Modify Editor Activity (for AI Chat button)
Add chat bubble icon to editor toolbar:
```java
ImageButton aiChatButton = new ImageButton(context);
aiChatButton.setImageResource(android.R.drawable.ic_menu_sort_by_size);
aiChatButton.setOnClickListener(v -> {
    if (currentProject.isAIMode()) {
        AIChatDialog chatDialog = new AIChatDialog(EditorActivity.this, currentProject.getCodingAgent());
        chatDialog.show();
    }
});
toolbar.addView(aiChatButton);
```

### Step 4: Add Settings Menu Item
In your settings menu or preferences activity:
```java
menuItem_AISettings.setOnClickListener(v -> {
    AISettingsDialog settingsDialog = new AISettingsDialog(context);
    settingsDialog.show();
});
```

### Step 5: Update About/Team Activity
Replace your current About Team activity with the new `AboutTeamActivity.java`

## Usage Flow

### Creating an AI Project
1. User clicks "+" button
2. Dialog appears: "Standard Mode" or "AI Mode"
3. If AI Mode:
   - Model selection dialog appears
   - User selects ChatGPT or Gemini
   - Project details dialog appears
4. Project is created with AI agent initialized

### Using AI Mode Editor
1. Editor opens with chat bubble icon in toolbar
2. User clicks chat bubble
3. AI Chat dialog appears
4. User describes what to do
5. AI generates/modifies code
6. Changes are applied to project

### Configuring AI
1. User opens Settings → AI Settings
2. Dialog shows three models
3. User enters API keys
4. Keys are saved securely

## API Key Configuration

**Before AI Mode can be used, users must:**

1. Get API keys:
   - **ChatGPT 5.2**: Visit https://platform.openai.com/api-keys
   - **Gemini 2.5 Flash**: Visit https://ai.google.dev/
   - **Gemini 3 Pro Preview**: Visit https://ai.google.dev/

2. Go to Settings → AI Settings

3. Enter each API key

## Security Considerations

⚠️ **Important:**
- API keys are stored in memory (not encrypted on disk)
- Consider implementing encryption for production
- Never expose API keys in logs
- Consider implementing API key rotation

## Prompt Engineering

The Coding Agent uses this system prompt:
```
You are an expert Android app developer and Sketchware Pro specialist.
You have full access to the project structure and can generate, modify, and refactor code.
```

Customize this in `CodingAgent.java` → `initializeSystemPrompt()` method.

## Testing Checklist

- [ ] Project creation works in both modes
- [ ] AI configuration dialog functions properly
- [ ] API calls work for all three models
- [ ] Chat interface displays messages correctly
- [ ] Code changes are applied to project
- [ ] About/Team tabs display correctly
- [ ] File persistence works
- [ ] Error handling for missing API keys
- [ ] Error handling for failed API calls
- [ ] Performance with large projects

## Future Enhancements

1. **Code Syntax Highlighting** in chat
2. **Image Support** for UI design
3. **Voice Input** for commands
4. **Code Diff Preview** before applying changes
5. **Undo/Redo** for AI-generated changes
6. **Custom System Prompts** per project
7. **Multi-language Support**
8. **Offline Mode** with local models
9. **Conversation Export**
10. **Prompt Templates**

## Troubleshooting

### "AI not configured" message
- Solution: Go to Settings → AI Settings and enter API keys

### API errors (401, 403)
- Check API key is correct
- Check API key has proper permissions
- Check API key hasn't expired

### Slow responses
- May be API rate limiting
- Check internet connection
- Consider using faster models (Gemini 2.5 Flash)

### Code not applying
- Check project path is correct
- Check file permissions
- Check code format in AI response

## Support

For issues or questions:
- GitHub Issues: https://github.com/justinsanjp/Sketchware-Pro/issues
- Discord: http://discord.gg/kq39yhT4rX
