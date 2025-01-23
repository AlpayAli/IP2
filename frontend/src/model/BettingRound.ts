import {PlayerAction} from "./PlayerAction.ts";
import {PlayerInGame} from "./PlayerInGame.ts";

export type BettingRound = {
    id: string;
    playerActionDtoList: PlayerAction[];
    currentHighestBet: number;
    activePlayer: PlayerInGame;
};
