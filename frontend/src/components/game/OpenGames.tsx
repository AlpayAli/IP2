import { useState, useContext } from "react";
import { useOpenGames } from "../../hooks/useOpenGames";
import { useJoinGame } from "../../hooks/useJoinGame";
import { useNavigate } from "react-router-dom";
import { Loading } from "../general/Loading";
import Account from "../general/Account.tsx";
import { BackButton } from "../general/BackButton.tsx";
import CurrentPlayerContext, { ICurrentPlayerContext } from "../../context/player/CurrentPlayerContext.ts";
import SettingsContext, { ISettingsContext } from "../../context/settings/SettingsContext.ts";
import {useActiveGames} from "../../hooks/UseActiveGames.ts";
import {Player} from "../../model/Player.ts";

export function OpenGames() {
    const [view, setView] = useState("open"); // Toggle between "open" and "active"
    const { isLoading, isError, data: openGames } = useOpenGames();
    const { isLoading: activeLoading, isError: activeError, data: activeGames } = useActiveGames(); // Replace with useActiveGames if available
    const { currentPlayer } = useContext<ICurrentPlayerContext>(CurrentPlayerContext);
    const { participateGame } = useJoinGame();
    const navigate = useNavigate();
    const { language } = useContext<ISettingsContext>(SettingsContext);
    console.log(activeGames)

    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'smallBlind') return 'Small Blind';
                if (key === 'minPlayers') return 'Min Players';
                if (key === 'maxPlayers') return 'Max Players';
                if (key === 'players') return 'Players';
                if (key === 'averageXP') return 'Average XP of Players';
                if (key === 'joinGame') return 'Join Game';
                if (key === 'noGames') return 'There are currently no games available.';
                if (key === 'openGames') return 'Open Games';
                if (key === 'activeGames') return 'Active Games';
                break;
            case 'NL':
                if (key === 'smallBlind') return 'Small Blind';
                if (key === 'minPlayers') return 'Min aantal spelers';
                if (key === 'maxPlayers') return 'Max aantal spelers';
                if (key === 'players') return 'Spelers';
                if (key === 'averageXP') return 'Gemiddelde XP van spelers';
                if (key === 'joinGame') return 'Meedoen aan spel';
                if (key === 'noGames') return 'Er zijn momenteel geen spellen beschikbaar.';
                if (key === 'openGames') return 'Open Spellen';
                if (key === 'activeGames') return 'Actieve Spellen';
                break;
            case 'FR':
                if (key === 'smallBlind') return 'Small Blind';
                if (key === 'minPlayers') return 'Min joueurs';
                if (key === 'maxPlayers') return 'Max joueurs';
                if (key === 'players') return 'Joueurs';
                if (key === 'averageXP') return 'XP Moyenne des Joueurs';
                if (key === 'joinGame') return 'Rejoindre le jeu';
                if (key === 'noGames') return 'Il n\'y a actuellement aucun jeu disponible.';
                if (key === 'openGames') return 'Jeux Ouverts';
                if (key === 'activeGames') return 'Jeux Actifs';
                break;
            default:
                if (key === 'smallBlind') return 'Small Blind';
                if (key === 'minPlayers') return 'Min Players';
                if (key === 'maxPlayers') return 'Max Players';
                if (key === 'players') return 'Players';
                if (key === 'averageXP') return 'Average XP of Players';
                if (key === 'joinGame') return 'Join Game';
                if (key === 'noGames') return 'There are currently no games available.';
                if (key === 'openGames') return 'Open Games';
                if (key === 'activeGames') return 'Active Games';
        }
        return key;
    };

    if (isLoading || activeLoading) {
        return <Loading />;
    }

    if (isError || activeError) {
        return <p className="text-red">Error loading games</p>;
    }

    const games = view === "open" ? openGames : activeGames;

    if (!games || games.length === 0) {
        return <p className="text-gray-500">{getTranslatedText("noGames")}</p>;
    }

    const calculateAverageXP = (players : Player[]) => {
        if (!players || players.length === 0) return 0;
        const totalXP = players.reduce((sum, player) => sum + (player.xp || 0), 0);
        return Math.floor(totalXP / players.length);
    };


    return (
        <div className="grid grid-cols-3 gap-10 content-center p-10 pt-28 h-screen">
            {currentPlayer ? (
                <Account currentPlayer={currentPlayer} />
            ) : (
                <div className="w-full h-full bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative">
                    ERROR
                </div>
            )}
            <div className="col-span-2">
                <div className="mb-4 flex justify-between">
                    <button
                        className={`p-2 rounded-lg ${view === "open" ? "bg-blue-500 text-white" : "bg-gray-200"}`}
                        onClick={() => setView("open")}
                    >
                        {getTranslatedText("openGames")}
                    </button>
                    <button
                        className={`p-2 rounded-lg ${view === "active" ? "bg-blue-500 text-white" : "bg-gray-200"}`}
                        onClick={() => setView("active")}
                    >
                        {getTranslatedText("activeGames")}
                    </button>
                </div>
                <div className="rounded-2xl h-[490px] relative overflow-y-scroll no-scrollbar mb-2">
                    <ul className="space-y-4">
                        {games.map((game) => {
                            const averageXP = calculateAverageXP(game.players as Player[]);
                            const playerCount = game.players?.length || 0;
                            return (
                                <li
                                    key={game.id}
                                    className="p-4 bg-blue-500 rounded-2xl shadow-md shadow-black"
                                >
                                    <p className="text-lg text-yellow font-medium">
                                        <strong>{getTranslatedText("smallBlind")}:</strong> {game.smallBlind}
                                    </p>
                                    <p className="text-white">
                                        <strong>{getTranslatedText("minPlayers")}:</strong> {game.minPlayers}
                                    </p>
                                    <p className="text-white">
                                        <strong>Buy-in Amount:</strong> {+game.smallBlind * 100}
                                    </p>
                                    <p className="text-white">
                                        <strong>{getTranslatedText("maxPlayers")}:</strong> {game.maxPlayers}
                                    </p>
                                    <p className="text-white">
                                        <strong>{getTranslatedText("players")}:</strong> {playerCount}
                                    </p>
                                    <p className="text-white">
                                        <strong>{getTranslatedText("averageXP")}:</strong> {averageXP}
                                    </p>
                                    <button
                                        className="mt-4 bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl w-1/3 hover:opacity-70"
                                        onClick={() => {
                                            participateGame(game.id);
                                            navigate(`/game/${game.id}`);
                                        }}
                                    >
                                        {getTranslatedText("joinGame")}
                                    </button>
                                </li>
                            );
                        })}
                    </ul>
                </div>
                <BackButton path="/" />
            </div>
        </div>
    );
}
