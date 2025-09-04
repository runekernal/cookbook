package model;

import java.io.Serializable;
import java.util.Map;
import java.util.LinkedHashMap;

public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization

    private String id;
    private String name;
    private String category;
    private String instructions;
    private String imageUrl;
    private String youtubeUrl;
    private Map<String, String> ingredients;

    public Recipe() {
        this.ingredients = new LinkedHashMap<>(); // Preserves insertion order
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
    public Map<String, String> getIngredients() { return ingredients; }
    public void setIngredients(Map<String, String> ingredients) { this.ingredients = ingredients; }

    public void addIngredient(String ingredient, String measure) {
        if (ingredient != null && !ingredient.trim().isEmpty()) {
            this.ingredients.put(ingredient, measure);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recipe recipe = (Recipe) obj;
        return id.equals(recipe.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Recipe{name='" + name + "'}";
    }
}
