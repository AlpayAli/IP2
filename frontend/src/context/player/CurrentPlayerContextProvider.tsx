import {ReactElement, useEffect, useState} from "react";
import {usePlayer} from "../../hooks/usePlayer.ts";
import CurrentPlayerContext, { defaultBackground } from "./CurrentPlayerContext.ts";

interface IWithChildren {
    children: ReactElement | ReactElement[];
}

export default function CurrentPlayerContextProvider({ children }: IWithChildren) {
    const { currentPlayer } = usePlayer();
    const [selectedBackground, setSelectedBackground] = useState<string>(() => {
        return localStorage.getItem("selectedBackground") || defaultBackground;
    });

    useEffect(() => {
        localStorage.setItem("selectedBackground", selectedBackground);
    }, [selectedBackground]);

    return (
        <CurrentPlayerContext.Provider
            value={{
                currentPlayer,
                selectedBackground, // Ensure this is provided
                setSelectedBackground: (background: string) => setSelectedBackground(background),
            }}
        >
            {children}
        </CurrentPlayerContext.Provider>
    );
}

