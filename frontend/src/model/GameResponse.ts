import {Player} from "./Player.ts";
import {PlayerInGame} from "./PlayerInGame.ts";
import {BettingRound} from "./BettingRound.ts";

export type GameResponse = {
    id: string;
    minPlayers: number;
    maxPlayers: number;
    smallBlind: number;
    currentRound: CurrentRound;
    players: PlayerInGame[];
    currentSmallBlindPlayer: Player;
    host: Player;
    status: string;
    winner : PlayerInGame;
}


export interface CurrentRound {
    id: string;
    status: string;
    tableCards: string[];
    gameId: string;
    pot: number;
    players: Player[];
    bettingRounds: BettingRound[];
    winners: Player[];
}

