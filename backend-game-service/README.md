# Poker Game API - Texas Hold'em

## Project Description

This is an API for a Texas Hold'em poker game, built using the Spring Framework in Java. The application uses a PostgreSQL database for data storage and Keycloak for user registration and account security.

The aim of this project is to provide a robust backend for playing Texas Hold'em poker through an API, which developers can use to support the game's frontend.

## Technical Setup

### Requirements

- **Java 17** or higher
- **Spring Framework**
- **PostgreSQL** database (port 5432)
- **Keycloak** for user authentication and management

### Installation

1. **Backend Application**:
   - Clone the repository:
     ```bash
     git clone <repository-url>
     cd <project-directory>
     ```
   - Ensure you have a **PostgreSQL** database running on port 5432 and configure the connection in the `application.properties` file.
   - Start the Spring application:
     ```bash
     ./gradlew bootRun
     ```
   - The API will be running at [http://localhost:8081](http://localhost:8081).

2. **Keycloak Setup**:
   - Follow the Keycloak installation instructions and start the Keycloak server.
   - Configure Keycloak with a client for this application and ensure proper settings for user registration.

### Database Configuration

- Ensure you have a PostgreSQL database running on port 5432.
- Use the appropriate schemas for game and user data.

## Game Information

### Texas Hold'em Rules

Texas Hold'em is a popular poker variant where each player is dealt two hole cards, and five community cards are shared. Players attempt to make the best five-card hand by combining their hole cards with the community cards.

### Starting a Game

A player can start a new game by sending a POST request to the API. The game will be set up, and players can join to participate.

### Actions During a Round

- **Fold**: Leave the hand and forfeit the current bet.
- **Call**: Match the current highest bet.
- **Raise**: Increase the current bet.
- **Check**: Skip the turn without raising or betting.

## API Usage

The API runs at [http://localhost:8081](http://localhost:8081) and is accessible via the `/api/` prefix.

# Available Endpoints

## **Bets**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| POST   | `/api/bets/{roundId}/{playerId}/{action}/{raiseAmount}`        | Perform an action for a player in a specific round (e.g., bet, raise, fold) |

## **Decks**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| GET    | `/api/decks/shuffle`                                           | Retrieve a shuffled deck                         |

## **Files**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| POST   | `/api/files/test-bucket`                                       | Test the accessibility of the Google Cloud Storage bucket |
| POST   | `/api/files/upload`                                            | Upload a file to the Google Cloud Storage bucket |
| GET    | `/api/files/generate-signed-url`                               | Generate a V4 signed URL for a file in a specific folder |
| GET    | `/api/files/download`                                          | Download a file from the Google Cloud Storage bucket |

## **Friends**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| GET    | `/api/friends`                                                 | Retrieve the friends of the logged-in player    |
| GET    | `/api/friends/requests`                                        | Retrieve the friend requests for the logged-in player |
| POST   | `/api/friends/{friendId}`                                      | Send a friend request to a player               |
| POST   | `/api/friends/nickname/{nickname}`                             | Send a friend request to a player by nickname  |
| DELETE | `/api/friends/friendRequests/{playerId}`                       | Decline a friend request from another player    |
| DELETE | `/api/friends/{friendId}`                                      | Remove a friend from the player’s list          |

## **Games**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| POST   | `/api/games/new`                                               | Create a new game                               |
| GET    | `/api/games/open`                                              | Retrieve a list of open games                   |
| POST   | `/api/games/{gameId}/start`                                    | Start a game                                    |
| POST   | `/api/games/{gameId}/join`                                     | Join a game                                     |
| POST   | `/api/games/{gameId}/leave`                                    | Leave a game                                    |
| GET    | `/api/games/{gameId}`                                          | Retrieve a specific game                        |
| GET    | `/api/games/latest`                                            | Retrieve the latest game started by the host    |

## **Invites**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| POST   | `/api/invites/{gameId}/{receiverName}`                          | Send an invitation to a player                  |
| POST   | `/api/invites/{invitationId}/accept`                           | Accept an invitation                            |
| POST   | `/api/invites/{invitationId}/decline`                          | Decline an invitation                           |
| GET    | `/api/invites`                                                 | Retrieve a list of invitations for the logged-in player |

## **Messages**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| POST   | `/api/messages/send/{gameId}`                                  | Send a message to a game                        |
| GET    | `/api/messages/{gameId}`                                       | Retrieve messages for a specific game           |

## **Players**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| GET    | `/api/players/isRegistered`                                    | Check if a player is registered                 |
| POST   | `/api/players/register`                                        | Register a new player                           |
| GET    | `/api/players/leaderboard`                                     | Retrieve players ranked by XP                   |
| GET    | `/api/players/currentPlayer`                                   | Retrieve details of the logged-in player        |
| GET    | `/api/players/{id}/balance`                                    | Retrieve the balance of a player                |
| POST   | `/api/players/changeName/{newName}`                            | Change the name of the logged-in player         |
| POST   | `/api/players/update`                                          | Update the name and avatar of the player        |

## **Rounds**
| Method | Endpoint                                                      | Description                                      |
|--------|---------------------------------------------------------------|--------------------------------------------------|
| POST   | `/api/rounds/initialize/{gameId}`                              | Initialize a new round for a game               |
| PUT    | `/api/rounds/{roundId}/advance`                                | Advance a round to the next phase               |
| GET    | `/api/rounds/{roundId}`                                        | Retrieve a specific round                       |

## External Resources

- [Spring Framework](https://spring.io/)
- [PostgreSQL](https://www.postgresql.org/docs/)
- [Keycloak Documentation](https://www.keycloak.org/docs/)
