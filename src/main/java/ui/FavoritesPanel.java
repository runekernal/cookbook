// src/ui/FavoritesPanel.java
package ui;

import model.Recipe;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FavoritesPanel extends JPanel {
    private final JPanel favoritesListPanel; // Standard JPanel
    private final Supplier<List<Recipe>> favoritesSupplier;
    private final Consumer<Recipe> onRecipeSelected;
    private final Consumer<Recipe> onRecipeDeleted;

    public FavoritesPanel(Supplier<List<Recipe>> favoritesSupplier, Consumer<Recipe> onRecipeSelected, Consumer<Recipe> onRecipeDeleted) {
        this.favoritesSupplier = favoritesSupplier;
        this.onRecipeSelected = onRecipeSelected;
        this.onRecipeDeleted = onRecipeDeleted;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Your Favorite Recipes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // **THE KEY CHANGE IS HERE**
        // Use a standard JPanel with our custom WrapLayout.
        favoritesListPanel = new JPanel(new WrapLayout(WrapLayout.LEFT, 15, 15));
        JScrollPane scrollPane = new JScrollPane(favoritesListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshFavorites() {
        favoritesListPanel.removeAll();
        List<Recipe> favorites = favoritesSupplier.get();

        if (favorites.isEmpty()) {
            favoritesListPanel.add(new JLabel("You have no favorite recipes saved."));
        } else {
            for (Recipe recipe : favorites) {
                favoritesListPanel.add(createFavoriteCard(recipe));
            }
        }
        revalidate();
        repaint();
    }

    // The createFavoriteCard() method remains exactly the same as before.
    private JPanel createFavoriteCard(Recipe recipe) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setPreferredSize(new Dimension(200, 250));

        Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        card.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(180, 150));
        imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRecipeSelected.accept(recipe);
            }
        });

        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = new URL(recipe.getImageUrl());
                Image image = new ImageIcon(url).getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
            @Override
            protected void done() {
                try {
                    imageLabel.setIcon(get());
                } catch (Exception e) {
                    imageLabel.setText("No Image");
                }
            }
        }.execute();

        JLabel nameLabel = new JLabel("<html><body style='text-align: center;'>" + recipe.getName() + "</body></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete '" + recipe.getName() + "'?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                onRecipeDeleted.accept(recipe);
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 5));
        bottomPanel.add(nameLabel, BorderLayout.CENTER);
        bottomPanel.add(deleteButton, BorderLayout.SOUTH);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }
}