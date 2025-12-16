# AI Mode Integration Checklist

## Overview
This checklist guides you through integrating AI Mode into the existing Sketchware Pro codebase.

## Phase 1: Setup & Configuration ✅

- [ ] Review `README.md` (already updated)
- [ ] Review `AI_FEATURES.md` for user-facing features
- [ ] Review `AI_MODE_IMPLEMENTATION.md` for technical details
- [ ] Review `MANIFEST_UPDATES.md` for manifest changes

## Phase 2: Project Structure

- [ ] Verify folder structure:
  ```
  app/src/main/java/pro/sketchware/ai/
  ├── AIConfig.java
  ├── AIAPIClient.java
  ├── AIIntegration.java
  ├── CodingAgent.java
  ├── ProjectContext.java
  └── ui/
      ├── AboutTeamActivity.java
      ├── AIChatDialog.java
      ├── AISettingsDialog.java
      └── NewProjectDialog.java
  ```

- [ ] All 8 Java files are in correct locations
- [ ] Documentation files are in repo root:
  - [ ] AI_FEATURES.md
  - [ ] AI_MODE_IMPLEMENTATION.md
  - [ ] MANIFEST_UPDATES.md (this file)
  - [ ] README.md (updated)

## Phase 3: Manifest & Gradle Updates

