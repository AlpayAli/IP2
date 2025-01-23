import {Friend} from "../../model/Friend.ts";
import {useFriends} from "../../hooks/useFriends.ts";
import {useNavigate} from "react-router-dom";
import {FriendRequest} from "../../model/FriendRequest.ts";
import {removeFriend} from "../../services/FriendService.ts";
import {useContext, useState} from "react";
import CurrentPlayerContext, {ICurrentPlayerContext} from "../../context/player/CurrentPlayerContext.ts";
import Account from "../general/Account.tsx";
import {AddFriendDialog} from "./AddFriendDialog.tsx";
import {playSound} from "../../utils/SoundUtils.ts";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function FriendList() {
    const {friends, friendRequests, isLoadingFriends, isErrorLoadingFriends} = useFriends();
    const navigate = useNavigate();
    const {currentPlayer} = useContext<ICurrentPlayerContext>(CurrentPlayerContext);
    const [isAddFriendOpen, setIsAddFriendOpen] = useState(false)
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'friendsAndRequests') return 'Friends & Requests';
                if (key === 'friends') return 'Friends';
                if (key === 'noFriendsYet') return "You don't have any friends yet.";
                if (key === 'friendRequests') return 'Friend Requests';
                if (key === 'noPendingFriendRequests') return 'No pending friend requests.';
                if (key === 'back') return 'Back';
                if (key === 'addNewFriend') return 'Add New Friend';
                break;
            case 'NL':
                if (key === 'friendsAndRequests') return 'Vrienden & Verzoeken';
                if (key === 'friends') return 'Vrienden';
                if (key === 'noFriendsYet') return 'Je hebt nog geen vrienden.';
                if (key === 'friendRequests') return 'Vriendschapsverzoeken';
                if (key === 'noPendingFriendRequests') return 'Geen openstaande vriendschapsverzoeken.';
                if (key === 'back') return 'Terug';
                if (key === 'addNewFriend') return 'Nieuwe Vriend Toevoegen';
                break;
            case 'FR':
                if (key === 'friendsAndRequests') return 'Amis & Demandes';
                if (key === 'friends') return 'Amis';
                if (key === 'noFriendsYet') return 'Vous n\'avez pas encore d\'amis.';
                if (key === 'friendRequests') return 'Demandes d\'amis';
                if (key === 'noPendingFriendRequests') return 'Aucune demande d\'ami en attente.';
                if (key === 'back') return 'Retour';
                if (key === 'addNewFriend') return 'Ajouter un nouvel ami';
                break;
            default:
                if (key === 'friendsAndRequests') return 'Friends & Requests';
                if (key === 'friends') return 'Friends';
                if (key === 'noFriendsYet') return "You don't have any friends yet.";
                if (key === 'friendRequests') return 'Friend Requests';
                if (key === 'noPendingFriendRequests') return 'No pending friend requests.';
                if (key === 'back') return 'Back';
                if (key === 'addNewFriend') return 'Add New Friend';
                break;
        }
        return '';
    };


    if (isLoadingFriends) {
        return <div className="flex items-center justify-center min-h-screen bg-gray-100">Loading friends...</div>;
    }

    if (isErrorLoadingFriends) {
        return <div className="flex items-center justify-center min-h-screen bg-gray-100">Error loading friends.</div>;
    }

    return (
        <>
            <AddFriendDialog isOpen={isAddFriendOpen} onClose={() => setIsAddFriendOpen(false)}/>
            <div className={"grid grid-cols-3 gap-10 content-center p-10 pt-28 h-screen"}>
                {currentPlayer ?
                    <Account currentPlayer={currentPlayer}/>
                    :
                    <div className={"w-full h-full bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative"}>
                        ERROR
                    </div>
                }
                <div className="w-full rounded-3xl shadow-lg p-6 col-span-2 bg-blue-500 relative">
                    <h1 className="text-yellow font-bold text-4xl mb-7">
                        {getTranslatedText('friendsAndRequests')}
                    </h1>
                    <div className="space-y-10">
                        {/* Friends Section */}
                        <div>
                            <h2 className="text-2xl font-semibold text-yellow mb-4">
                                {getTranslatedText('friends')}
                            </h2>
                            <div className="space-y-4">
                                {friends.length > 0 ? (
                                    friends.map((friend) => <FriendListItem key={friend.id} friend={friend}/>)
                                ) : (
                                    <p className="text-white">
                                        {getTranslatedText('noFriendsYet')}
                                    </p>
                                )}
                            </div>
                        </div>

                        {/* Friend Requests Section */}
                        <div>
                            <h2 className="text-2xl font-semibold text-yellow mb-4">
                                {getTranslatedText('friendRequests')}
                            </h2>
                            <div className="space-y-4">
                                {friendRequests.length > 0 ? (
                                    friendRequests.map((friendRequest) => (
                                        <FriendRequestListItem key={friendRequest.id} friendRequest={friendRequest}/>
                                    ))
                                ) : (
                                    <p className="text-white">
                                        {getTranslatedText('noPendingFriendRequests')}
                                    </p>
                                )}
                            </div>
                        </div>
                    </div>


                    <div className="flex justify-start space-x-4 mt-8 absolute bottom-10">
                        <button
                            onClick={() => navigate("/")}
                            className="bg-red text-white py-2 px-4 rounded-lg font-semibold shadow-lg shadow-black hover:opacity-70 transition"
                        >
                            {getTranslatedText('back')}
                        </button>
                        <button
                            onClick={() => setIsAddFriendOpen(true)}
                            className="bg-yellow text-blue-500 py-2 px-4 rounded-lg shadow-lg hover:opacity-70 shadow-black font-semibold transition"
                        >
                            {getTranslatedText('addNewFriend')}
                        </button>
                    </div>
                </div>
            </div>
        </>
    );
}

