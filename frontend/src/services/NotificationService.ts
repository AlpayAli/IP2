import axiosConfig from "../config/axiosConfig.ts";
import {NotificationResponse} from "../model/Notification.ts";

export async function getNotifications(){
    const response = await axiosConfig.get<NotificationResponse[]>('/api/notifications');
    return response.data;
}
