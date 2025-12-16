package pro.sketchware.ai;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Context Manager
 * Maintains and provides access to project structure and files
 * Allows the Coding Agent to read and modify project files
 */
public class ProjectContext {
    
    private String projectPath;
    private List<ProjectFile> projectFiles;
    
    public static class ProjectFile {
        public String path;
        public String content;
        public String fileType;  // "java", "xml", "gradle", etc.
        
        public ProjectFile(String path, String content, String fileType) {
            this.path = path;
            this.content = content;
            this.fileType = fileType;
        }
    }
    
    public ProjectContext(String projectPath) {
        this.projectPath = projectPath;
        this.projectFiles = new ArrayList<>();
        loadProjectStructure();
    }
    
    /**
     * Load project structure and important files
     */
    private void loadProjectStructure() {
        try {
            // Load key project files
            loadFile("AndroidManifest.xml");
            loadFile("build.gradle");
            loadFile("settings.gradle");
            
            // Load Java source files
            loadJavaSourceFiles();
            
            // Load XML layout files
            loadLayoutFiles();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load Java source files recursively
     */
    private void loadJavaSourceFiles() {
        File srcDir = new File(projectPath + "/app/src/main/java");
        if (srcDir.exists()) {
            loadFilesRecursively(srcDir, "java");
        }
    }
    
    /**
     * Load XML layout files
     */
    private void loadLayoutFiles() {
        File layoutDir = new File(projectPath + "/app/src/main/res/layout");
        if (layoutDir.exists()) {
            loadFilesRecursively(layoutDir, "xml");
        }
    }
    
    /**
     * Recursively load files from directory
     */
    private void loadFilesRecursively(File directory, String fileType) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    loadFilesRecursively(file, fileType);
                } else if (file.getName().endsWith("." + (fileType.equals("java") ? "java" : "xml"))) {
                    try {
                        String relativePath = file.getAbsolutePath()
                                .substring(projectPath.length());
                        String content = new String(Files.readAllBytes(file.toPath()));
                        projectFiles.add(new ProjectFile(relativePath, content, fileType));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    /**
     * Load a specific file
     */
    private void loadFile(String fileName) {
        try {
            String path = projectPath + "/" + fileName;
            File file = new File(path);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                String fileType = getFileType(fileName);
                projectFiles.add(new ProjectFile(fileName, content, fileType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get file type based on extension
     */
    private String getFileType(String fileName) {
        if (fileName.endsWith(".java")) return "java";
        if (fileName.endsWith(".xml")) return "xml";
        if (fileName.endsWith(".gradle")) return "gradle";
        if (fileName.endsWith(".json")) return "json";
        return "unknown";
    }
    
    /**
     * Get full project context as string for AI
     */
    public String getProjectContext() {
        StringBuilder context = new StringBuilder();
        context.append("Project Path: ").append(projectPath).append("\n\n");
        context.append("Project Structure:\n");
        context.append("================\n\n");
        
        for (ProjectFile file : projectFiles) {
            context.append("File: ").append(file.path).append("\n");
            context.append("Type: ").append(file.fileType).append("\n");
            // Limit content size to avoid token overflow
            String contentPreview = file.content.length() > 500 ? 
                    file.content.substring(0, 500) + "..." : 
                    file.content;
            context.append("Content:\n").append(contentPreview).append("\n\n");
        }
        
        return context.toString();
    }
    
    /**
     * Get file by path
     */
    public ProjectFile getFile(String filePath) {
        for (ProjectFile file : projectFiles) {
            if (file.path.equals(filePath)) {
                return file;
            }
        }
        return null;
    }
    
    /**
     * Apply code changes to project file
     */
    public void applyCodeChange(String filePath, String newContent, String language) {
        try {
            File targetFile = new File(projectPath + filePath);
            
            // Create parent directories if they don't exist
            targetFile.getParentFile().mkdirs();
            
            // Write new content
            try (FileWriter writer = new FileWriter(targetFile)) {
                writer.write(newContent);
            }
            
            // Update project context
            ProjectFile existing = getFile(filePath);
            if (existing != null) {
                existing.content = newContent;
            } else {
                projectFiles.add(new ProjectFile(filePath, newContent, language));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create new file in project
     */
    public void createFile(String filePath, String content) {
        applyCodeChange(filePath, content, getFileType(filePath));
    }
    
    /**
     * Get all project files
     */
    public List<ProjectFile> getAllFiles() {
        return new ArrayList<>(projectFiles);
    }
    
    /**
     * Refresh project structure (reload from disk)
     */
    public void refresh() {
        projectFiles.clear();
        loadProjectStructure();
    }
}
