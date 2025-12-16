# AI Mode Activity Integration Guide

## Overview

Diese Anleitung zeigt dir, wie du die AI Mode Features in die bestehenden Sketchware Pro Activities integrierst.

## Tool: ActivityIntegrationAdapter

Die neue Klasse `ActivityIntegrationAdapter` bietet fertige Integrations-Methoden für alle drei Activities!

---

## 1️⃣ MainActivity Integration - "+" Button

### Wo ist der "+" Button?

Der "+" Button ist normalerweise in der **MainActivity** oder einer **ProjectsListActivity**.

### How to Integrate

**Option A: Mit ActivityIntegrationAdapter (empfohlen)**

```java
import pro.sketchware.ai.ui.ActivityIntegrationAdapter;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Find your + button
        Button plusButton = findViewById(R.id.plus_button);
        
        // Integrate AI Mode
        ActivityIntegrationAdapter.integratePlusButton(this, plusButton);
    }
}
```

**Option B: Manuell**

```java
import pro.sketchware.ai.ui.NewProjectDialog;

plusButton.setOnClickListener(v -> {
    NewProjectDialog dialog = new NewProjectDialog(MainActivity.this);
    dialog.showModeSelectionDialog((projectName, aiMode, model) -> {
        // Your project creation logic here
        createProject(projectName, aiMode, model);
    });
});
```

### Was passiert?

1. User klickt "+" Button
2. Dialog zeigt: "Standard Mode" oder "AI Mode"
3. Bei "AI Mode" → Model Selection Dialog
4. Standard Project Dialog mit Projekt-Details
5. Projekt wird erstellt (mit `aiMode = true` wenn AI Mode)

---

## 2️⃣ SettingsActivity Integration - AI Settings

### Wo sind die Settings?

Normalerweise in einer **PreferencesActivity** oder **SettingsActivity**.

### How to Integrate

**Option A: Mit Preferences (wenn du Preference-Framework nutzt)**

ADD to your `preferences.xml` oder erstelle neue `ai_preferences.xml`:

```xml
<PreferenceCategory
    android:title="AI"
    android:key="ai_category">
    
    <Preference
        android:key="ai_settings"
        android:title="AI Configuration"
        android:summary="Manage API Keys and Models" />
        
</PreferenceCategory>
```

Then im PreferenceFragment:

```java
import pro.sketchware.ai.ui.ActivityIntegrationAdapter;

public class PreferencesFragment extends PreferenceFragmentCompat {
    
    @Override
    public void onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals("ai_settings")) {
            ActivityIntegrationAdapter.integrateAISettings(getActivity());
            return;
        }
        super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
```

**Option B: Mit Button (wenn du kein Preference-Framework nutzt)**

```java
import pro.sketchware.ai.ui.ActivityIntegrationAdapter;

public class SettingsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        Button aiSettingsButton = findViewById(R.id.ai_settings_button);
        aiSettingsButton.setOnClickListener(
            ActivityIntegrationAdapter.getAISettingsClickListener(this)
        );
    }
}
```

**Option C: Mit MenuItem (im Options Menu)**

```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.ai_settings) {
        ActivityIntegrationAdapter.integrateAISettings(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
}
```

### Was passiert?

1. User öffnet AI Settings
2. Dialog zeigt 3 AI-Modelle (ChatGPT, Gemini 2.5, Gemini 3)
3. User kann API Keys eingeben
4. Status zeigt ob konfiguriert (✓ oder ✗)
5. Keys werden gespeichert (in SharedPreferences)

---

## 3️⃣ EditorActivity Integration - Chat Button

### Wo ist der Editor?

Normalerweise in einer **EditorActivity** oder **CodeEditorActivity**.

### How to Integrate

**Option A: Mit ActivityIntegrationAdapter (empfohlen)**

```java
import pro.sketchware.ai.ui.ActivityIntegrationAdapter;

public class EditorActivity extends AppCompatActivity {
    
    private CodingAgent codingAgent;
    private Project currentProject;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        // Your toolbar setup
        Toolbar toolbar = findViewById(R.id.editor_toolbar);
        
        // Setup AI if project is in AI Mode
        ActivityIntegrationAdapter.setupEditorAI(this, toolbar, currentProject);
    }
}
```

**Option B: Manuell - für Toolbar**

```java
if (currentProject.isAIMode()) {
    CodingAgent codingAgent = currentProject.getCodingAgent();
    if (codingAgent != null) {
        Toolbar toolbar = findViewById(R.id.editor_toolbar);
        ActivityIntegrationAdapter.integrateChatButton(this, toolbar, codingAgent);
    }
}
```

**Option C: Manuell - für LinearLayout**

```java
if (currentProject.isAIMode()) {
    CodingAgent codingAgent = currentProject.getCodingAgent();
    if (codingAgent != null) {
        LinearLayout editorToolbar = findViewById(R.id.editor_toolbar_layout);
        ActivityIntegrationAdapter.integrateChatButton(this, editorToolbar, codingAgent);
    }
}
```

### Was passiert?

1. Chat-Button wird NUR bei AI-Mode-Projekten angezeigt
2. Standard-Projekte zeigen KEINEN Chat-Button
3. User klickt Chat-Button
4. AI Chat Dialog öffnet sich
5. User kann mit AI chatten
6. AI generiert/modifiziert Code

---

