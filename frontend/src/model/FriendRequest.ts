export class FriendRequest {
    id: string;
    senderId: string;
    receiverId: string;
    senderUsername: string;
    accepted: boolean;
    requestDate: Date;

    constructor(id: string, senderId: string, receiverId: string, senderUsername: string, accepted: boolean, requestDate: Date) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderUsername = senderUsername;
        this.accepted = accepted;
        this.requestDate = requestDate;
    }
}