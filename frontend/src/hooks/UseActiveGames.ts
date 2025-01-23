import {useQuery} from "@tanstack/react-query";
import {GameRequest} from "../model/GameRequest.ts";
import {getActiveGames} from "../services/GameDataService.ts";

export function useActiveGames() {

    const {isLoading, isError, data: activeGames} = useQuery<GameRequest[]>({
        queryKey: ['activeGames'],
        queryFn: getActiveGames,
    });


    return {isLoading, isError, data: activeGames};
}