function FriendListItem({friend}: { friend: Friend }) {
    return (
        <div className="flex justify-between items-center bg-yellow p-4 rounded-lg shadow-sm">
            <div className="cursor-pointer"
                onClick={() => window.open(`/profile/${friend.nickname}`, "_blank")}
            >
                <span className="text-lg font-medium text-gray-800">{friend.nickname}</span>
            </div>
            <button
                onClick={() => removeFriend(friend.id)}
                className="bg-red text-white py-1 px-3 rounded-lg font-medium hover:opacity-70 transition"
            >
                Remove
            </button>
        </div>
    );
}

function FriendRequestListItem({friendRequest}: { friendRequest: FriendRequest }) {
    const {addFriend, mutateDeclineFriendRequest} = useFriends();
    const {soundLevel} = useContext<ISettingsContext>(SettingsContext);
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'accept') return 'Accept';
                if (key === 'decline') return 'Decline';
                break;
            case 'NL':
                if (key === 'accept') return 'Accepteren';
                if (key === 'decline') return 'Afwijzen';
                break;
            case 'FR':
                if (key === 'accept') return 'Accepter';
                if (key === 'decline') return 'Refuser';
                break;
            default:
                if (key === 'accept') return 'Accept';
                if (key === 'decline') return 'Decline';
                break;
        }
        return '';
    };
    return (
        <div className="flex justify-between items-center bg-yellow p-4 rounded-lg shadow-sm">
            <span className="text-lg font-medium text-blue-500">{friendRequest.senderUsername}</span>
            <div className="space-x-2">
                <button
                    onClick={() => {
                        playSound("https://storage.googleapis.com/image_bucket_ip2/sounds/click.wav", soundLevel)
                        addFriend(friendRequest.senderId)
                    }}
                    className="bg-green-500 text-white py-1 px-3 rounded-lg font-medium hover:bg-green-600 transition"
                >
                    {getTranslatedText('accept')}
                </button>
                <button
                    onClick={() => mutateDeclineFriendRequest(friendRequest.senderId)}
                    className="bg-red text-white py-1 px-3 rounded-lg font-medium hover:opacity-70 transition"
                >
                    {getTranslatedText('decline')}
                </button>
            </div>
        </div>
    );
}
