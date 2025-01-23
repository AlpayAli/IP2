import axiosConfig from "../config/axiosConfig.ts";
import {Achievement} from "../model/Achievement.ts";

export async function getAchievements() {
    const response = await axiosConfig.get(`/api/achievements`);
    return response.data as Achievement[];
}


export async function getRecentAchievement() {
    const response = await axiosConfig.get(`/api/achievements/player/recent`);
    return response.data as Achievement[];
}