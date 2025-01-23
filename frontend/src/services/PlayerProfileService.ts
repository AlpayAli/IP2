import axiosConfig from "../config/axiosConfig.ts";

export async function getPlayerProfile(username: string) {
    const response = await axiosConfig.get(`/api/profile/${username}`);
    return response.data;
}