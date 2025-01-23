import {Button, Dialog, DialogActions, DialogContent, DialogTitle, MenuItem, Select, Slider} from "@mui/material";
import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";
import {playSound} from "../../utils/SoundUtils.ts";
import {ArrowDropDown} from "@mui/icons-material";
import Flag from "react-world-flags";

interface SettingsDialogProps {
    isOpen: boolean,
    onClose: () => void
}

export function SettingsDialog({isOpen, onClose}: SettingsDialogProps) {
    const {soundLevel, setSoundLevel, currency, setCurrency, language, setLanguage, theme, setTheme} = useContext<ISettingsContext>(SettingsContext)
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN': // English
                if (key === 'settings') return 'Settings';
                if (key === 'gamesound') return 'Gamesound';
                if (key === 'currency') return 'Currency';
                if (key === 'language') return 'Language';
                if (key === 'close') return 'Close';
                if (key === 'theme') return 'Theme';
                if (key === 'default') return 'Default';
                if (key === 'winter') return 'Winter';
                break;
            case 'NL': // Dutch
                if (key === 'settings') return 'Instellingen';
                if (key === 'gamesound') return 'Geluid van het spel';
                if (key === 'currency') return 'Munt';
                if (key === 'language') return 'Taal';
                if (key === 'close') return 'Sluiten';
                if (key === 'theme') return 'Thema';
                if (key === 'default') return 'Standaard';
                if (key === 'winter') return 'Winter';
                break;
            case 'FR': // French
                if (key === 'settings') return 'Paramètres';
                if (key === 'gamesound') return 'Son du jeu';
                if (key === 'currency') return 'Monnaie';
                if (key === 'language') return 'Langue';
                if (key === 'close') return 'Fermer';
                if (key === 'theme') return 'Thème';
                if (key === 'default') return 'Standard';
                if (key === 'winter') return 'Hiver';
                break;
            default: // Fallback
                if (key === 'settings') return 'Settings';
                if (key === 'gamesound') return 'Gamesound';
                if (key === 'currency') return 'Currency';
                if (key === 'language') return 'Language';
                if (key === 'close') return 'Close';
                if (key === 'theme') return 'Theme';
                if (key === 'default') return 'Default';
                if (key === 'winter') return 'Winter';
                break;
        }
        return '';
    };

    return (
        <Dialog open={isOpen} onClose={onClose}>
            <div className={"w-[500px]"}>
                <DialogTitle>{getTranslatedText('settings')}</DialogTitle>
                <DialogContent>
                    <p>{getTranslatedText('gamesound')}</p>
                    <Slider
                        min={0}
                        max={100}
                        value={soundLevel}
                        onChange={(_, newValue) => {
                            playSound("https://storage.googleapis.com/image_bucket_ip2/sounds/click.wav", (newValue as number))
                            setSoundLevel(newValue as number)
                        }}
                    />

                    <p>{getTranslatedText('currency')}</p>
                    <Select variant={"outlined"} IconComponent={ArrowDropDown}
                            value={currency}
                            onChange={(e) => setCurrency(e.target.value)}>
                        <MenuItem value={"Bitcoin"}>Bitcoin</MenuItem>
                        <MenuItem value={"Franc"}>Franc</MenuItem>
                        <MenuItem value={"Lira"}>Lira</MenuItem>
                        <MenuItem value={"Pound"}>Pound</MenuItem>
                        <MenuItem value={"Yen"}>Yen</MenuItem>
                        <MenuItem value={"Yuan"}>Yuan</MenuItem>
                        <MenuItem value={"Dollar"}>Dollar</MenuItem>
                        <MenuItem value={"Euro"}>Euro</MenuItem>
                    </Select>

                    <p>{getTranslatedText('language')}</p>
                    <Select variant={"outlined"} IconComponent={ArrowDropDown}
                            value={language}
                            onChange={(e) => setLanguage(e.target.value)}>
                        <MenuItem value={"EN"}>
                            <Flag code="GB" style={{width: 20, height: 15, marginRight: 10}}/>
                        </MenuItem>
                        <MenuItem value={"NL"}>
                            <Flag code="NL" style={{width: 20, height: 15, marginRight: 10}}/>
                        </MenuItem>
                        <MenuItem value={"FR"}>
                            <Flag code="FR" style={{width: 20, height: 15, marginRight: 10}}/>
                        </MenuItem>
                    </Select>
                    <p>{getTranslatedText("theme")}</p>
                    <Select variant={"outlined"} IconComponent={ArrowDropDown}
                            value={theme}
                            onChange={(e) => (setTheme(e.target.value))}>
                        <MenuItem value={"default"}>{getTranslatedText("default")}</MenuItem>
                        <MenuItem value={"Winter"}>{getTranslatedText("winter")}</MenuItem>
                    </Select>


                </DialogContent>
                <DialogActions>
                    <Button variant={"outlined"} onClick={onClose}>{getTranslatedText('close')}</Button>
                </DialogActions>
            </div>
        </Dialog>
    )
}
