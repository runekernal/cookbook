// src/service/RecipeAPIService.java
package service;

import model.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RecipeAPIService {

    private static final String API_BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    public List<Recipe> searchRecipes(String query) throws Exception {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        URL url = new URL(API_BASE_URL + "search.php?s=" + encodedQuery);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();

        return parseRecipes(sb.toString());
    }

    private List<Recipe> parseRecipes(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        if (jsonObject.isNull("meals")) {
            return recipes; // Return empty list if no meals are found
        }

        JSONArray meals = jsonObject.getJSONArray("meals");
        for (int i = 0; i < meals.length(); i++) {
            JSONObject meal = meals.getJSONObject(i);
            Recipe recipe = new Recipe();

            recipe.setId(meal.getString("idMeal"));
            recipe.setName(meal.getString("strMeal"));
            recipe.setCategory(meal.optString("strCategory"));
            recipe.setInstructions(meal.optString("strInstructions"));
            recipe.setImageUrl(meal.optString("strMealThumb"));
            recipe.setYoutubeUrl(meal.optString("strYoutube"));

            // The API stores ingredients in up to 20 separate fields
            for (int j = 1; j <= 20; j++) {
                String ingredient = meal.optString("strIngredient" + j);
                String measure = meal.optString("strMeasure" + j);
                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    recipe.addIngredient(ingredient, measure);
                }
            }
            recipes.add(recipe);
        }
        return recipes;
    }
}