import { useMutation, useQueryClient } from "@tanstack/react-query";
import {joinGame, leaveGame} from "../services/GameDataService";


export function useJoinGame() {
    const queryClient = useQueryClient();

    /*return useMutation({
        mutationFn: (gameId: string) => joinGame(gameId),
        onSuccess: () => {
            queryClient.invalidateQueries(["games"]);
        },
    });*/

    const {
        mutate: participateGame,
    } = useMutation({
        mutationFn: (gameId: string) => {
            return joinGame(gameId)
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ["games"]});
        },
    })

    return {
        participateGame,
    }
}

export function useLeaveGame(onSuccessCallback: () => void) {
    const queryClient = useQueryClient();

    const {
        mutate: endGame,
    } = useMutation({
        mutationFn: (gameId: string) => {
            return leaveGame(gameId)
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ["games"]});
            onSuccessCallback();
        },
    })

    return {
        endGame,
    }
}