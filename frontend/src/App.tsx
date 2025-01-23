import SecurityContextProvider from "./context/security/SecurityContextProvider.tsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {RouteGuard} from "./components/RouteGuard.tsx";
import {HostGame} from "./components/game/HostGame.tsx";
import axios from "axios";
import {OpenGames} from "./components/game/OpenGames.tsx";
import {Header} from "./components/general/Header.tsx";
import {Background} from "./components/general/Background.tsx";
import {Game} from "./components/game/Game.tsx";
import PokerGame from "./components/general/PokerGame.tsx";
import {FriendList} from "./components/friends/FriendList.tsx";
import {AddFriend} from "./components/friends/AddFriend.tsx";
import {Invites} from "./components/game/Invites.tsx";
import CurrentPlayerContextProvider from "./context/player/CurrentPlayerContextProvider.tsx";
import {Achievements} from "./components/general/Achievements.tsx";
import {GimmickStore} from "./components/gimmickstore/GimmickStore.tsx";
import {Leaderboard} from "./components/leaderboard/Leaderboard.tsx";
import SettingsContextProvider from "./context/settings/SettingsContextProvider.tsx";
import PlayerProfile from "./components/profile/PlayerProfile.tsx";

const baseURL = import.meta.env.VITE_BACKEND_URL ?? throwEnvError('VITE_KEYCLOAK_REALM');
axios.defaults.baseURL = baseURL;
axios.defaults.withCredentials = true;

function throwEnvError(varName: string): never {
    throw new Error(`Environment variable ${varName} is not defined`);
}


const App = () => {
    return (
        <SecurityContextProvider>
            <CurrentPlayerContextProvider>
                <SettingsContextProvider>
                    <Background>
                        <BrowserRouter>
                            <Header/>
                            <Routes>
                                <Route path="/" element={<RouteGuard><PokerGame/></RouteGuard>}/>
                                <Route path="/join" element={<RouteGuard><PokerGame/></RouteGuard>}/>
                                <Route path="/find" element={<RouteGuard><OpenGames/></RouteGuard>}/>
                                <Route path="/host" element={<RouteGuard><HostGame/></RouteGuard>}/>
                                <Route path="/invite/:id" element={<RouteGuard><Invites/></RouteGuard>}/>
                                <Route path="/game/:id" element={<RouteGuard><Game/></RouteGuard>}/>
                                <Route path="/friends" element={<RouteGuard><FriendList/></RouteGuard>}/>
                                <Route path="/add-friend" element={<RouteGuard><AddFriend/></RouteGuard>}/>
                                <Route path="/achievements" element={<RouteGuard><Achievements/></RouteGuard>}/>
                                <Route path="/store" element={<RouteGuard><GimmickStore/></RouteGuard>}/>
                                <Route path="/leaderboard" element={<RouteGuard><Leaderboard/></RouteGuard>}/>
                                <Route path="/profile/:username" element={<RouteGuard><PlayerProfile/></RouteGuard>}/>
                            </Routes>
                        </BrowserRouter>
                    </Background>
                </SettingsContextProvider>
            </CurrentPlayerContextProvider>
        </SecurityContextProvider>
    )
};

export default App;
