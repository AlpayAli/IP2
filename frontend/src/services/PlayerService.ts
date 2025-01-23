import axios from "axios";
import {Player} from "../model/Player.ts";

export const getPlayer = async () => {
    const player = await axios.get<Player>('/api/players/currentPlayer')
    return player.data
}

export const isRegistered = () => {
    return axios.get('/api/players/isRegistered')
}

export const register = () => {
    return axios.post('/api/players/register')
}

export const updateUsername = (newName: string, file?: File) => {
    const formData = new FormData();
    formData.append("newName", newName);
    if (file) {
        formData.append("file", file);
    }

    return axios.post('/api/players/update', formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
};

export async function updatePlayer(updatedPlayer: Player) {
    await axios.put('/api/players', updatedPlayer);
}

export async function dailySpin() {
    const spin = await axios.post('/api/players/spin');
    return spin.data as number;
}