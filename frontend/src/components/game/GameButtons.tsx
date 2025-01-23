import {Slider} from "@mui/material";
import {GameResponse} from "../../model/GameResponse.ts";
import {Player} from "../../model/Player.ts";
import {useActions} from "../../hooks/useActions.ts";
import {useState, useEffect} from "react";

interface GameButtonsProps {
    game: GameResponse,
    currentPlayer: Player | undefined,
    currentTotalBet: number,
    getTranslatedText: (key: string) => string,
}

export function GameButtons({game, currentPlayer, currentTotalBet, getTranslatedText}: GameButtonsProps) {
    const {newPlayerAction} = useActions();
    const [raiseAmount, setRaiseAmount] = useState(0);
    const [showRaiseSlider, setShowRaiseSlider] = useState(false);
    const [isTimerComplete] = useState(false);
    const currentPlayerWithCards = game?.players.find(p => p.name == currentPlayer?.name);


    const handleAction = (actionType: string, raiseAmount: number = 0) => {
        setShowRaiseSlider(false);
        if (!game?.currentRound?.id) {
            return;
        }
        newPlayerAction(
            {
                roundId: game.currentRound.id,
                actionType: actionType.toUpperCase(),
                raiseAmount,
            },
            {
                onSuccess: () => {
                    setRaiseAmount(0);
                },
                onError: (error) => {
                    console.error(`${actionType} action failed:`, error);
                },
            }
        );
    };

    useEffect(() => {
        console.log('useEffect');
        if (isTimerComplete) {
            console.log('timer complete');
            const lastBettingRound = game?.currentRound?.bettingRounds?.[game?.currentRound?.bettingRounds?.length - 1] || null;
            const currentHighestBet = lastBettingRound?.currentHighestBet || 0;
            const canCheck = currentTotalBet === currentHighestBet;

            if (canCheck) {
                console.log('trying to check');
                handleAction("check", 0);
            } else {
                console.log('trying to fold');
                handleAction("fold", 0);
            }
        }
    }, [isTimerComplete]);

    return (
        <>
            <div className={"w-full grid grid-cols-4 gap-5 relative"}>
                {(() => {
                    const lastBettingRound = game?.currentRound?.bettingRounds?.[game?.currentRound?.bettingRounds?.length - 1] || null;
                    const currentHighestBet = lastBettingRound?.currentHighestBet || 0;
                    const playerBalance = currentPlayerWithCards?.balance || 0;

                    const canCheck = currentTotalBet === currentHighestBet;
                    const canCall = currentTotalBet < currentHighestBet;
                    const canRaise = playerBalance > currentHighestBet;
                    const canFold = currentHighestBet !== 0;

                    return (
                        <>
                            {/* Check button */}
                            {canCheck && (
                                <div className={"w-full h-full col-span-2"}>
                                    <button
                                        className={"w-full h-full bg-green-500 text-white font-bold p-2 mx-auto rounded-xl hover:opacity-70"}
                                        onClick={() => {
                                            handleAction("check", 0);
                                        }}>
                                        {getTranslatedText("check")}
                                    </button>
                                </div>
                            )}

                            {/* Call button */}
                            {canCall && (
                                <div className={"w-full h-full col-span-2"}>
                                    <button
                                        className={"w-full h-full bg-green-500 text-white font-bold p-2 mx-auto rounded-xl hover:opacity-70"}
                                        onClick={() => {
                                            handleAction("call", 0);
                                        }}>
                                        {getTranslatedText("call")}
                                    </button>
                                </div>
                            )}

                            {/* Raise button */}
                            {canRaise && (
                                <>
                                    {showRaiseSlider &&
                                        <div
                                            className={"bg-gray-100 absolute w-full rounded-lg bottom-0 p-3 h-full grid z-50"}>
                                            <div
                                                className={"row-span-2 flex justify-center items-center"}>
                                                <Slider
                                                    value={raiseAmount}
                                                    onChange={(_, newValue) => setRaiseAmount(newValue as number)}
                                                    min={currentHighestBet}
                                                    max={playerBalance + currentTotalBet || currentTotalBet}
                                                    step={25}
                                                    valueLabelDisplay="auto"
                                                    sx={{
                                                        '& .MuiSlider-thumb': {
                                                            width: 30,
                                                            height: 30
                                                        },
                                                        '& .MuiSlider-track': {height: 10},
                                                        '& .MuiSlider-rail': {height: 10}
                                                    }}
                                                />
                                            </div>

                                            <div
                                                className={"h-full w-full grid grid-cols-2 gap-9 bottom-0"}>
                                                <button
                                                    className={"w-full h-full bg-red text-white font-bold p-2 shadow-black shadow-md mx-auto rounded-xl hover:opacity-70"}
                                                    onClick={() => {
                                                        setShowRaiseSlider(false);
                                                    }}>
                                                    Cancel
                                                </button>
                                                <button
                                                    className={"w-full h-full bg-green-500 text-white font-bold p-2 shadow-black shadow-md mx-auto rounded-xl hover:opacity-70"}
                                                    onClick={() => {
                                                        handleAction("raise", raiseAmount);
                                                    }}>
                                                    {getTranslatedText("raise")} {raiseAmount}
                                                </button>
                                            </div>
                                        </div>
                                    }

                                    <div className={"w-full h-full col-span-2"}>
                                        <button
                                            className={"w-full h-full bg-green-500 text-white font-bold p-2 mx-auto rounded-xl hover:opacity-70"}
                                            onClick={() => {
                                                setShowRaiseSlider(!showRaiseSlider);
                                            }}>
                                            {getTranslatedText("raise")}
                                        </button>
                                    </div>
                                </>
                            )}

                            {/* Fold button */}
                            {canFold && (
                                <div className={"col-span-4 w-full h-full"}>
                                    <button
                                        className={"w-full h-full bg-red text-white font-bold p-2 mx-auto rounded-xl hover:opacity-70"}
                                        onClick={() => {
                                            handleAction("fold", 0);
                                        }}>
                                        {getTranslatedText("fold")}
                                    </button>
                                </div>
                            )}
                        </>
                    );
                })()}
                {/* Progress bar so that it automatically does action when time is up */}


            </div>
        </>
    );
}
