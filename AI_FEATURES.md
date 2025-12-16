# Sketchware Pro - AI Mode Features

## 🤖 AI Mode Overview

Sketchware Pro now includes a powerful **AI Mode** that lets you create Android applications using natural language descriptions instead of traditional visual programming or code.

## ✨ Key Features

### 1. **Dual Mode Project Creation**
- **Standard Mode**: Traditional Sketchware Pro experience
- **AI Mode**: Create projects with AI assistance

### 2. **AI Model Support**

Three state-of-the-art AI models available:
- **ChatGPT 5.2** - Advanced reasoning and code generation
- **Google Gemini 2.5 Flash** - Fast and efficient responses
- **Google Gemini 3 Pro Preview** - Cutting-edge capabilities

### 3. **Coding Agent**

The AI Coding Agent provides:
- Full project access (read/write)
- Real-time code generation
- Project structure understanding
- Automatic code modification
- Works like Cursor editor for Android development

### 4. **Chat-Based Interface**

- Chat bubble icon in editor toolbar (AI mode only)
- Natural language commands
- Real-time AI responses
- Code suggestions and generation
- Project-aware context

## 🚀 How to Use

### Setting Up AI Mode

1. **Configure API Keys**
   - Open Settings → AI Settings
   - Enter your API keys:
     - ChatGPT: [Get from platform.openai.com](https://platform.openai.com/api-keys)
     - Gemini: [Get from ai.google.dev](https://ai.google.dev/)
   - Save the settings

2. **Create an AI Project**
   - Tap the "+" button
   - Select "AI Mode"
   - Choose your preferred AI model
   - Enter project name
   - Start building!

### Using the AI Chat

1. Open a project in AI Mode
2. Tap the chat bubble icon in the editor
3. Describe what you want to do:
   ```
   "Create a login screen with email and password fields"
   "Add a button that calls an API endpoint"
   "Generate a RecyclerView for displaying a list of items"
   ```
4. The AI will generate and apply code changes

## 📋 Example Prompts

### Creating Layouts
```
"Create a main activity with a text input field and a submit button"
"Design a dashboard with cards showing statistics"
"Make a chat interface with message bubbles"
```

### Adding Functionality
```
"Add Firebase authentication to the project"
"Implement a local database using SQLite"
"Connect to a REST API for weather data"
```

### Code Modifications
```
"Change the primary color to blue throughout the app"
"Add error handling to all network requests"
"Refactor the MainActivity to use MVVM architecture"
```

## 🔒 Security & Privacy

- API keys are stored in app memory (not disk-encrypted in current version)
- Internet connection required for AI features
- All code generation requests go through official APIs
- Your project files are processed locally on your device

## 📊 Model Comparison

| Feature | ChatGPT 5.2 | Gemini 2.5 Flash | Gemini 3 Pro |
|---------|------------|------------------|-------------|
| Speed | Fast | Very Fast | Fast |
| Accuracy | Very High | High | Excellent |
| Cost | Standard | Budget-friendly | Premium |
| Best For | Complex logic | Quick responses | Advanced tasks |

## ⚙️ System Requirements

- Android 6.0 or higher
- Internet connection
- API key for at least one AI model
- Sufficient storage for projects

## 🎯 Use Cases

1. **Rapid Prototyping**
   - Quickly build app prototypes
   - Test ideas without extensive coding

2. **Learning**
   - Learn Android development
   - Understand best practices
   - See generated code examples

3. **Production Apps**
   - Generate boilerplate code
   - Implement complex features faster
   - Maintain code quality

4. **Professional Development**
   - Speed up development cycle
   - Focus on business logic
   - Reduce repetitive coding

## 💡 Tips & Tricks

1. **Be Specific**: Describe exactly what you want
   ```
   ❌ "Make a button"
   ✅ "Create a green button with text 'Submit' that sends form data"
   ```

2. **Ask for Multiple Features**: The AI can handle complex requests
   ```
   "Create a login screen with email validation, 
   a remember me checkbox, and a forgot password link"
   ```

3. **Iterate**: Build progressively
   - First: Create basic layout
   - Then: Add functionality
   - Finally: Polish and optimize

4. **Review Generated Code**: Always check the generated code before accepting

## 🐛 Troubleshooting

### "AI not configured"
- Go to Settings → AI Settings
- Add API keys for your desired models

### "API Error 401"
- Check if your API key is correct
- Verify the API key has active quota

### "Changes not applied"
- Check file permissions
- Verify project path is accessible
- Check AI response format

### "Slow responses"
- Try using Gemini 2.5 Flash (faster)
- Check your internet connection
- May be due to API rate limiting

## 📚 Learn More

- [Implementation Guide](AI_MODE_IMPLEMENTATION.md)
- [GitHub Repository](https://github.com/justinsanjp/Sketchware-Pro)
- [Discord Community](http://discord.gg/kq39yhT4rX)

## 🎁 What's Included

✅ AI Configuration Management
✅ Three AI Models (ChatGPT + Gemini variants)
✅ Coding Agent with full project access
✅ Chat-based UI
✅ API Key management
✅ Project context awareness
✅ Automatic code application
✅ Settings integration
✅ About/Team information with AI label

## 🙏 Credits

- **Lead Developer**: justinsanjp
- **Original Sketchware**: Sketchware Team
- **Sketchware Pro Community**: All contributors

## 📝 Version History

- **1.0.0** - Initial AI Mode release
  - ChatGPT 5.2 integration
  - Google Gemini support
  - Coding Agent implementation
  - AI Chat interface
  - Settings management

## 🚦 Future Roadmap

- [ ] Voice input support
- [ ] Code diff preview
- [ ] Syntax highlighting in chat
- [ ] Custom system prompts
- [ ] Local model support
- [ ] Undo/Redo for AI changes
- [ ] Conversation export
- [ ] Performance optimizations

---

**Happy Building! 🎉**
