# Pokémon App 🚀

An Android application built with Jetpack Compose to explore Pokémon data. It fetches information from the public [PokeAPI](https://pokeapi.co) service.

## Features ✨
- Browse the first generation of Pokémon (151 creatures).
- Search by name and sort by different criteria.
- View detailed statistics, types and evolution chain.
- Mark favorites for quick access.

## API 🔗
The app uses **PokeAPI**, an open REST API that provides Pokémon details, species information and evolution data. Requests are made via HTTPS using Ktor.

## Tech Stack 🛠
- **Kotlin** and **Jetpack Compose** for modern Android UI.
- **Ktor** HTTP client for network operations.
- **Koin** for dependency injection.
- **Coil** for loading images.
- Build scripts in **Gradle Kotlin DSL**.

## Getting Started ▶️
Open the project in Android Studio Giraffe or later and run on a device or emulator (minSdk 26).

## Screenshots 📷

### Home Screen 🏠

<img src="pk1.png" width="300" alt="Home screen" />

A simple list of Pokémon with search. 🐉

### Detail Screen 📋

<img src="pk2.png" width="300" alt="Detail screen" />

Detailed Pokémon information with evolutions. ⚡