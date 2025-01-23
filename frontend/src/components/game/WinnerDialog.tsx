import {PlayerInGame} from "../../model/PlayerInGame.ts";
import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";
import {Player} from "../../model/Player.ts";
import {playSound} from "../../utils/SoundUtils.ts";

interface IWinnerDialog {
    winners: PlayerInGame[];
    currentPlayers: Player | undefined;
}

export function WinnerDialog({winners, currentPlayers}: IWinnerDialog) {
    const {soundLevel, language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'win') return 'wins!';
                break;
            case 'NL':
                if (key === 'win') return 'wint!';
                break;
            case 'FR':
                if (key === 'win') return 'gagne!';
                break;
            default:
                if (key === 'win') return 'wins!';
                break;
        }
        return key;
    }


    if (currentPlayers?.id !== winners[0]?.id) {
        playSound("https://storage.googleapis.com/image_bucket_ip2/sounds/winning-sound.wav",soundLevel)
    }

    return (
        <div className={"fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 z-50"}>
            <div className={"absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white p-4 rounded-lg"}>
                <h1 className={"text-2xl font-bold text-center"}>{winners[0]?.name} {getTranslatedText('win')}</h1>
            </div>
        </div>
    );
}
