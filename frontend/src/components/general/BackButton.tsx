import { useNavigate } from "react-router-dom";
import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function BackButton(props: { path: string | number }) {
    const navigate = useNavigate();
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'back') return 'Back';
                break;
            case 'NL':
                if (key === 'back') return 'Terug';
                break;
            case 'FR':
                if (key === 'back') return 'Retour';
                break;
            default:
                if (key === 'back') return 'Back';
                break;
        }
        return '';
    }

    const handleClick = () => {
        const path = typeof props.path === "number" ? String(props.path) : props.path;
        navigate(path);
    };

    return (
        <button
            className={"bg-red text-white font-bold p-2 shadow-black shadow-lg rounded-xl w-1/6 hover:opacity-70"}
            onClick={handleClick}
        >
            {getTranslatedText('back')}
        </button>
    );
}
