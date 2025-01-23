import {usePlayerRegistration} from "../../hooks/usePlayerRegistration.ts";
import {Loading} from "./Loading.tsx";
import {useNavigate} from "react-router-dom";
import Account from "./Account.tsx";
import {useContext} from "react";
import CurrentPlayerContext, {ICurrentPlayerContext} from "../../context/player/CurrentPlayerContext.ts";
import {playSound} from "../../utils/SoundUtils.ts";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";


function PokerGame() {
    const navigate = useNavigate();
    const {loading, error} = usePlayerRegistration();
    const {currentPlayer} = useContext<ICurrentPlayerContext>(CurrentPlayerContext);
    const {soundLevel, theme} = useContext<ISettingsContext>(SettingsContext);


    if (loading) return <Loading/>
    if (error) return <div>{error}</div>;
    return (<>
            <div className={"grid grid-cols-3 gap-10 content-center p-10 pt-28 h-screen"}>
                {currentPlayer ?
                    <Account currentPlayer={currentPlayer}/>
                    :
                    <div className={"w-full h-full bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative"}>
                        ERROR
                    </div>
                }

                <div className={"w-full h-full shadow-lg shadow-black rounded-3xl relative bg-cover bg-center hover:cursor-pointer hover:scale-110 hover:animate-pulse"}
                     {...(theme === "Winter" ? {style: {backgroundImage: "url('https://storage.googleapis.com/image_bucket_ip2/general/winter-find-game.png')"}} : {style: {backgroundImage: "url('https://storage.googleapis.com/image_bucket_ip2/general/find-game.png')"}})}
                     onClick={() => {
                         playSound("https://storage.googleapis.com/image_bucket_ip2/sounds/click.wav", soundLevel)
                         navigate(`/find`)
                     }}>
                </div>
                <div className={"w-full h-full shadow-lg shadow-black rounded-3xl relative bg-cover bg-center hover:cursor-pointer hover:scale-110 hover:animate-pulse"}
                     {...(theme === "Winter" ? {style: {backgroundImage: "url('https://storage.googleapis.com/image_bucket_ip2/general/winter-host-game.png')"}} : {style: {backgroundImage: "url('https://storage.googleapis.com/image_bucket_ip2/general/host-game.png')"}})}
                     onClick={() => {
                         playSound("https://storage.googleapis.com/image_bucket_ip2/sounds/click.wav", soundLevel)
                         navigate(`/host`)
                     }}>
                </div>
            </div>
        </>
    )
}

export default PokerGame