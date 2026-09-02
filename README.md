# 🍳 CookBook

CookBook is a desktop recipe-finder app built with Java Swing. Search for 
recipes by name, browse detailed ingredients and step-by-step instructions, 
and save the ones you love to a personal favorites list — all in a clean, 
dark-themed interface powered by FlatLaf.

## Features

- 🔍 **Search** — Look up recipes by name using the [TheMealDB](https://www.themealdb.com/api.php) API
- 📖 **Recipe Details** — View ingredients, measurements, instructions, a photo, and a linked YouTube tutorial
- ❤️ **Favorites** — Save recipes you like and revisit them anytime
- 💾 **Local Persistence** — Favorites are saved to disk, so your list survives between sessions
- 🎨 **Modern UI** — Dark theme via FlatLaf, with a responsive wrapping layout for recipe cards

## Tech Stack

- Java 21
- Swing (UI)
- Maven (build)
- [org.json](https://github.com/stleary/JSON-java) for API response parsing
- [FlatLaf](https://www.formdev.com/flatlaf/) for theming
- [TheMealDB](https://www.themealdb.com/) free API as the recipe data source

## Getting Started

```bash
git clone https://github.com/runekernal/cookbook.git
cd cookbook
mvn clean package
mvn exec:java -Dexec.mainClass="Main"
```

## Project Structure

```
src/main/java/
├── Main.java              # App entry point
├── model/Recipe.java      # Recipe data model
├── service/                # TheMealDB API client
├── storage/                # Favorites persistence (serialization)
└── ui/                     # Swing panels (Search, Details, Favorites)
```
