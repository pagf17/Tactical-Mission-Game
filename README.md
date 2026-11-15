# Tactical Mission Game

A turn-based tactical game developed in **Java** as an academic project for the **Data Structures** course.

The player controls **Tó Cruz**, who must navigate a building, complete a mission objective, manage health and equipment, deal with moving enemies, and reach an exit. Missions and building layouts are loaded from JSON files.

## Features

- **Two game modes**
  - Manual mode: choose the player's actions through a text-based menu.
  - Automatic mode: the game selects an entry point and makes movement and combat decisions automatically.
- **Mission selection:** includes two versions of the *Pata de Coelho* mission.
- **Graph-based maps:** rooms are represented as vertices and connections as edges. Edge weights are updated according to the game state.
- **Navigation assistance:** displays routes towards the mission objective, the nearest medical kit, and an exit.
- **Turn-based gameplay:** the player and enemies take turns; enemies can move between connected rooms and attack the player.
- **Combat and inventory:** fight enemies, collect medical kits, use healing items, and equip protective vests.
- **JSON data:** mission/map configuration is loaded from JSON files.
- **Results tracking:** completed manual games are exported to `resultados.json`, and previous results can be viewed in the game menu.

## Technologies

- Java 17
- Maven
- JSON Simple 4.0.1
- Git

## Data structures

A key part of this project is the implementation and use of data structures. The codebase includes custom implementations of structures such as:

- Lists and unordered lists
- Linked lists
- Stacks
- Queues and priority queues
- Binary trees and heaps
- Graphs and network extensions
- Iterators and sorting/searching utilities

These structures support the game's map, navigation, inventory, and other gameplay operations.

## Project structure

```text
.
├── README.md
└── trabalhoed/
    ├── pom.xml
    ├── missao.json
    ├── missao2.json
    ├── resultados.json
    └── src/
        └── main/
            └── java/
                ├── Estruturas/
                ├── Interfaces/
                └── Jogo/
                    ├── App.java
                    ├── Jogo.java
                    ├── Mapa.java
                    ├── Missao.java
                    └── ...
```

## Requirements

- Java Development Kit (JDK) 17 or later
- Apache Maven

Check your installations:

```bash
java -version
mvn -version
```

## How to run

1. Clone the repository:

   ```bash
   git clone https://github.com/pagf17/Tactical-Mission-Game.git
   ```

2. Open a terminal in the `trabalhoed` directory:

   ```bash
   cd Tactical-Mission-Game/trabalhoed
   ```

3. Compile the project:

   ```bash
   mvn clean compile
   ```

4. Run the `Jogo.App` main class from your IDE, keeping `trabalhoed` as the working directory so the JSON mission files can be found.

   Alternatively, if Maven's Exec plugin is available:

   ```bash
   mvn exec:java -Dexec.mainClass="Jogo.App"
   ```

5. Follow the menu in the terminal to select a mission and a game mode.

## Gameplay

In manual mode, the menu allows the player to:

- Move between connected rooms
- Use a medical kit
- Collect available items
- Check the player's status
- Attack enemies
- View the map
- Interact with the mission objective or extract once it is complete
- End a turn or abandon the mission

In automatic mode, the game chooses an entry point and runs the mission using its automatic turn logic.

## Configuration

The mission files (`missao.json` and `missao2.json`) contain the building layout, room connections, items, enemies, entry/exit points, and mission objective. You can inspect these files to understand how a mission is configured.

---

*Academic project developed for learning and applying data structures and algorithms in Java.*
