import {CurrencyIcon} from "../general/CurrencyIcon.tsx";
import {formatCardUrl} from "../../utils/CardMapper.ts";
import {ChatDialog} from "./ChatDialog.tsx";
import {Player} from "../../model/Player.ts";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {useLeaveGame} from "../../hooks/useJoinGame.ts";
import {GameResponse} from "../../model/GameResponse.ts";
import {GameButtons} from "./GameButtons.tsx";

interface GameFooterProps {
    currentPlayer: Player | undefined,
    getTranslatedText: (key: string) => string,
    refetch: () => void,
    game: GameResponse,
    currency: string,
}

export function GameFooter({currentPlayer, getTranslatedText, refetch, game, currency }: GameFooterProps) {
    const navigate = useNavigate();
    const toggleChat = () => setIsChatOpen((prev) => !prev);
    const {endGame} = useLeaveGame(() => {refetch();});
    const [currentTotalBet, setCurrentTotalBet] = useState(0);
    const [isChatOpen, setIsChatOpen] = useState(false);


    useEffect(() => {
        if (!game || !currentPlayer) return;

        const lastBettingRound = game?.currentRound?.bettingRounds?.[game?.currentRound?.bettingRounds?.length - 1];

        if (!lastBettingRound) return;

        const totalBet = lastBettingRound?.playerActionDtoList
            ?.filter((action) => action.playerName === currentPlayer?.name)
            ?.reduce((sum, action) => sum + action.amount, 0) || 0;

        setCurrentTotalBet(totalBet);

    }, [game, currentPlayer]);

    const currentPlayerWithCards = game?.players.find(p => p.name == currentPlayer?.name);

    return (
        <div className={"row-start-10 row-span-4 grid grid-cols-10 gap-5"}>
            {/*leave button*/}
            <div className={"col-span-1"}>
                <div className={"w-full h-full relative mx-5 z-50"}>
                    <button
                        className={"absolute bottom-5 bg-red text-white font-bold p-2 shadow-black shadow-lg mx-auto w-full rounded-xl hover:opacity-70"}
                        onClick={() => {
                            endGame(game.id)
                            navigate(`/find`)
                        }}>
                        {getTranslatedText("leave_game")}
                    </button>
                </div>
            </div>
            {/*playerinformation*/}
            <div className={"col-span-3 relative p-1"}>
                <div className={"absolute bottom-5 mx-auto flex float-left w-[400px]"}>
                    {currentPlayer?.avatarUrl ?
                        <img alt={"avatar"}
                             className={"rounded-full border-[12px] border-blue-500 z-20 w-[150px]"}
                             src={currentPlayer?.avatarUrl}/>
                        :
                        <div
                            className={"rounded-full border-[12px] bg-blue-500 border-blue-500 z-20 w-[150px] h-[150px] flex items-center justify-center"}>
                            <div className={"text-white font-bold "}>
                                {currentPlayer?.name}
                            </div>
                        </div>
                    }
                    <div
                        className={"text-white relative -left-[50px] pl-[50px] bg-blue-500 h-[100px] z-10 my-auto w-full rounded-r-full"}>

                        <div className={"text-center font-bold"}>{currentPlayer?.name}</div>

                        <div className={"w-full h-1/3 flex"}>
                            <div className={"h-full "}>
                                <CurrencyIcon currency={currency}/>
                            </div>
                            <div className={" w-full text-white font-bold text-xl content-center"}>
                                {currentPlayerWithCards?.balance}
                            </div>
                        </div>

                        <div className={"w-full h-1/2 grid grid-cols-2"}>
                            <div> {currentPlayer?.xp} XP</div>
                        </div>
                    </div>
                </div>
            </div>
            {/*playbuttons*/}
            <div className={"col-span-3 flex justify-center p-10"}>
                {/* Alleen tonen als de huidige speler de actieve speler is */}
                {game?.currentRound?.bettingRounds[game?.currentRound?.bettingRounds?.length - 1].activePlayer.id === currentPlayer?.id && (
                    <GameButtons
                        game={game}
                        currentPlayer={currentPlayer}
                        currentTotalBet={currentTotalBet}
                        getTranslatedText={getTranslatedText}/>
                )}

            </div>
            {/*cards*/}
            <div className={"col-span-3 p-1 grid grid-cols-2"}>
                {currentPlayerWithCards && currentPlayerWithCards.currentCards && currentPlayerWithCards.currentCards?.length != 0 &&
                    <>
                        <div
                            className={`h-full w-full bg-contain bg-no-repeat bg-right`}
                            style={{backgroundImage: `url(${formatCardUrl(currentPlayerWithCards.currentCards[0])})`}}>
                        </div>
                        <div
                            className={`h-full w-full bg-contain bg-no-repeat bg-left`}
                            style={{backgroundImage: `url(${formatCardUrl(currentPlayerWithCards.currentCards[1])})`}}>
                        </div>
                    </>

                }
            </div>

            <div className="absolute bottom-4 right-4">
                <button className={"bg-blue-500 text-white p-2 rounded-lg"}
                    onClick={toggleChat}
                >
                    Chat
                </button>
            </div>
            <ChatDialog gameId={game.id!} isOpen={isChatOpen} onClose={toggleChat}/>
        </div>
    )
}