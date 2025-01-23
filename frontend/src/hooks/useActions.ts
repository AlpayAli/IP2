import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postPlayerAction } from "../services/ActionService.ts";

export const useActions = () => {
    const queryClient = useQueryClient();

    const {
        mutate: newPlayerAction,
        isError: isErrorActing,
    } = useMutation({
        mutationFn: ({ roundId, actionType, raiseAmount }: { roundId: string; actionType: string; raiseAmount: number }) =>
            postPlayerAction(roundId, actionType, raiseAmount),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey : ['bets']});
        },
    });

    return {
        newPlayerAction,
        isErrorActing,
    };
};
