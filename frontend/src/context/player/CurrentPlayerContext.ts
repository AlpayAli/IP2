import {Player} from "../../model/Player.ts";
import {createContext} from "react";

export interface ICurrentPlayerContext {
    currentPlayer: Player | undefined;
    selectedBackground: string;
    setSelectedBackground: (background: string) => void;
}

export const defaultBackground = "https://storage.googleapis.com/image_bucket_ip2/general/table-bg/1.png";


export default createContext<ICurrentPlayerContext>({
    currentPlayer: undefined,
    selectedBackground: defaultBackground, // Default value
    setSelectedBackground: () => {}, // Default implementation
});
