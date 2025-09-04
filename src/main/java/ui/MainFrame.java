// src/ui/MainFrame.java
package ui;

import model.Recipe;
import storage.FavoriteStorage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final JTabbedPane tabbedPane;
    private final RecipeDetailsPanel recipeDetailsPanel;
    private final FavoritesPanel favoritesPanel;
    private final FavoriteStorage favoriteStorage;
    private List<Recipe> favoriteRecipes;

    public MainFrame() {
        setTitle("Recipe Finder App");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        favoriteStorage = new FavoriteStorage();
        favoriteRecipes = favoriteStorage.loadFavorites();
        if (favoriteRecipes == null) {
            favoriteRecipes = new ArrayList<>();
        }

        tabbedPane = new JTabbedPane();

        // Panels
        SearchPanel searchPanel = new SearchPanel(this::showRecipeDetails);
        recipeDetailsPanel = new RecipeDetailsPanel(this::addRecipeToFavorites);
        favoritesPanel = new FavoritesPanel(this::getFavoriteRecipes, this::showRecipeDetails, this::deleteRecipeFromFavorites);

        tabbedPane.addTab("🔍 Search", searchPanel);
        tabbedPane.addTab("📖 Recipe Details", recipeDetailsPanel);
        tabbedPane.addTab("❤️ Favorites", favoritesPanel);

        // Refresh favorites when the tab is selected
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == favoritesPanel) {
                favoritesPanel.refreshFavorites();
            }
        });

        add(tabbedPane);
    }

    private List<Recipe> getFavoriteRecipes() {
        return this.favoriteRecipes;
    }

    private void showRecipeDetails(Recipe recipe) {
        recipeDetailsPanel.displayRecipe(recipe);
        tabbedPane.setSelectedComponent(recipeDetailsPanel);
    }

    private void addRecipeToFavorites(Recipe recipe) {
        if (!favoriteRecipes.contains(recipe)) {
            favoriteRecipes.add(recipe);
            favoriteStorage.saveFavorites(favoriteRecipes);
            JOptionPane.showMessageDialog(this, "'" + recipe.getName() + "' added to favorites!");
        } else {
            JOptionPane.showMessageDialog(this, "'" + recipe.getName() + "' is already in your favorites.", "Already Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteRecipeFromFavorites(Recipe recipe) {
        favoriteRecipes.remove(recipe);
        favoriteStorage.saveFavorites(favoriteRecipes);
        favoritesPanel.refreshFavorites(); // Refresh the view after deleting
        JOptionPane.showMessageDialog(this, "'" + recipe.getName() + "' removed from favorites.");
    }
}