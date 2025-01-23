import {PlayerInGame} from "../../model/PlayerInGame.ts";
import {useNavigate} from "react-router-dom";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";
import {Player} from "../../model/Player.ts";
import {playSound} from "../../utils/SoundUtils.ts";
import {useContext} from "react";

interface IGameWinnerDialog {
    winner: PlayerInGame;
    currentPlayer: Player | undefined;
}

export function GameWinnerDialog({winner, currentPlayer}: IGameWinnerDialog) {
    const {soundLevel, language} = useContext<ISettingsContext>(SettingsContext);
    const navigate = useNavigate();

    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'win') return 'Wins the game!';
                if (key === 'back') return 'Returning to Home Screen...';
                break;
            case 'NL':
                if (key === 'win') return 'Wint het spel!';
                if (key === 'back') return 'Terug naar het hoofdscherm...';
                break;
            case 'FR':
                if (key === 'win') return 'Gagne le jeu!';
                if (key === 'back') return 'Retour à l\'écran d\'accueil...';
                break;
            default:
                if (key === 'win') return 'Wins the game!';
                if (key === 'back') return 'Returning to Home Screen...';
                break;
        }
        return key;
    };

    const handleReturn = () => {
        navigate('/');
    };

    if (currentPlayer?.name === winner?.name) {
        playSound("https://storage.googleapis.com/image_bucket_ip2/sounds/winning-sound.wav", soundLevel);
    }

    return (
        <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 z-50 flex items-center justify-center">
            <div className=" p-8 rounded-lg shadow-lg text-center flex flex-col items-center bg-blue-600">
                <h1 className="text-2xl text-white font-bold mb-4">{winner?.name} {getTranslatedText('win')}</h1>

                <button
                    onClick={handleReturn}
                    className="bg-blue-300 text-white px-6 py-3 rounded-lg hover:bg-white hover:text-black transition"
                >
                    {getTranslatedText('back')}
                </button>
            </div>
        </div>
    );
}
