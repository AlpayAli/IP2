import axios from "axios";

const API_URL = "/api/gimmicks";

export const getGimmicks = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const getPlayerGimmicks = async () => {
    const response = await axios.get(`${API_URL}/player`);
    return response.data;
};

export const purchaseGimmick = async (gimmickId: string, type : string) => {
    const response = await axios.post(`${API_URL}/purchase/${gimmickId}/${type}`);
    return response.data;
};

export const getAvailableGimmicks = async () => {
    const response = await axios.get(`${API_URL}/available`);
    return response.data;
}