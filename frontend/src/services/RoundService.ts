import axiosConfig from "../config/axiosConfig.ts";

export async function initializeRound(gameId: string) {
    const response = await axiosConfig.post(`/api/rounds/initialize/${gameId}`);
    return response.data;
}

