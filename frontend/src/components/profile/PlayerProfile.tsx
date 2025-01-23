import { useContext, useState } from "react";
import { usePlayerInformation } from "../../hooks/usePlayerInformation";
import { useParams } from "react-router-dom";
import { Star } from "@mui/icons-material";
import { CurrencyIcon } from "../general/CurrencyIcon.tsx";
import { useFriends } from "../../hooks/useFriends";
import CurrentPlayerContext, { ICurrentPlayerContext } from "../../context/player/CurrentPlayerContext.ts";
import SettingsContext, { ISettingsContext } from "../../context/settings/SettingsContext.ts";

function PlayerProfile() {
    const { username } = useParams();
    const { isLoading, isError, playerProfile } = usePlayerInformation(username!);
    const { addFriendByNickname } = useFriends();
    const { currentPlayer } = useContext<ICurrentPlayerContext>(CurrentPlayerContext);

    const [showFriends, setShowFriends] = useState(false);
    const [showAchievements, setShowAchievements] = useState(false);

    const { currency, language } = useContext<ISettingsContext>(SettingsContext);

    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'loading') return 'Loading...';
                if (key === 'error') return 'Error';
                if (key === 'notAvailable') return 'Player not available.';
                if (key === 'profileEnd') return "'s Profile";
                if (key === 'friends') return 'Friends';
                if (key === 'achievements') return 'Achievements';
                if (key === 'roundsWon') return 'Rounds won';
                if (key === 'gamesWon') return 'Games won';
                if (key === 'yourProfile') return 'Your profile';
                if (key === 'alreadyFriends') return 'Already friends';
                if (key === 'addFriend') return 'Add friend';
                if (key === 'friendList') return 'Friend list';
                if (key === 'noFriends') return 'No friends';
                if (key === 'achievements') return 'Achievements';
                if (key === 'noAchievements') return 'No achievements';
                break;
            case 'NL':
                if (key === 'loading') return 'Laden...';
                if (key === 'error') return 'Fout';
                if (key === 'notAvailable') return 'Speler niet beschikbaar.';
                if (key === 'profileEnd') return '\'s Profiel';
                if (key === 'friends') return 'Vrienden';
                if (key === 'achievements') return 'Prestaties';
                if (key === 'roundsWon') return 'Rondes gewonnen';
                if (key === 'gamesWon') return 'Spellen gewonnen';
                if (key === 'yourProfile') return 'Jouw profiel';
                if (key === 'alreadyFriends') return 'Al vrienden';
                if (key === 'addFriend') return 'Voeg vriend toe';
                if (key === 'friendList') return 'Vriendenlijst';
                if (key === 'noFriends') return 'Geen vrienden';
                if (key === 'achievements') return 'Prestaties';
                if (key === 'noAchievements') return 'Geen prestaties';
                break;
            case 'FR':
                if (key === 'loading') return 'Chargement...';
                if (key === 'error') return 'Erreur';
                if (key === 'notAvailable') return 'Joueur non disponible.';
                if (key === 'profileEnd') return '\'s Profil';
                if (key === 'friends') return 'Amis';
                if (key === 'achievements') return 'Réalisations';
                if (key === 'roundsWon') return 'Rondes gagnées';
                if (key === 'gamesWon') return 'Jeux gagnés';
                if (key === 'yourProfile') return 'Votre profil';
                if (key === 'alreadyFriends') return 'Déjà amis';
                if (key === 'addFriend') return 'Ajouter un ami';
                if (key === 'friendList') return 'Liste d\'amis';
                if (key === 'noFriends') return 'Pas d\'amis';
                if (key === 'achievements') return 'Réalisations';
                if (key === 'noAchievements') return 'Pas de réalisations';
                break;
            default:
                if (key === 'loading') return 'Loading...';
                if (key === 'error') return 'Error';
                if (key === 'notAvailable') return 'Player not available.';
                if (key === 'profileEnd') return "'s Profile";
                if (key === 'friends') return 'Friends';
                if (key === 'achievements') return 'Achievements';
                if (key === 'roundsWon') return 'Rounds won';
                if (key === 'gamesWon') return 'Games won';
                if (key === 'yourProfile') return 'Your profile';
                if (key === 'alreadyFriends') return 'Already friends';
                if (key === 'addFriend') return 'Add friend';
                if (key === 'friendList') return 'Friend list';
                if (key === 'noFriends') return 'No friends';
                if (key === 'achievements') return 'Achievements';
                if (key === 'noAchievements') return 'No achievements';
                break;
        }
        return key;
    };

    const handleAddFriend = (friendUsername: string) => {
        addFriendByNickname(friendUsername, {
            onSuccess: () => alert(`Friend request sent to ${friendUsername}!`),
            onError: (error: any) => alert(`Failed to send friend request: ${error.message}`),
        });
    };

    if (isLoading) {
        return <div className="flex justify-center items-center h-screen bg-gray-100">{getTranslatedText("loading")}</div>;
    }

    if (isError) {
        return <div className="flex justify-center items-center h-screen bg-gray-100">{getTranslatedText("error")}</div>;
    }

    if (!playerProfile) {
        return <div className="flex justify-center items-center h-screen bg-gray-100">{getTranslatedText("notAvailable")}</div>;
    }

    return (
        <div className="flex justify-center items-center min-h-screen p-7">
            <div className="w-full sm:max-w-[400px] lg:max-w-[500px] xl:max-w-[600px] bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative">
                {/* Avatar */}
                <div className="absolute right-1/2 transform translate-x-1/2 rounded-full border-[5px] bg-blue-500 border-yellow z-20 w-[150px] h-[150px] flex justify-center items-center shadow-lg shadow-black">
                    {playerProfile.avatarUrl ? (
                        <img
                            className="rounded-full w-full h-full object-cover"
                            src={playerProfile.avatarUrl}
                            alt={`${playerProfile.username}'s avatar`}
                        />
                    ) : (
                        <div className="text-white font-bold text-xl">{playerProfile.username.charAt(0)}</div>
                    )}
                </div>

                {/* Info */}
                <div className="border-[5px] border-yellow rounded-2xl mt-[100px] p-3 pt-[50px]">
                    <h1 className="text-yellow text-4xl font-black text-center mb-4">
                        {playerProfile.username}{getTranslatedText("profileEnd")}
                    </h1>
                    <div className="w-full rounded-xl border-2 border-yellow p-2 flex items-center mb-2">
                        <div className="w-2/12">
                            <Star className="float-left" sx={{ width: '100%', height: '100%', color: '#fcd34d' }} />
                        </div>
                        <div className="w-full text-white font-bold text-xl">{playerProfile.xp} XP</div>
                    </div>
                    <div className="w-full rounded-xl border-2 border-yellow p-2 flex items-center">
                        <div className="w-2/12">
                            <CurrencyIcon currency={currency} />
                        </div>
                        <div className="w-full text-white font-bold text-xl">{playerProfile.balance}</div>
                    </div>
                </div>

                {/* Extra info */}
                <div className="border-[5px] border-yellow rounded-2xl mt-2 p-5 grid grid-cols-1 gap-4">
                    {/* Vrienden */}
                    <div
                        className="w-full rounded-xl border-2 border-yellow p-2 cursor-pointer hover:bg-yellow hover:text-blue-500 transition"
                        onClick={() => setShowFriends(true)}
                    >
                        <div className="text-white font-bold text-xl">
                            {getTranslatedText("friends")}: {playerProfile.friendNames.length}
                        </div>
                    </div>

                    {/* Achievements */}
                    <div
                        className="w-full rounded-xl border-2 border-yellow p-2 cursor-pointer hover:bg-yellow hover:text-blue-500 transition"
                        onClick={() => setShowAchievements(true)}
                    >
                        <div className="text-white font-bold text-xl">
                            {getTranslatedText("achievements")}: {playerProfile.achievementNames.length}
                        </div>
                    </div>

                    <div className="w-full rounded-xl border-2 border-yellow p-2">
                        <div className="text-white font-bold text-xl">{getTranslatedText("roundsWon")}: {playerProfile.roundsWon}</div>
                    </div>
                    <div className="w-full rounded-xl border-2 border-yellow p-2">
                        <div className="text-white font-bold text-xl">{getTranslatedText("gamesWon")}: {playerProfile.gamesWon}</div>
                    </div>
                </div>

                {/* Toevoegen */}
                <div className="mt-4 flex justify-center">
                    {currentPlayer?.name === playerProfile.username ? (
                        <div className="bg-blue-700 text-white font-bold py-2 px-4 rounded-xl shadow-lg">
                            {getTranslatedText("yourProfile")}
                        </div>
                    ) : playerProfile.alreadyFriends ? (
                        <div className="bg-green-500 text-white font-bold py-2 px-4 rounded-xl shadow-lg">
                            {getTranslatedText("alreadyFriends")}
                        </div>
                    ) : (
                        <button
                            className="bg-yellow text-blue-500 font-bold py-2 px-4 rounded-xl shadow-lg hover:opacity-80"
                            onClick={() => handleAddFriend(username!)}
                        >
                            {getTranslatedText("addFriend")}
                        </button>
                    )}
                </div>

                {/* Vriendenlijst */}
                {showFriends && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                        <div className="bg-blue-500 w-[90%] max-w-[600px] rounded-lg p-5 shadow-lg relative">
                            <h2 className="text-yellow text-2xl font-bold mb-4">{getTranslatedText("friendList")}</h2>
                            <button
                                className="absolute top-4 right-4 text-white text-2xl font-bold"
                                onClick={() => setShowFriends(false)}
                            >
                                &times;
                            </button>
                            {playerProfile.friendNames.length > 0 ? (
                                <div className="space-y-4">
                                    {playerProfile.friendNames.map((friendName: string) => (
                                        <div
                                            key={friendName}
                                            className="bg-yellow p-3 rounded-lg text-blue-500 font-bold cursor-pointer"
                                            onClick={() => window.open(`/profile/${friendName}`, "_blank")}
                                        >
                                            {friendName}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-white">{getTranslatedText("noFriends")}</div>
                            )}
                        </div>
                    </div>
                )}

                {/* Achievements lijst */}
                {showAchievements && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                        <div className="bg-blue-500 w-[90%] max-w-[600px] rounded-lg p-5 shadow-lg relative">
                            <h2 className="text-yellow text-2xl font-bold mb-4">{getTranslatedText("achievements")}</h2>
                            <button
                                className="absolute top-4 right-4 text-white text-2xl font-bold"
                                onClick={() => setShowAchievements(false)}
                            >
                                &times;
                            </button>
                            {playerProfile.achievementNames.length > 0 ? (
                                <div className="space-y-4">
                                    {playerProfile.achievementNames.map((achievementName: string) => (
                                        <div
                                            key={achievementName}
                                            className="bg-yellow p-3 rounded-lg text-blue-500 font-bold"
                                        >
                                            {achievementName}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-white">{getTranslatedText("noAchievements")}</div>
                            )}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default PlayerProfile;
