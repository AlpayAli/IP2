import Account from "../general/Account.tsx";
import {useContext, useEffect, useState} from "react";
import CurrentPlayerContext, { ICurrentPlayerContext } from "../../context/player/CurrentPlayerContext.ts";
import { useLeaderboard } from "../../hooks/useLeaderboard.ts";
import { useNavigate } from "react-router-dom";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function Leaderboard() {
    const { currentPlayer } = useContext<ICurrentPlayerContext>(CurrentPlayerContext);
    const { leaderboardByXp, leaderboardByBalance } = useLeaderboard();
    const [activeLeaderboard, setActiveLeaderboard] = useState([...leaderboardByBalance]);
    const [sortKey, setSortKey] = useState<"balance" | "xp" | null>(null);
    const [sortOrder, setSortOrder] = useState<"asc" | "desc">("desc");
    const navigate = useNavigate();
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'leaderboard') return 'Leaderboard';
                if (key === 'username') return 'Username';
                if (key === 'balance') return 'Balance';
                if (key === 'xp') return 'Xp';
                if (key === 'back') return 'Back';
                break;
            case 'NL':
                if (key === 'leaderboard') return 'Klassement';
                if (key === 'username') return 'Gebruikersnaam';
                if (key === 'balance') return 'Balans';
                if (key === 'xp') return 'Xp';
                if (key === 'back') return 'Terug';
                break;
            case 'FR':
                if (key === 'leaderboard') return 'Classement';
                if (key === 'username') return 'Nom d\'utilisateur';
                if (key === 'balance') return 'Solde';
                if (key === 'xp') return 'Xp';
                if (key === 'back') return 'Retour';
                break;
            default:
                if (key === 'leaderboard') return 'Leaderboard';
                if (key === 'username') return 'Username';
                if (key === 'balance') return 'Balance';
                if (key === 'xp') return 'Xp';
                if (key === 'back') return 'Back';
                break;
        }
        return '';
    };

    useEffect(() => {
        setActiveLeaderboard([...leaderboardByBalance]);
    }, [leaderboardByBalance]);

    const handleSort = (key: "balance" | "xp") => {
        let newSortOrder: "asc" | "desc" = "desc";

        if (sortKey === key) {
            newSortOrder = sortOrder === "asc" ? "desc" : "asc";
        }

        setSortKey(key);
        setSortOrder(newSortOrder);

        const sortedLeaderboard = [...(key === "balance" ? leaderboardByBalance : leaderboardByXp)].sort((a, b) =>
            newSortOrder === "asc" ? a[key] - b[key] : b[key] - a[key]
        );

        setActiveLeaderboard(sortedLeaderboard);
    };

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
                    <h1 className={"text-yellow font-bold text-4xl mb-7"}>{getTranslatedText('leaderboard')}</h1>
                    <div className="space-y-10 h-[400px] overflow-y-auto no-scrollbar">
                        <table className="table-auto w-full">
                            <thead className={"sticky top-0 bg-blue-500 border-b-2 border-yellow"}>
                            <tr className="text-left text-yellow-200 ">
                                <th className="py-2 px-4 text-yellow">#</th>
                                <th className="py-2 px-4 text-yellow">{getTranslatedText('username')}</th>
                                <th
                                    onClick={() => handleSort("balance")}
                                    className="py-2 px-4 text-yellow cursor-pointer"
                                >
                                    {getTranslatedText('balance')}
                                </th>
                                <th
                                    onClick={() => handleSort("xp")}
                                    className="py-2 px-4 text-yellow cursor-pointer"
                                >
                                    {getTranslatedText('xp')}
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            {activeLeaderboard.map((player, index) => (
                                <tr
                                    key={player.id}
                                    className={`border-b-2 border-yellow ${currentPlayer?.id === player.id ? "bg-yellow text-blue-500 bottom-0 top-10 sticky" : "bg-blue-500 text-yellow"}`}
                                >
                                    <td className={`py-2 px-4`}>{index + 1}</td>
                                    <td
                                        className="py-2 px-4 cursor-pointer"
                                        onClick={() => window.open(`/profile/${player.name}`, "_blank")}
                                    >
                                        {player.name}
                                    </td>
                                    <td className={`py-2 px-4`}>{player.balance}</td>
                                    <td className={`py-2 px-4`}>{player.xp}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
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
