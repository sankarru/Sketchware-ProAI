package pro.sketchware.ai.ui;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * About Team Activity with tabs for AI Label, Old Modders, and New Modders
 */
public class AboutTeamActivity extends TabActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TabHost tabHost = getTabHost();
        tabHost.setup();
        
        // AI Label Tab
        TabHost.TabSpec aiTab = tabHost.newTabSpec("ai");
        aiTab.setIndicator("AI");
        aiTab.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return createAITabContent();
            }
        });
        tabHost.addTab(aiTab);
        
        // Old Modders Tab
        TabHost.TabSpec oldModdersTab = tabHost.newTabSpec("old_modders");
        oldModdersTab.setIndicator("Old Modders");
        oldModdersTab.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return createOldModdersTabContent();
            }
        });
        tabHost.addTab(oldModdersTab);
        
        // Modders Tab (New)
        TabHost.TabSpec moddersTab = tabHost.newTabSpec("modders");
        moddersTab.setIndicator("Modders");
        moddersTab.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return createModdersTabContent();
            }
        });
        tabHost.addTab(moddersTab);
        
        tabHost.setCurrentTab(0);
    }
    
    /**
     * Create AI Tab Content
     */
    private View createAITabContent() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(15, 15, 15, 15);
        
        TextView titleText = new TextView(this);
        titleText.setText("Sketchware Pro - AI Powered");
        titleText.setTextSize(18);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        titleText.setPadding(0, 0, 0, 10);
        layout.addView(titleText);
        
        TextView descriptionText = new TextView(this);
        descriptionText.setText(
            "AI Mode Features:\n" +
            "\n• ChatGPT 5.2 Integration" +
            "\n• Google Gemini 2.5 Flash Support" +
            "\n• Google Gemini 3 Pro Preview" +
            "\n• Coding Agent for Full Project Access" +
            "\n• AI-powered Code Generation" +
            "\n• Natural Language Project Creation" +
            "\n\nThe AI Mode enables you to create Android applications using natural language." +
            "Simply describe what you want to build and the AI will generate and modify your code."
        );
        descriptionText.setTextSize(14);
        descriptionText.setLineSpacing(1.5f, 1.5f);
        layout.addView(descriptionText);
        
        return layout;
    }
    
    /**
     * Create Old Modders Tab Content
     */
    private View createOldModdersTabContent() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(15, 15, 15, 15);
        
        TextView titleText = new TextView(this);
        titleText.setText("Original Sketchware Pro Modders");
        titleText.setTextSize(16);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        titleText.setPadding(0, 0, 0, 15);
        layout.addView(titleText);
        
        String[] oldModders = {
            "Original Sketchware Team",
            "Community Contributors v1.0-v2.0",
            "Early Sketchware Pro Maintainers"
        };
        
        for (String modder : oldModders) {
            TextView modderText = new TextView(this);
            modderText.setText("• " + modder);
            modderText.setTextSize(14);
            modderText.setPadding(0, 5, 0, 5);
            layout.addView(modderText);
        }
        
        return layout;
    }
    
    /**
     * Create Modders Tab Content (New)
     */
    private View createModdersTabContent() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(15, 15, 15, 15);
        
        TextView titleText = new TextView(this);
        titleText.setText("Current Modders");
        titleText.setTextSize(16);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        titleText.setPadding(0, 0, 0, 15);
        layout.addView(titleText);
        
        // Main current modders
        String[] currentModders = {
            "justinsanjp - AI Integration & Project Lead",
            "Community Contributors"
        };
        
        for (String modder : currentModders) {
            TextView modderText = new TextView(this);
            modderText.setText("• " + modder);
            modderText.setTextSize(14);
            modderText.setTypeface(null, android.graphics.Typeface.BOLD);
            modderText.setPadding(0, 5, 0, 5);
            modderText.setTextColor(0xFF1976D2);
            layout.addView(modderText);
        }
        
        // Contributions note
        TextView contributionsText = new TextView(this);
        contributionsText.setText(
            "\nThank you for contributing to Sketchware Pro!" +
            "\n\nIf you're interested in contributing, please visit our GitHub repository."
        );
        contributionsText.setTextSize(12);
        contributionsText.setPadding(0, 15, 0, 0);
        contributionsText.setLineSpacing(1.5f, 1.5f);
        layout.addView(contributionsText);
        
        return layout;
    }
}
