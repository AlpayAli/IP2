import {useNavigate, useParams} from "react-router-dom";
import {useGame, useStartGame} from "../../hooks/useGames.ts";
import {useContext, useEffect, useState} from "react";
import {formatCardUrl} from "../../utils/CardMapper.ts";
import {TableCards} from "./TableCards.tsx";
import CurrentPlayerContext, {ICurrentPlayerContext} from "../../context/player/CurrentPlayerContext.ts";
import {PlayerInGame} from "../../model/PlayerInGame.ts";
import {WinnerDialog} from "./WinnerDialog.tsx";
import {useInitializeRound} from "../../hooks/useRounds.ts";
import {Snackbar, Alert} from "@mui/material";
import {useRecentAchievements} from "../../hooks/UseAchievements.ts";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";
import {CurrencyIcon} from "../general/CurrencyIcon.tsx";
import {GameFooter} from "./GameFooter.tsx";
import {useJoinGame} from "../../hooks/useJoinGame.ts";
import {GameWinnerDialog} from "./GameWinnerDialog.tsx";

export function Game() {
    const playerAndPositions: string[][] = [
        ["0", "xl:-left-10 xl:bottom-0 -left-1 bottom-0", "-right-16 bottom-12",],
        ["1", "xl:-left-10 xl:top-0 -left-12 top-14", "-right-16 -bottom-8"],
        ["2", "xl:left-52 xl:-top-20 left-28 -top-16", " -bottom-16 left-4"],
        ["3", "xl:left-1/3 xl:-top-20 left-1/3 -top-16", "left-4 -bottom-16"],
        ["4", "xl:right-1/3 xl:-top-20 right-1/3 -top-16", "left-4 -bottom-16"],
        ["5", "xl:right-52 xl:-top-20 right-28 -top-16", "left-4 -bottom-16"],
        ["6", "xl:-right-10 xl:top-0 -right-12 top-14", "-left-16 -bottom-8"],
        ["7", "xl:-right-10 xl:bottom-0 -right-1 bottom-0", "-left-16 bottom-12"],
        ["8", "xl:-right-10 xl:bottom-40 -right-1 bottom-0", "-left-16 bottom-12"],
    ];
    const navigate = useNavigate();
    const [showNotification, setShowNotification] = useState(false); // Nieuwe state voor notificatie
    const {id} = useParams()
    const {game, isLoadingGameById, isErrorLoadingGameById, refetch} = useGame(id!)
    const {currentPlayer, selectedBackground} = useContext<ICurrentPlayerContext>(CurrentPlayerContext);
    const [showWinnerDialog, setShowWinnerDialog] = useState(false);
    const [showGameWinnerDialog, setShowGameWinnerDialog] = useState(false)
    const [winners, setWinners] = useState<PlayerInGame[]>([]);
    const [gameWinner, setGameWinner] = useState<PlayerInGame>();
    const {startRound} = useInitializeRound(id!); // `id` is het gameId dat je al gebruikt
    const [enablePolling, setEnablePolling] = useState(false);
    const [showCards, setShowCards] = useState(false);
    const {currency, language} = useContext<ISettingsContext>(SettingsContext)
    const {participateGame} = useJoinGame();



    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'waiting_for_joining_players') return 'Waiting for players to join';
                if (key === 'waiting_for_host_to_start') return 'Waiting for host to start the game';
                if (key === 'leave_game') return 'Leave Game';
                if (key === 'raise') return 'Raise';
                if (key === 'fold') return 'Fold';
                if (key === 'check') return 'Check';
                if (key === 'call') return 'Call';
                if (key === 'invite') return 'Invite player';
                if (key === 'start_game') return 'Start Game';
                if (key === 'join_game') return 'Join Game';

                break;
            case 'NL':
                if (key === 'waiting_for_joining_players') return 'Wachten op spelers om deel te nemen';
                if (key === 'waiting_for_host_to_start') return 'Wachten op host om het spel te starten';
                if (key === 'leave_game') return 'Verlaat spel';
                if (key === 'raise') return 'Verhogen';
                if (key === 'fold') return 'Folderen';
                if (key === 'check') return 'Checken';
                if (key === 'call') return 'Callen';
                if (key === 'invite') return 'Speler uitnodigen';
                if (key === 'start_game') return 'Start spel';
                if (key === 'join_game') return 'Meedoen aan spel';

                break;
            case 'FR':
                if (key === 'waiting_for_joining_players') return 'En attente des joueurs pour rejoindre';
                if (key === 'waiting_for_host_to_start') return 'En attente de l\'hôte pour démarrer le jeu';
                if (key === 'leave_game') return 'Quitter le jeu';
                if (key === 'raise') return 'Augmenter';
                if (key === 'fold') return 'Plier';
                if (key === 'check') return 'Vérifier';
                if (key === 'call') return 'Appeler';
                if (key === 'invite') return 'Inviter joueur';
                if (key === 'start_game') return 'Démarrer le jeu';
                if (key === 'join_game') return 'Rejoindre le jeu';

                break;
            default:
                if (key === 'waiting_for_joining_players') return 'Waiting for players to join';
                if (key === 'waiting_for_host_to_start') return 'Waiting for host to start the game';
                if (key === 'leave_game') return 'Leave Game';
                if (key === 'raise') return 'Raise';
                if (key === 'fold') return 'Fold';
                if (key === 'check') return 'Check';
                if (key === 'call') return 'Call';
                if (key === 'invite') return 'Invite player';
                if (key === 'start_game') return 'Start Game';
                if (key === 'joinGame') return 'Join Game';

                break;
        }
        return key;
    }


    const {
        recentAchievements,
    } = useRecentAchievements(enablePolling);

    useEffect(() => {
        // Activeer polling als de huidige ronde is geëindigd
        if (game?.currentRound?.status.toLowerCase() === "ended") {
            setEnablePolling(true);
        } else {
            setEnablePolling(false);
        }
    }, [game?.currentRound?.status]);

    const handleCloseAchievementNotification = () => {
        setEnablePolling(false); // Stop polling wanneer notificaties worden gesloten
    };




    const {startGame} = useStartGame(id!);


    useEffect(() => {
        if (game?.currentRound?.status.toLowerCase() === "ended" && game?.status.toLowerCase() !== "ended") {
            setWinners(game.currentRound.winners || []);
            setShowWinnerDialog(true);
            setShowCards(true);

            // Zet een timer om de dialog te sluiten en een nieuwe ronde te starten
            const timer = setTimeout(() => {
                setShowWinnerDialog(false);
                setShowCards(false);
                startRound(); // Start een nieuwe ronde via de frontend
            }, 10000); // 5 seconden

            return () => clearTimeout(timer); // Opruimen van de timer bij unmount
        }
    }, [game?.currentRound?.status, startRound]);

    useEffect(() => {
        refetch();
    }, [id, refetch]);



    useEffect(() => {
        // Controleer of het de beurt is van de huidige speler
        if (
            game?.currentRound?.bettingRounds[game?.currentRound?.bettingRounds?.length - 1]
                .activePlayer.id === currentPlayer?.id
        ) {
            setShowNotification(true);
        } else {
            setShowNotification(false);
        }
    }, [game?.currentRound?.bettingRounds, currentPlayer?.id]);


    const handleCloseNotification = () => {
        setShowNotification(false);
    };

    useEffect(() => {
        if (game?.status.toLowerCase() === "ended") {
            setGameWinner(game.winner)
            setShowGameWinnerDialog(true)
            setShowWinnerDialog(false)
            setShowCards(false)
            setShowNotification(false)
        }
    }, [game?.winner]);


    if (isLoadingGameById) {
        return <div className={"flex justify-center content-center items-center"}>Loading...</div>;
    }

    if (isErrorLoadingGameById || !game) {
        return <div className={"flex justify-center content-center items-center"}>Error loading game or game data is
            incomplete...</div>;
    }

    return (
        <>
            {(game.players.length < 2) &&
                <div
                    className={"absolute bg-black bg-opacity-50 w-screen h-screen flex items-center justify-center z-50"}>
                    <div
                        className={"w-1/3 h-1/4 bg-white opacity-90 z-40 rounded-3xl flex items-center justify-center "}>
                        <div className={"text text-center"}>{getTranslatedText("waiting_for_joining_players")}</div>

                    </div>
                </div>
            }

            {(game?.status.toLowerCase() === "waiting_for_players" || false) &&
                <div
                    className={"absolute bg-black bg-opacity-50 w-screen h-screen flex items-center justify-center z-30"}>
                    <div
                        className={"w-1/3 h-1/4 bg-white opacity-90 z-40 rounded-3xl flex items-center justify-center "}>
                        <div className={"text text-center"}>{getTranslatedText("waiting_for_host_to_start")}</div>

                    </div>
                </div>
            }

            {((game?.status.toLowerCase() === "waiting_for_players" || false) && game?.host.id === currentPlayer?.id && game.players.length > 1) ? (
                <div className={"absolute w-[150px] h-[50px] z-50 right-1/2 top-2/3 transform translate-x-1/2"}>
                    <button
                        className={"w-full h-full bg-green-500 z-50 text-white font-bold p-2 shadow-lg rounded-xl hover:opacity-70"}
                        onClick={() => startGame()}
                    >
                        {getTranslatedText("start_game")}
                    </button>
                </div>
            ) : (
                !game.players.some(player => player.id === currentPlayer?.id) && (
                    <div className={"absolute w-[150px] h-[50px] z-50 right-1/2 top-2/3 transform translate-x-1/2"}>
                        <button
                            className={"w-full h-full bg-blue-500 z-50 text-white font-bold p-2 shadow-lg rounded-xl hover:opacity-70"}
                            onClick={() => participateGame(game.id)}
                        >
                            {getTranslatedText("join_game")}
                        </button>
                    </div>
                )
            )}

            {/* Snackbar Notificatie */}
            <Snackbar
                open={showNotification}
                autoHideDuration={3000} // Verwijnt na 3 seconden
                onClose={handleCloseNotification}
                anchorOrigin={{vertical: "top", horizontal: "center"}}
            >
                <Alert
                    onClose={handleCloseNotification}
                    severity="info"
                    sx={{width: "100%"}}
                >
                    Het is jouw beurt!
                </Alert>
            </Snackbar>

            {recentAchievements && recentAchievements?.length > 0 && (
                <Snackbar
                    open={enablePolling}
                    autoHideDuration={5000}
                    onClose={handleCloseAchievementNotification}
                    anchorOrigin={{vertical: "top", horizontal: "center"}}
                >
                    <Alert
                        onClose={handleCloseAchievementNotification}
                        severity="success"
                        sx={{width: "100%"}}
                    >
                        <strong>Achievement behaald!</strong>
                        <ul>
                            {recentAchievements.map((achievement) => (
                                <li key={achievement.achievementId}>{achievement.achievementName}</li>
                            ))}
                        </ul>
                    </Alert>
                </Snackbar>
            )}

            {showWinnerDialog && (
                <WinnerDialog winners={winners} currentPlayers={currentPlayer}/>
            )}

            {showGameWinnerDialog && (
                <GameWinnerDialog winner={gameWinner!} currentPlayer={currentPlayer}/>
            )}

            <div className={"h-screen grid grid-rows-12"}>
                <div
                    className={
                        "row-start-2 row-span-8 relative w-5/6 h-max my-auto p-0 xl:p-20 bg-no-repeat bg-cover mx-auto rounded-full border-[50px] border-[#800000] content-center"
                    }
                    style={{
                        backgroundImage: `url(${selectedBackground})`,
                    }}
                >

                    {game.players.map((player, index: number) => {
                        // Haal de laatste betting round veilig op, of gebruik null als er geen zijn
                        const lastBettingRound = game?.currentRound?.bettingRounds?.[game?.currentRound?.bettingRounds?.length - 1] || null;

                        // Controleer of er een lastBettingRound is, anders toon 0
                        const totalBetAmount = lastBettingRound
                            ? lastBettingRound.playerActionDtoList
                                .filter((action) => action.playerName === player.name)
                                .reduce((sum, action) => sum + action.amount, 0)
                            : 0;

                        return (
                            <div
                                className="text-center text-white font-bold w-full cursor-pointer hover:underline"
                                onClick={() => window.open(`/profile/${player.name}`, "_blank")}
                            >
                                <div
                                    key={player.id}
                                    className={`bg-blue-500 rounded-full absolute ${playerAndPositions[index][1]} w-[70px] h-[70px] xl:w-[100px] xl:h-[100px]`}
                                >
                                    {/* Spelerinformatie */}
                                    <div className="relative h-full w-full flex items-center">
                                        <div className="text-center text-white font-bold w-full">{player.name}</div>

                                        {showCards ?
                                            <>
                                                {/* Kaarten */}
                                                <div
                                                    className={`absolute w-[150px] left-[100px] ${playerAndPositions[index][2]} grid grid-cols-2 items-center`}>
                                                    {player.currentCards?.map((card, index) => (
                                                        <img
                                                            key={index}
                                                            src={formatCardUrl(card)}
                                                            alt={card}
                                                            className="mb-1"
                                                        />
                                                    ))}
                                                </div>
                                            </>
                                            :
                                            <>{/* Inzetinformatie */}
                                                <div
                                                    className={`absolute w-2/3 ${playerAndPositions[index][2]} flex flex-col items-center`}>
                                                    <img
                                                        src="https://storage.googleapis.com/image_bucket_ip2/general/chips.webp"
                                                        alt="chips"
                                                        className="mb-1"
                                                    />
                                                    <div
                                                        className="text-green-600 bg-white bg-opacity-50 text-center rounded-full px-2">
                                                        {totalBetAmount}
                                                    </div>
                                                </div>
                                            </>

                                        }
                                    </div>
                                </div>
                            </div>
                        );
                    })
                    }
                    {
                        game.players.length < 9 && (
                            <button
                                onClick={() => {
                                    navigate(`/invite/${game?.id}`)
                                }}
                                className={`bg-blue-500 rounded-full absolute ${playerAndPositions[8][1]} w-[70px] h-[70px] xl:w-[100px] xl:h-[100px]`}
                            >
                                {/* Spelerinformatie */}
                                <div className="relative h-full w-full flex items-center">
                                    <div
                                        className="text-center text-white font-bold w-full">{getTranslatedText("invite")}</div>
                                    {/* Inzetinformatie */}
                                </div>
                            </button>
                        )
                    }
                    <div
                        className={"border-4 border-opacity-50 border-white h-full w-full rounded-full relative p-7 flex justify-center"}>
                        <div
                            className={"absolute -top-16 left-1/2 transform -translate-x-1/2 border-4 sm:h-1/5 border-white border-opacity-50 bg-green-600 my-10 flex rounded-xl"}>
                            <div className={""}>
                                <CurrencyIcon currency={currency}/>
                            </div>
                            <div
                                className={" p-1 w-full text-center text-white font-bold text-2xl content-center"}>
                                {game?.currentRound?.pot}
                            </div>
                        </div>

                        <TableCards tableCards={game?.currentRound?.tableCards}/>
                    </div>
                </div>

                {/*gamefooter*/}
                <GameFooter
                    currentPlayer={currentPlayer}
                    getTranslatedText={getTranslatedText}
                    refetch={refetch}
                    game={game}
                    currency={currency}
                />
            </div>
        </>
    )
}