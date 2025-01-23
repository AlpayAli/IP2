import {Gimmick} from "../../model/Gimmick.ts";
import {useContext} from "react";
import CurrentPlayerContext from "../../context/player/CurrentPlayerContext.ts";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function GimmickListItem({gimmick}: { gimmick: Gimmick }) {
    const {setSelectedBackground} = useContext(CurrentPlayerContext);
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'use') return 'Use';
                break;
            case 'NL':
                if (key === 'use') return 'Gebruik';
                break;
            case 'FR':
                if (key === 'use') return 'Utiliser';
                break;
            default:
                if (key === 'use') return 'Use';
                break;
        }
        return ''
    }

    const handleSetBackground = () => {
        setSelectedBackground(gimmick.imageUrl); // Update the context
        alert(`Selected ${gimmick.name} as your table background!`);
    };

    return (
        <div className="flex justify-between items-center bg-yellow p-4 rounded-lg shadow-sm">
        <img src={gimmick.imageUrl} alt={gimmick.name} className="w-12 h-12 object-cover rounded-full mr-4"/>
    <span className="text-lg font-medium text-gray-800">{gimmick.name}</span>
        <button
    className="ml-4 bg-green-500 text-white px-3 py-1 rounded"
    onClick={handleSetBackground}
        >
        {getTranslatedText('use')}
    </button>
    </div>
);
}