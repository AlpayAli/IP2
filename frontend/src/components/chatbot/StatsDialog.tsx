import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";
import {Button, Dialog, DialogActions} from "@mui/material";
import {Stats} from "./Stats.tsx";


interface StatsDialogProps {
    isOpen: boolean,
    onClose: () => void
}

export function StatsDialog({isOpen, onClose}: StatsDialogProps) {
    const { language } = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'close') return 'Close';
                break;
            case 'NL':
                if (key === 'close') return 'Sluiten';
                break;
            case 'FR':
                if (key === 'close') return 'Fermer';
                break;
            default:
                if (key === 'close') return 'Close';
                break;
        }
        return '';
    };
    return (
        <Dialog className={"p-0"}
            open={isOpen} onClose={onClose}>
            <div className={"w-full"}>
                <Stats/>
                <DialogActions>
                    <Button variant={"outlined"} onClick={onClose}>{getTranslatedText('close')}</Button>
                </DialogActions>
            </div>
        </Dialog>
    )
}