import {Button, Dialog, DialogActions} from "@mui/material";
import {ChatBot} from "./ChatBot.tsx";
import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

interface ChatBotDialogProps {
    isOpen: boolean,
    onClose: () => void
}

export function ChatBotDialog({isOpen, onClose}: ChatBotDialogProps) {
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
        <Dialog
            open={isOpen} onClose={onClose}>
            <div className={"w-[400px]"}>
               <ChatBot/>
                <DialogActions>
                    <Button variant={"outlined"} onClick={onClose}>{getTranslatedText('close')}</Button>
                </DialogActions>
            </div>
        </Dialog>
    )
}

