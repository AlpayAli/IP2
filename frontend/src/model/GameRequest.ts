export type GameRequest = {
    id: string;
    minPlayers: string;
    maxPlayers: string;
    smallBlind: string;
    security: string;
    players?: { xp: number }[];

}

export type GameData = Omit<GameRequest, 'id'>
