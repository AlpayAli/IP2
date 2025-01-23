import axios from "axios";
import {Player} from "../model/Player.ts";

export const getLeaderboardByBalance = async () => {
    const leaderboard = await axios.get<Player[]>('/api/leaderboard')
    return leaderboard.data
}

export const getLeaderboardByXp = async () => {
    const leaderboard = await axios.get<Player[]>('/api/leaderboard/xp')
    return leaderboard.data
}