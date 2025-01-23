import axiosConfig from "../config/axiosConfig.ts";
import { Message } from "../model/Message.ts";

export async function getMessages(gameId: string): Promise<Message[]> {
    const response = await axiosConfig.get(`/api/messages/${gameId}`);
    return response.data;
}

export async function sendMessage(gameId: string, content: string): Promise<Message> {
    const response = await axiosConfig.post(`/api/messages/send/${gameId}`, content, {
        headers : {
            "Content-Type" : "text/plain"
        }
    });
    return response.data;
}