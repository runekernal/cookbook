// src/ui/RecipeDetailsPanel.java
package ui;

import model.Recipe;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeDetailsPanel extends JPanel {
    private final JLabel titleLabel;
    private final JLabel imageLabel;
    private final JTextArea instructionsArea;
    private final JPanel ingredientsPanel;
    private final JButton saveButton;
    private Recipe currentRecipe;

    public RecipeDetailsPanel(Consumer<Recipe> saveFavoriteCallback) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Title and Save Button
        JPanel topPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Select a recipe to see details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        saveButton = new JButton("❤️ Save to Favorites");
        saveButton.addActionListener(e -> {
            if (currentRecipe != null) {
                saveFavoriteCallback.accept(currentRecipe);
            }
        });
        topPanel.add(saveButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Main content split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.4);
        add(splitPane, BorderLayout.CENTER);

        // Left Panel: Image and Ingredients
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        leftPanel.add(imageLabel, BorderLayout.NORTH);

        ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsPanel);
        ingredientsScrollPane.setBorder(BorderFactory.createTitledBorder("Ingredients"));
        leftPanel.add(ingredientsScrollPane, BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // Right Panel: Instructions
        instructionsArea = new JTextArea("Instructions will appear here.");
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);
        instructionsArea.setEditable(false);
        instructionsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        instructionsScrollPane.setBorder(BorderFactory.createTitledBorder("Instructions"));
        splitPane.setRightComponent(instructionsScrollPane);
    }

    public void displayRecipe(Recipe recipe) {
        this.currentRecipe = recipe;
        titleLabel.setText(recipe.getName());
        instructionsArea.setText(recipe.getInstructions());

        // Load image in a background thread
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL imageUrl = new URL(recipe.getImageUrl());
                ImageIcon imageIcon = new ImageIcon(imageUrl);
                Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }

            @Override
            protected void done() {
                try {
                    imageLabel.setIcon(get());
                } catch (Exception e) {
                    imageLabel.setText("Image not available");
                    e.printStackTrace();
                }
            }
        }.execute();

        // Display ingredients
        ingredientsPanel.removeAll();
        for (Map.Entry<String, String> entry : recipe.getIngredients().entrySet()) {
            JLabel ingredientLabel = new JLabel("• " + entry.getValue() + " " + entry.getKey());
            ingredientLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            ingredientsPanel.add(ingredientLabel);
        }

        revalidate();
        repaint();
    }
}