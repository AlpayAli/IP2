import {useAchievements} from "../../hooks/UseAchievements";
import {Achievement} from "../../model/Achievement";
import CurrentPlayerContext, {ICurrentPlayerContext} from "../../context/player/CurrentPlayerContext.ts";
import {useNavigate} from "react-router-dom";
import Account from "../general/Account.tsx";
import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function Achievements() {
    const { currentPlayer } = useContext<ICurrentPlayerContext>(CurrentPlayerContext);
    const navigate = useNavigate();
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'achievements') return 'Achievements';
                if (key === 'failedToLoadAchievements') return 'Failed to load achievements. Please try again later.';
                if (key === 'back') return 'Back';
                break;
            case 'NL':
                if (key === 'achievements') return 'Prestaties';
                if (key === 'failedToLoadAchievements') return 'Kan prestaties niet laden. Probeer het later opnieuw.';
                if (key === 'back') return 'Terug';
                break;
            case 'FR':
                if (key === 'achievements') return 'Réalisations';
                if (key === 'failedToLoadAchievements') return 'Impossible de charger les réalisations. Veuillez réessayer plus tard.';
                if (key === 'back') return 'Retour';
                break;
            default:
                if (key === 'achievements') return 'Achievements';
                if (key === 'failedToLoadAchievements') return 'Failed to load achievements. Please try again later.';
                if (key === 'back') return 'Back';
                break;
        }
        return key;
    }
    const {
        achievements,
        isLoadingAchievements,
        isErrorLoadingAchievements,
    } = useAchievements();

    if (isLoadingAchievements) {
        return (
            <div className="flex items-center justify-center h-screen">
                <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-500"></div>
            </div>
        );
    }

    if (isErrorLoadingAchievements) {
        return (
            <div className="flex items-center justify-center h-screen text-red-500">
                <p>{getTranslatedText('failedToLoadAchievements')}</p>
            </div>
        );
    }

    return (
        <>
            <div className={"grid grid-cols-3 gap-10 content-center p-10 pt-28 h-screen"}>
                {currentPlayer ? (
                    <Account currentPlayer={currentPlayer} />
                ) : (
                    <div className={"w-full h-full bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative"}>
                        ERROR
                    </div>
                )}
                <div className="w-full rounded-3xl shadow-lg p-6 col-span-2 bg-blue-500 relative">
                    <h1 className="text-4xl font-bold text-center mb-10 text-yellow">
                        {getTranslatedText('achievements')}
                    </h1>
                    <div
                        className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 h-[400px] overflow-y-auto no-scrollbar">
                        {achievements && achievements.map((achievement: Achievement) => (
                            <div
                                key={achievement.achievementId}
                                className={`relative p-6 border rounded-lg shadow-lg transition transform ${
                                    achievement.isUnlocked
                                        ? "bg-gradient-to-r from-green-400 to-yellow text-blue-500"
                                        : "bg-gradient-to-r from-blue-500 hover:to-blue-400 text-yellow"
                                }`}
                            >

                                <h2 className="text-xl font-semibold text-center">
                                    {achievement.achievementName}
                                </h2>
                                <p className="text-sm text-center mt-2">
                                    {achievement.achievementDescription}
                                </p>
                                <p className="text-lg font-bold text-center mt-4">
                                    XP: {achievement.achievementXp}
                                </p>
                            </div>
                        ))}
                    </div>
                    <div className="flex justify-start space-x-4 mt-8 absolute bottom-10">
                        <button
                            onClick={() => navigate("/")}
                            className="bg-red text-white py-2 px-4 rounded-lg font-semibold shadow-lg shadow-black hover:opacity-70 transition"
                        >
                            {getTranslatedText('back')}
                        </button>
                    </div>
                </div>
            </div>
        </>
    );
}
