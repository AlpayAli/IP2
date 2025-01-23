import {useNavigate} from "react-router-dom";
import {useCreateGame} from "../../hooks/useGames.ts";
import {HostGameForm} from "./HostGameForm.tsx";
import {useContext, useEffect} from "react";
import Account from "../general/Account.tsx";
import CurrentPlayerContext, {ICurrentPlayerContext} from "../../context/player/CurrentPlayerContext.ts";

export function HostGame() {
    const navigate = useNavigate()
    const {addGame, newGame, isGameAdded} = useCreateGame()
    const {currentPlayer} = useContext<ICurrentPlayerContext>(CurrentPlayerContext);


    useEffect(() => {
        if (isGameAdded) {
            navigate(`/invite/${newGame?.id}`)
        }
    }, [isGameAdded]);

    return (
        <div className={"grid grid-cols-3 gap-10 content-center p-10 pt-28 h-screen"}>
            {currentPlayer ?
                <Account currentPlayer={currentPlayer}/>
                :
                <div className={"w-full h-full bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative"}>
                    ERROR
                </div>
            }

            <div className={"col-span-2 bg-blue-500 rounded-2xl shadow-lg shadow-black p-10 relative"}>

                <HostGameForm onSubmit={(game) => {
                    addGame({...game})
                }}/>
            </div>

        </div>
    )
}