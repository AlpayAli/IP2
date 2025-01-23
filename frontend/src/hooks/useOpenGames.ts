import {useQuery} from "@tanstack/react-query";
import {GameRequest} from "../model/GameRequest.ts";
import {getOpenGames} from "../services/GameDataService.ts";

export function useOpenGames() {
    const {isLoading, isError, data: openGames} = useQuery<GameRequest[]>({
        queryKey: ['openGames'],
        queryFn: getOpenGames,
    });


    return {isLoading, isError, data: openGames};
}