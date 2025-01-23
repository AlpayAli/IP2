# Poker Game Frontend - Texas Hold'em

## Project Description

This is the frontend for a Texas Hold'em poker game, built using Vite, React, TailwindCSS, Axios, TypeScript, Zod, Keycloak, and Material UI. The frontend communicates with a backend API to enable game functionalities, with Keycloak used for user authentication and account management.

The goal of this project is to provide a user-friendly and responsive interface for playing Texas Hold'em poker, leveraging modern frontend tools and best practices.

## Technical Setup

### Prerequisites

- **Node.js** (version 16 or higher)
- **Vite** for development and bundling
- **React** for building the UI
- **TailwindCSS** for styling
- **Axios** for API calls
- **Keycloak** for authentication
- **Zod** for validation

### Installation

1. **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd <project-directory>
    ```

2. **Configure environment variables**:
    - Create a `.env` file in the root of your project with the following content:
      ```env
      VITE_KEYCLOAK_URL=http://localhost:8080
      VITE_KEYCLOAK_REALM=your-realm
       VITE_KEYCLOAK_CLIENT_ID=your-client-id
      ```

    - Make sure to replace `your-realm` and `your-client-id` with the actual values from your Keycloak server.

3. **Install dependencies**:
    ```bash
    npm install
    ```

4. **Run the development server**:
    ```bash
    npm run dev
    ```

   The frontend will be accessible at [http://localhost:3000](http://localhost:3000).

### Keycloak Setup

1. **Keycloak Configuration**:
    - Follow the [Keycloak installation instructions](https://www.keycloak.org/docs/latest/) to set up the Keycloak server.
    - Create a client in Keycloak for this application with the correct settings (including the realm and client ID you configured in the `.env` file).

2. **Configure the frontend**:
    - The frontend will automatically use the Keycloak configuration values from the `.env` file:
      ```typescript
      const realm = import.meta.env.VITE_KEYCLOAK_REALM ?? throwEnvError('VITE_KEYCLOAK_REALM');
      const url = import.meta.env.VITE_KEYCLOAK_URL ?? throwEnvError('VITE_KEYCLOAK_URL');
      const clientId = import.meta.env. VITE_KEYCLOAK_CLIENT_ID ?? throwEnvError(' VITE_KEYCLOAK_CLIENT_ID');
      ```

### Folder Structure

The project follows a standard React folder structure:

- **src**: Contains all the source code files.
    - **components**: UI components used throughout the application.
    - **context**: React context providers for managing global state.
    - **services**: Functions for interacting with APIs.
    - **utils**: Utility functions and helpers.
    - **styles**: TailwindCSS configuration and custom styles.
    - **App.tsx**: The root component of the application.

## Game Information

### Texas Hold'em Game Rules

Texas Hold'em is a popular poker variant where each player receives two hole cards, and five community cards are shared. Players aim to form the best five-card hand using a combination of their hole cards and the community cards.

### Starting a Game

To start a new game, the player can send a POST request to the API to create a new game. Players can then join the game using the provided game ID.

### Player Actions

- **Fold**: Leave the hand and forfeit the bet.
- **Call**: Match the highest current bet.
- **Raise**: Increase the current bet.
- **Check**: Skip the turn without raising or betting.

## API Communication

The frontend communicates with the backend API running at [http://localhost:8081](http://localhost:8081).

## External Resources

- [Vite Documentation](https://vitejs.dev/)
- [React Documentation](https://react.dev/)
- [TailwindCSS Documentation](https://tailwindcss.com/docs)
- [Keycloak Documentation](https://www.keycloak.org/docs/)
- [Axios Documentation](https://axios-http.com/docs/intro)
