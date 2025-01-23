import axiosConfig from "../config/axiosConfig.ts";

export async function getBettingRoundByGameId(gameId: string) {
    const response = await axiosConfig.get(`/api/bets/${gameId}`);
    return response.data;
}
