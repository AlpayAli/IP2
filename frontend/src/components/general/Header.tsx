import {
    Star,
    EmojiEvents,
    QuestionMark,
    Settings,
    Logout,
    GroupOutlined,
    Notifications, Store, Home, GraphicEq
} from '@mui/icons-material';
import {useContext, useState} from "react";
import SecurityContext from "../../context/security/SecurityContext.ts";
import {useNavigate} from "react-router-dom";
import {useNotifications} from "../../hooks/useNotifications.ts";
import {NotificationsDialog} from "../notifications/NotificationsDialog.tsx";
import {ChatBotDialog} from "../chatbot/ChatBotDialog.tsx";
import {SettingsDialog} from "./SettingsDialog.tsx";
import {StatsDialog} from "../chatbot/StatsDialog.tsx";

export function Header() {
    const {logout} = useContext(SecurityContext);
    const navigate = useNavigate()
    const {notifications} = useNotifications()
    const [isNotificationsOpen, setIsNotificationsOpen] = useState(false)
    const [isSettingsOpen, setIsSettingsOpen] = useState(false)
    const [isChatBotOpen, setIsChatBotOpen] = useState(false)
    const [isStatsOpen, setIsStatsOpen] = useState(false)

    return (
        <>
            <NotificationsDialog isOpen={isNotificationsOpen} onClose={() => setIsNotificationsOpen(false)}/>
            <SettingsDialog isOpen={isSettingsOpen} onClose={() => setIsSettingsOpen(false)}/>
            <ChatBotDialog isOpen={isChatBotOpen} onClose={() => setIsChatBotOpen(false)}/>
            <StatsDialog isOpen={isStatsOpen} onClose={() => setIsStatsOpen(false)}/>
            <div className={"flex w-full absolute justify-between"}>
                <div className={" grid grid-cols-6 gap-7 m-5"}>
                    <button className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}
                            onClick={() => navigate(`/`)}
                    >
                        <Home sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "#fcd34d"
                        }}/>

                    </button>
                    <button className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}
                            onClick={() => navigate(`/achievements`)}
                    >
                        <Star sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "#fcd34d"
                        }}/>

                    </button>
                    <button
                        onClick={() => navigate(`/leaderboard`)}
                        className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}
                    >
                        <EmojiEvents sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "#fcd34d"
                        }}/>
                    </button>
                    <button className={"relative rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}>
                        <Notifications
                            onClick={() => setIsNotificationsOpen(true)}
                            sx={{
                                width: "100%",
                                height: "100%",
                                marginY: "auto",
                                marginX: "auto",
                                color: "#fcd34d"
                            }}/>
                        {notifications && notifications.length > 0 &&
                            <div className={"absolute rounded-full w-3 h-3 bg-red top-0"}>
                            </div>
                        }
                    </button>
                    <button
                        onClick={() => navigate(`/store`)}
                        className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}>
                        <Store sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "#fcd34d"
                        }}/>
                    </button>
                    <button
                        onClick={() => navigate(`/friends`)}
                        className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}>
                        <GroupOutlined sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "#fcd34d"
                        }}/>
                    </button>
                </div>

                <div className={" grid grid-cols-4 gap-5 m-5"}>
                    <button
                        onClick={() => setIsStatsOpen(true)}
                        className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}>
                        <GraphicEq sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "white"
                        }}/>

                    </button>
                    <button
                        onClick={() => setIsChatBotOpen(true)}
                        className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}>
                        <QuestionMark sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "white"
                        }}/>

                    </button>
                    <button className={"rounded-full bg-blue-500 w-[50px] h-[50px] hover:scale-110"}
                            onClick={() => setIsSettingsOpen(true)}>
                        <Settings sx={{
                            width: "100%",
                            height: "100%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "white"
                        }}/>
                    </button>

                    <button
                        onClick={logout}
                        className={"rounded-full bg-red w-[50px] h-[50px] hover:scale-110"}>
                        <Logout sx={{
                            width: "70%",
                            height: "70%",
                            marginY: "auto",
                            marginX: "auto",
                            color: "white"
                        }}/>
                    </button>
                </div>
            </div>
        </>
    )
}

export default Header