import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from "@mui/material";
import Spinwheel from "./Spinwheel.tsx";
import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

interface DailySpinDialogProps {
    isOpen: boolean,
    onClose: () => void
}

export function DailySpinDialog({isOpen, onClose}: DailySpinDialogProps) {
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN': // English
                if (key === 'dailySpinTitle') return 'Daily Spin';
                if (key === 'closeButton') return 'Close';
                break;
            case 'NL': // Dutch
                if (key === 'dailySpinTitle') return 'Dagelijkse Spin';
                if (key === 'closeButton') return 'Sluiten';
                break;
            case 'FR': // French
                if (key === 'dailySpinTitle') return 'Roulette Quotidienne';
                if (key === 'closeButton') return 'Fermer';
                break;
            // Voeg hier meer talen toe indien nodig.
            default:
                if (key === 'dailySpinTitle') return 'Daily Spin';
                if (key === 'closeButton') return 'Close';
                break;
        }
        return '';
    };

    return (
        <Dialog
            open={isOpen} onClose={onClose}>
            <div className={"w-[600px] relative"}>
                <DialogTitle>{getTranslatedText("dailySpinTitle")}</DialogTitle>
                <DialogContent>
                    <Spinwheel/>
                </DialogContent>
                <DialogActions>
                    <Button variant={"outlined"} onClick={onClose}>
                        {getTranslatedText("closeButton")}
                    </Button>
                </DialogActions>
            </div>
        </Dialog>
    )
}


