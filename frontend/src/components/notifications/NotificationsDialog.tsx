import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from "@mui/material";
import {useNotifications} from "../../hooks/useNotifications.ts";
import {useFriendsNotification} from "../../hooks/useFriends.ts";
import {useInvite} from "../../hooks/useInvite.ts";
import {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Notification} from "./Notification.tsx";
import {DailySpinDialog} from "../dailyspin/DailySpinDialog.tsx";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

interface NotificationsDialogProps {
    isOpen: boolean,
    onClose: () => void
}

export function NotificationsDialog({isOpen, onClose}: NotificationsDialogProps) {
    const {notifications, isLoadingNotifications, isErrorNotifications} = useNotifications()
    const {addFriend, mutateDeclineFriendRequest} = useFriendsNotification()
    const {decline, accept, isAccepted, acceptedInvite} = useInvite()
    const navigate = useNavigate()
    const [isDailySpinOpen, setisDailySpinOpen] = useState(false)
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'notifications') return 'Notifications';
                if (key === 'close') return 'Close';
                if (key === 'accept') return 'Accept';
                if (key === 'decline') return 'Decline';
                if (key === 'play') return 'Play';
                if (key === 'spin') return 'Spin';
                break;
            case 'NL':
                if (key === 'notifications') return 'Notificaties';
                if (key === 'close') return 'Sluiten';
                if (key === 'accept') return 'Accepteren';
                if (key === 'decline') return 'Afwijzen';
                if (key === 'play') return 'Spelen';
                if (key === 'spin') return 'Draaien';
                break;
            case 'FR':
                if (key === 'notifications') return 'Notifications';
                if (key === 'close') return 'Fermer';
                if (key === 'accept') return 'Accepter';
                if (key === 'decline') return 'Décliner';
                if (key === 'play') return 'Jouer';
                if (key === 'spin') return 'Tourner';
                break;
            default:
                if (key === 'notifications') return 'Notifications';
                if (key === 'close') return 'Close';
                if (key === 'accept') return 'Accept';
                if (key === 'decline') return 'Decline';
                if (key === 'play') return 'Play';
                if (key === 'spin') return 'Spin';
                break;
        }
        return '';
    }


    useEffect(() => {
        if (isAccepted) {
            onClose()
            navigate(`/game/${acceptedInvite?.gameId}`)
        }
    }, [isAccepted]);


    return (
        <>
            <DailySpinDialog isOpen={isDailySpinOpen} onClose={() => setisDailySpinOpen(false)}/>
            <Dialog
                open={isOpen} onClose={onClose}>
                <div className={"w-[500px]"}>
                    <DialogTitle>{getTranslatedText('notifications')}</DialogTitle>
                    <DialogContent>
                        {isLoadingNotifications && <p>Loading...</p>}
                        {isErrorNotifications && <p>Error</p>}
                        {notifications && notifications.length == 0 && <p>No notifications</p>}
                        {notifications && notifications.map((notification) => (
                            <div key={notification.notificationId}>
                                <Notification
                                    date={notification.date}
                                    notificationText={() => {
                                        switch (notification.type) {
                                            case "INVITATION":
                                                return `${notification.senderName} heeft u uitgenodigd om mee te spelen met zijn pokerspel!`
                                            case "FRIEND_REQUEST":
                                                return `Nieuw vriendschapverzoek van ${notification.senderName}`
                                            case "DAILY_SPIN":
                                                return `U heeft uw dagelijkse spin ontvangen!`
                                            default:
                                                return ""
                                        }
                                    }}
                                    buttonText={() => {
                                        switch (notification.type) {
                                            case "INVITATION":
                                                return getTranslatedText('play')
                                            case "FRIEND_REQUEST":
                                                return getTranslatedText('accept')
                                            case "DAILY_SPIN":
                                                return getTranslatedText('spin')
                                            default:
                                                return ""
                                        }
                                    }}
                                    accept={() => {
                                        switch (notification.type) {
                                            case "INVITATION":
                                                accept(notification.notificationId)
                                                break;
                                            case "FRIEND_REQUEST":
                                                addFriend(notification.senderId)
                                                break;
                                            case "DAILY_SPIN": {
                                                setisDailySpinOpen(true)
                                                onClose()
                                            }
                                                break;
                                        }
                                    }}
                                    decline={() => {
                                        switch (notification.type) {
                                            case "INVITATION":
                                                decline(notification.notificationId)
                                                break;
                                            case "FRIEND_REQUEST":
                                                mutateDeclineFriendRequest(notification.senderId)
                                                break;
                                            case "DAILY_SPIN":
                                                onClose()
                                                break;
                                        }
                                    }}/>
                            </div>

                        ))}
                    </DialogContent>
                    <DialogActions>
                        <Button variant={"outlined"} onClick={onClose}>{getTranslatedText('close')}</Button>
                    </DialogActions>

                </div>
            </Dialog>
        </>
    )
}

