// src/ui/SearchPanel.java
package ui;

import model.Recipe;
import service.RecipeAPIService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public class SearchPanel extends JPanel {
    private final JTextField searchField;
    private final JPanel resultsPanel; // Back to a standard JPanel
    private final RecipeAPIService apiService;
    private final Consumer<Recipe> onRecipeSelected;
    private final Timer searchTimer;

    public SearchPanel(Consumer<Recipe> onRecipeSelected) {
        this.apiService = new RecipeAPIService();
        this.onRecipeSelected = onRecipeSelected;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search bar panel (no changes here)
        JPanel searchBarPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel(" 🔍 ");
        searchLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        searchField = new JTextField("Chicken");
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchBarPanel.add(searchLabel, BorderLayout.WEST);
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        add(searchBarPanel, BorderLayout.NORTH);

        // **THE KEY CHANGE IS HERE**
        // We use a standard JPanel, but set its layout to our new WrapLayout.
        resultsPanel = new JPanel(new WrapLayout(WrapLayout.LEFT, 15, 15));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Timer and listener logic (no changes here)
        searchTimer = new Timer(500, e -> performSearch());
        searchTimer.setRepeats(false);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchTimer.restart(); }
            public void removeUpdate(DocumentEvent e) { searchTimer.restart(); }
            public void changedUpdate(DocumentEvent e) { searchTimer.restart(); }
        });

        performSearch();
    }

    // The performSearch() and createRecipeCard() methods remain exactly the same as before.
    // ... (rest of the SearchPanel code is unchanged)

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        resultsPanel.removeAll();
        JLabel loadingLabel = new JLabel("Searching for '" + query + "'...");
        loadingLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        resultsPanel.add(loadingLabel);
        resultsPanel.revalidate();
        resultsPanel.repaint();

        new SwingWorker<List<Recipe>, Void>() {
            @Override
            protected List<Recipe> doInBackground() throws Exception {
                return apiService.searchRecipes(query);
            }

            @Override
            protected void done() {
                resultsPanel.removeAll();
                try {
                    List<Recipe> recipes = get();
                    if (recipes.isEmpty()) {
                        resultsPanel.add(new JLabel("No recipes found for '" + query + "'"));
                    } else {
                        for (Recipe recipe : recipes) {
                            resultsPanel.add(createRecipeCard(recipe));
                        }
                    }
                } catch (Exception e) {
                    resultsPanel.add(new JLabel("Error fetching recipes. Check your connection."));
                    e.printStackTrace();
                }
                resultsPanel.revalidate();
                resultsPanel.repaint();
            }
        }.execute();
    }

    private JPanel createRecipeCard(Recipe recipe) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setPreferredSize(new Dimension(200, 220));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        card.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(180, 150));

        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = new URL(recipe.getImageUrl());
                Image image = new ImageIcon(url).getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
            @Override
            protected void done() {
                try { imageLabel.setIcon(get()); } catch (Exception e) { imageLabel.setText("No Image"); }
            }
        }.execute();

        JLabel nameLabel = new JLabel("<html><body style='text-align: center;'>" + recipe.getName() + "</body></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { onRecipeSelected.accept(recipe); }
        });
        return card;
    }
}