## 4️⃣ Projekt-Modell anpassen

Dein `Project` Model braucht diese Felder:

```java
public class Project {
    
    private String name;
    private String path;
    private boolean aiMode;  // NEW
    private CodingAgent codingAgent;  // NEW
    private AIConfig.AIModel selectedAIModel;  // NEW
    
    // Getter
    public boolean isAIMode() {
        return aiMode;
    }
    
    public CodingAgent getCodingAgent() {
        return codingAgent;
    }
    
    public AIConfig.AIModel getSelectedAIModel() {
        return selectedAIModel;
    }
    
    // Setter
    public void setAIMode(boolean aiMode) {
        this.aiMode = aiMode;
    }
    
    public void setCodingAgent(CodingAgent codingAgent) {
        this.codingAgent = codingAgent;
    }
    
    public void setSelectedAIModel(AIConfig.AIModel model) {
        this.selectedAIModel = model;
    }
}
```

---

## 5️⃣ Projekt-Erstellung mit AI Mode

In deiner `createProject()` Methode:

```java
private void createProject(String projectName, boolean aiMode, AIConfig.AIModel model) {
    // Create project directory
    String projectPath = getProjectPath(projectName);
    new File(projectPath).mkdirs();
    
    // Create project object
    Project project = new Project();
    project.setName(projectName);
    project.setPath(projectPath);
    project.setAIMode(aiMode);
    
    if (aiMode) {
        // Create Coding Agent
        CodingAgent codingAgent = new CodingAgent(
            projectName,
            projectPath,
            model
        );
        project.setCodingAgent(codingAgent);
        project.setSelectedAIModel(model);
    }
    
    // Save project
    saveProject(project);
    
    // Open editor
    openEditor(project);
}
```

---

## 6️⃣ App-Startup Initialization

In deiner Application-Klasse oder MainActivity onCreate():

```java
import pro.sketchware.ai.ui.ActivityIntegrationAdapter;

public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize AI on app startup
        ActivityIntegrationAdapter.initializeAI(this);
    }
}
```

Oder in MainActivity:

```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize AI
        ActivityIntegrationAdapter.initializeAI(this);
    }
}
```

Das lädt gespeicherte API-Keys aus SharedPreferences.

---

## 7️⃣ Komplettes Code-Beispiel

### MainActivity

```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize AI on app startup
        ActivityIntegrationAdapter.initializeAI(this);
        
        // Integrate "+" button
        Button plusButton = findViewById(R.id.plus_button);
        ActivityIntegrationAdapter.integratePlusButton(this, plusButton);
    }
    
    // Your createProject method
    private void createProject(String name, boolean aiMode, AIConfig.AIModel model) {
        // ... see above ...
    }
}
```

### SettingsActivity

```java
public class SettingsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        Button aiSettingsButton = findViewById(R.id.ai_settings_button);
        aiSettingsButton.setOnClickListener(
            ActivityIntegrationAdapter.getAISettingsClickListener(this)
        );
    }
}
```

### EditorActivity

```java
public class EditorActivity extends AppCompatActivity {
    
    private Project currentProject;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        // Load project
        currentProject = loadProject(getIntent().getStringExtra("project_id"));
        
        // Setup editor
        setupEditor();
        
        // Integrate AI (only if in AI Mode)
        Toolbar toolbar = findViewById(R.id.editor_toolbar);
        ActivityIntegrationAdapter.setupEditorAI(this, toolbar, currentProject);
    }
    
    private void setupEditor() {
        // Your editor setup code
    }
}
```

---

## ⚙️ AndroidManifest.xml

Vergiss nicht diese Permission:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 🧪 Testing Checklist

- [ ] App compiles
- [ ] App launches
- [ ] AI Menu Item in Settings visible
- [ ] AI Settings Dialog opens
- [ ] Can enter API keys
- [ ] Keys are saved
- [ ] "+" Button shows AI Mode option
- [ ] AI Mode Project can be created
- [ ] Chat button appears in AI projects
- [ ] Chat dialog opens
- [ ] Can send messages
- [ ] AI responds (with real API key)

---

## 🐛 Troubleshooting

### "Cannot resolve ActivityIntegrationAdapter"
- Stelle sicher, dass du importiert hast: `import pro.sketchware.ai.ui.ActivityIntegrationAdapter;`
- Rebuild: `./gradlew clean build`

### "Chat button doesn't appear"
- Prüfe: `project.isAIMode()` returns `true`
- Prüfe: `project.getCodingAgent()` ist not `null`
- Prüfe: Du bist in einem AI-Mode Projekt

### "AI Settings Dialog won't open"
- Prüfe: Imports sind korrekt
- Prüfe: Internet Permission in AndroidManifest.xml
- Prüfe: INTERNET permission ist granted (Android 6.0+)

### "API Key not saved"
- SharedPreferences braucht MODE_PRIVATE
- Prüfe: ActivityIntegrationAdapter.saveAPIKey() wird aufgerufen
- Prüfe: Key ist nicht leer

---

## 📚 Weitere Hilfe

- **AIIntegrationAdapter.java** - Alle verfügbaren Methoden
- **AI_MODE_IMPLEMENTATION.md** - Technische Details
- **AI_FEATURES.md** - Benutzer-Features
- **Discord**: http://discord.gg/kq39yhT4rX

**Ready to integrate? Let's go! 🚀**
