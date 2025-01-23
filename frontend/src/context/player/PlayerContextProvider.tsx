// import {ReactNode, useState} from "react";
// import {Player} from "../../model/Player.ts";
// import PlayerContext from "./PlayerContext.ts";
// import {getPlayer, isRegistered, register} from "../../services/PlayerService.ts";
//
//
// interface IWithChildren {
//     children: ReactNode
// }
//
// export default function PlayerContextProvider({children}: IWithChildren) {
//     const [player, setPlayer] = useState<Player | undefined>(undefined);
//
//     return (
//         <PlayerContext.Provider
//             value={{
//                 player,
//                 isRegistered,
//                 register,
//                 getPlayer,
//             }}
//         >
//             {children}
//         </PlayerContext.Provider>
//     )
// }