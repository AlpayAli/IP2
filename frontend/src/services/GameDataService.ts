import {GameRequest, GameData} from "../model/GameRequest.ts";
import axiosConfig from "../config/axiosConfig.ts";
import {GameResponse} from "../model/GameResponse.ts";

export const createGame = async (game: GameData) => {
    const response = await axiosConfig.post('/api/games/new', game);
    return response.data as GameResponse;
}

export const startingGame = async (gameId: string) => {
    const response = await axiosConfig.post(`/api/games/${gameId}/start`);
    return response.data;
}

export async function getOpenGames() {
    const response = await axiosConfig.get<GameRequest[]>('/api/games/open');
    return response.data as GameRequest[];
}

export async function getActiveGames() {
    const response = await axiosConfig.get<GameRequest[]>('/api/games/active');
    return response.data as GameRequest[];

}

export async function joinGame(gameId: string) {
    const response = await axiosConfig.post(`/api/games/${gameId}/join`);
    return response.data;
}

export async function leaveGame(gameId: string) {
    const response = await axiosConfig.post(`/api/games/${gameId}/leave`);
    return response.data;
}

export async function getGame(gameId: string) {
    const response = await axiosConfig.get(`/api/games/${gameId}`);
    return response.data as GameResponse;
}