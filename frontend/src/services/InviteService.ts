import axios from "axios";
import {Invite} from "../model/Invite.ts";

export const sendInvite = async (username: string, gameId: string) => {
    return await axios.post(`/api/invites/${gameId}/${username}`)
}

export const inviteAllFriends = async (gameId: string) => {
    return await axios.post(`/api/invites/${gameId}/all`)
}

export const acceptInvite = async (inviteId: string) => {
    const response = await axios.post(`/api/invites/${inviteId}/accept`)
    return response.data as Invite
}
export const declineInvite = async (inviteId: string) => {
    return await axios.post(`/api/invites/${inviteId}/decline`)
}