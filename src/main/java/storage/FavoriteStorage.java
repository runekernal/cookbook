// src/storage/FavoriteStorage.java
package storage;

import model.Recipe;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteStorage {

    private static final String FILE_PATH = "favorites.ser";

    public void saveFavorites(List<Recipe> recipes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(recipes);
        } catch (IOException e) {
            System.err.println("Error saving favorites: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Recipe> loadFavorites() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list if file doesn't exist
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Recipe>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading favorites: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }
}