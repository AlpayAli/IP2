import {useMutation,  useQueryClient} from "@tanstack/react-query";
import {initializeRound} from "../services/RoundService.ts";

export function useInitializeRound(gameId: string) {
    const queryClient = useQueryClient()

    const {
        mutate: startRound,
    } = useMutation({
        mutationFn: () => initializeRound(gameId),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['games']});
        },
    })

    return {
        startRound,
    }
}


