# Network-Based Tic-Tac-Toe with Spectator Mode

This project was developed as part of the DUT Informatique curriculum at the IUT of Montpellier during the 2019-2020 academic year. 
It demonstrates a networked application with client-server architecture, allowing two players to engage in a Tic-Tac-Toe game remotely, 
with support for spectators who can watch the game in real-time.

## Features

- **Multiplayer Tic-Tac-Toe**: A simple yet fully functional game allowing two players to connect over a network.
- **Spectator Mode**: Enables multiple spectators to join and watch an ongoing game in real-time.
- **Interactive GUI**: A graphical interface enhances user experience with intuitive gameplay.
- **Threaded Server**: Efficient management of multiple connections using Java's threading model.
- **Socket-Based Communication**: Implements TCP sockets for robust client-server interaction.

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later.
- IntelliJ IDEA or any Java-compatible IDE.
- Open network ports for hosting multiplayer games.

### Installation

1. Clone the repository:

   ```bash
   git clone <repository-url>
   ```

2. Open the project in IntelliJ IDEA.

3. Run the `MainApps` class to start the program.

### Usage

1. **Select Game Mode**:
   - `1`: Join a game as a player.
   - `2`: Watch a game as a spectator.
   - `3`: Host a game for another player.
   - `4`: Exit.

2. **Hosting a Game**:
   - Ensure the required port is open on your router.
   - Share your IP and port with the other player.

3. **Playing**:
   - Interact with the graphical interface to place your symbol.
   - Spectators can view the game through their terminals or GUI.

---

## Architecture Overview

The project uses a **client-server architecture** to facilitate communication between players and spectators. Below is a high-level description of the architecture:

- **Server**:
  - Hosts the game and manages player connections.
  - Uses threads to handle multiple clients (players and spectators) simultaneously.
  - Synchronizes game state updates between players and spectators.

- **Client**:
  - Connects to the server to participate in the game.
  - Sends user actions (e.g., moves) to the server and receives updates.

- **Spectator Threads**:
  - Created dynamically for each spectator joining the game.
  - Pushes real-time game state updates to connected spectators.

### Package Structure

1. **Graphical Interface**: Contains classes for rendering the game board and capturing player inputs.
2. **Game Logic**: Includes the `Morpion` class, which encapsulates game rules and state management.
3. **Network Communication**:
   - Implements client-server communication using Java's `Socket` and `ServerSocket` classes.
   - Handles connections, message exchange, and synchronization.

This modular architecture ensures clear separation of concerns, making it easier to maintain and extend the project.

---

## Future Enhancements

- Support for selecting from a list of ongoing games.
- Improved graphical interface to display additional details (e.g., game stats, turn indicators).
- Modular architecture to support additional games beyond Tic-Tac-Toe.

---

## Contributors

- Erwan Le Goff
- Adrien Corn
- Florent Le Maire

**Project Supervisor**: Francis Garcia

---

## Acknowledgments

- OpenClassrooms course: *Java et la programmation réseau* for foundational knowledge on sockets.
- Tutorials and guidance from IUT faculty, especially on threading and client-server models.

---
