import axios from "axios";

export const askChatBot = async (message: string) => {
    const response = await axios.post('/api/chatbot', {"query_text": message})
    return response.data
}

export const calculateStats = async (minPlayers: number, maxPlayers: number, playTime: number, minAge : number, usersRated : number, domains : string[], mechanics : string[]) => {
    const response = await axios.post('/api/chatbot/stats', {
        "min_players": minPlayers,
        "max_players": maxPlayers,
        "play_time": playTime,
        "min_age": minAge,
        "users_rated": usersRated,
        "domains": domains,
        "mechanics": mechanics
    })
    return response.data
}