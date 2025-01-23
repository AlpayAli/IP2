import axios from "axios";

export const postPlayerAction = async (roundId : string, actionType : string, raiseAmount : number) => {
    const response = await axios.post(`/api/bets`, {roundId, actionType, raiseAmount});
    return response.data;
}