- [ ] Add `INTERNET` permission to AndroidManifest.xml
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />
  ```

- [ ] Verify JSON dependency in build.gradle (if not already present):
  ```gradle
  implementation 'org.json:json:20231013'
  ```

- [ ] If using Proguard, add rules from MANIFEST_UPDATES.md

## Phase 4: Main Activity Integration

### For Project Creation (+Button)

- [ ] Find your main activity (MainActivity or equivalent)
- [ ] Add import:
  ```java
  import pro.sketchware.ai.ui.NewProjectDialog;
  import pro.sketchware.ai.AIConfig;
  ```

- [ ] Find the "+" button handler (new project button)
- [ ] Replace/modify with:
  ```java
  plusButton.setOnClickListener(v -> {
      NewProjectDialog dialog = new NewProjectDialog(MainActivity.this);
      dialog.showModeSelectionDialog((projectName, aiMode, model) -> {
          // Your existing project creation code
          createProject(projectName, aiMode, model);
      });
  });
  ```

- [ ] Test the flow:
  - [ ] Click "+" button
  - [ ] See "Standard Mode" and "AI Mode" options
  - [ ] Select AI Mode
  - [ ] See model selection
  - [ ] Complete project creation

## Phase 5: Settings Integration

- [ ] Find settings/preferences activity
- [ ] Add AI Settings menu item or option
- [ ] Add click listener:
  ```java
  import pro.sketchware.ai.AIIntegration;
  import pro.sketchware.ai.ui.AISettingsDialog;
  
  aiSettingsMenuItem.setOnClickListener(v -> {
      AISettingsDialog settingsDialog = new AISettingsDialog(SettingsActivity.this);
      settingsDialog.show();
  });
  ```

- [ ] Test the flow:
  - [ ] Open Settings
  - [ ] Find "AI Settings"
  - [ ] Click to open
  - [ ] See three models
  - [ ] Try adding API keys
  - [ ] See "Configured" status

## Phase 6: Editor Activity Integration

### For AI Chat Button

- [ ] Find your editor activity (EditorActivity or equivalent)
- [ ] Add import:
  ```java
  import pro.sketchware.ai.CodingAgent;
  import pro.sketchware.ai.AIIntegration;
  import pro.sketchware.ai.AIConfig;
  import pro.sketchware.ai.ui.AIChatDialog;
  ```

- [ ] In your toolbar initialization, add chat button (only for AI projects):
  ```java
  if (currentProject.isAIMode() && currentProject.getCodingAgent() != null) {
      ImageButton aiChatButton = AIIntegration.createAIChatButton(
          EditorActivity.this, 
          currentProject.getCodingAgent()
      );
      toolbar.addView(aiChatButton);
  }
  ```

- [ ] Test the flow:
  - [ ] Create an AI Mode project
  - [ ] Open editor
  - [ ] See chat bubble icon in toolbar
  - [ ] Click chat bubble
  - [ ] See chat interface
  - [ ] Send a test message
  - [ ] See AI response

## Phase 7: About/Team Activity

- [ ] Replace or integrate `AboutTeamActivity.java`
- [ ] Update About menu to point to new activity:
  ```java
  Intent aboutIntent = new Intent(MainActivity.this, AboutTeamActivity.class);
  startActivity(aboutIntent);
  ```

- [ ] Test the flow:
  - [ ] Open About section
  - [ ] See "AI" tab with AI Mode info
  - [ ] See "Old Modders" tab (renamed)
  - [ ] See "Modders" tab with justinsanjp

## Phase 8: Project Model Updates

- [ ] Update your Project model/class to include:
  ```java
  private boolean aiMode;
  private CodingAgent codingAgent;
  private AIConfig.AIModel selectedAIModel;
  
  public boolean isAIMode() { return aiMode; }
  public CodingAgent getCodingAgent() { return codingAgent; }
  public AIConfig.AIModel getSelectedAIModel() { return selectedAIModel; }
  ```

- [ ] Update project save/load logic to persist AI mode info

## Phase 9: Testing

### Basic Functionality
- [ ] App compiles without errors
- [ ] App launches successfully
- [ ] Main menu displays correctly

### AI Mode Creation
- [ ] "+" button shows mode selection
- [ ] Standard Mode works as before
- [ ] AI Mode requires API configuration
- [ ] AI Model selection works
- [ ] Project creation succeeds

### Settings
- [ ] Settings activity opens
- [ ] AI Settings dialog opens
- [ ] Can enter API keys
- [ ] Configuration persists
- [ ] Status shows correct info

### Editor
- [ ] Standard projects don't show AI chat
- [ ] AI projects show chat button
- [ ] Chat dialog opens
- [ ] Messages display correctly
- [ ] API calls work (test with real API key)

### About Screen
- [ ] Three tabs visible
- [ ] AI tab shows features
- [ ] Old Modders tab shows history
- [ ] Modders tab shows justinsanjp

## Phase 10: API Configuration Testing

- [ ] Get API keys:
  - [ ] ChatGPT: https://platform.openai.com/api-keys
  - [ ] Gemini: https://ai.google.dev/

- [ ] Configure in app:
  - [ ] Settings → AI Settings
  - [ ] Add ChatGPT key
  - [ ] Add Gemini key
  - [ ] Verify configuration status

- [ ] Test API calls:
  - [ ] Create AI project
  - [ ] Open chat
  - [ ] Send test message: "Hello"
  - [ ] Receive response

## Phase 11: Documentation

- [ ] README.md updated ✅
- [ ] AI_FEATURES.md available ✅
- [ ] AI_MODE_IMPLEMENTATION.md available ✅
- [ ] MANIFEST_UPDATES.md available ✅
- [ ] Code comments are clear
- [ ] JavaDoc comments added if needed

## Phase 12: Performance & Optimization

- [ ] API calls are async (don't block UI)
- [ ] Chat loading indicators present
- [ ] Long responses handled correctly
- [ ] Memory usage is reasonable
- [ ] No crashes with large projects
- [ ] Error handling is robust

## Phase 13: Final Review

- [ ] All tests passed
- [ ] No compiler warnings
- [ ] No runtime errors
- [ ] Code follows existing style
- [ ] Features match specification
- [ ] Documentation is complete
- [ ] Ready for PR/merge

## Quick Reference: Key Files

| File | Purpose | Status |
|------|---------|--------|
| AIConfig.java | Model & API key management | ✅ Created |
| CodingAgent.java | Main AI agent | ✅ Created |
| AIAPIClient.java | API communication | ✅ Created |
| ProjectContext.java | Project file access | ✅ Created |
| NewProjectDialog.java | Project creation UI | ✅ Created |
| AIChatDialog.java | Chat interface | ✅ Created |
| AISettingsDialog.java | Settings UI | ✅ Created |
| AboutTeamActivity.java | About screen | ✅ Created |
| AIIntegration.java | Integration helpers | ✅ Created |
| README.md | Main documentation | ✅ Updated |
| AI_FEATURES.md | User guide | ✅ Created |
| AI_MODE_IMPLEMENTATION.md | Dev guide | ✅ Created |
| MANIFEST_UPDATES.md | Manifest changes | ✅ Created |

## Support & Help

If you encounter issues:

1. Check the error message carefully
2. Review `AI_MODE_IMPLEMENTATION.md` troubleshooting
3. Check GitHub Issues
4. Ask on Discord: http://discord.gg/kq39yhT4rX

## Merge Completion Checklist

- [ ] All files committed
- [ ] All tests passing
- [ ] Documentation complete
- [ ] Code reviewed
- [ ] Ready to push
- [ ] Create PR to main repository
- [ ] Get feedback from maintainers
- [ ] Address review comments
- [ ] Merge to main

---

**Last Updated**: December 16, 2025
**Created By**: AI Integration Team
**Status**: 🟢 Integration Ready
