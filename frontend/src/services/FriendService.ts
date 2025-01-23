import axios from "axios";
import {Friend} from "../model/Friend.ts";
import {FriendRequest} from "../model/FriendRequest.ts";

export const getFriends = async () => {
    const friends = await axios.get<Friend[]>('/api/friends')
    return friends.data
}

export const addFriend = async (friendId: string) => {
    await axios.post(`/api/friends/${friendId}`)
}

export const addFriendByNickname = async (nickname: string) => {
    await axios.post(`/api/friends/nickname/${nickname}`)
}

export const removeFriend = async (friendId: string): Promise<void> => {
    await axios.delete(`/api/friends/${friendId}`);
};


export const getFriendRequests = async () => {
    const friendRequests = await axios.get<FriendRequest[]>('/api/friends/requests')
    return friendRequests.data
}

export const acceptFriendRequest = async (friendId: string) => {
    await axios.post(`/api/friends/friendRequests/${friendId}`)
}

export const declineFriendRequest = async (playerId: string) => {
    await axios.delete(`/api/friends/friendRequests/${playerId}`)
}
