import {GameData} from "../model/GameRequest.ts";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createGame, getGame, startingGame} from "../services/GameDataService.ts";

export function useCreateGame() {
    const queryClient = useQueryClient()

    const {
        mutate: addGame,
        isSuccess: isGameAdded,
        data: newGame,
        isPending: isAddingGame,
    } = useMutation({
        mutationFn: async (game: GameData) => {
            const response = await createGame(game);
            return response;
        },
        onSuccess: (data) => {
            queryClient.invalidateQueries({queryKey: ['games']});
            return data;
        },
    })

    return {
        addGame,
        newGame,
        isGameAdded,
        isAddingGame,
    }
}

export function useGame(id : string) {
    const {
        isLoading: isLoadingGameById,
        isError: isErrorLoadingGameById,
        data: game,
        refetch,
    } = useQuery({
        queryKey: ['game', id],
        queryFn: () => getGame(id),
        refetchInterval: 1000,
    })

    return {
        isLoadingGameById,
        isErrorLoadingGameById,
        game,
        refetch,
    }
}

export function useStartGame(gameId: string) {
    const queryClient = useQueryClient()

    const {
        mutate: startGame,
    } = useMutation({
        mutationFn: () => startingGame(gameId),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['game']});
        },
    })

    return {
        startGame,
    }
}

