import {useMutation, useQueryClient} from "@tanstack/react-query";
import {dailySpin} from "../services/PlayerService.ts";

export function useDailySpin() {
    const queryClient = useQueryClient();

    const {
        mutate: spin,
        isSuccess: isSpun,
        data: spinData,
    } = useMutation({
        mutationFn: async () => {
            return await dailySpin();
        },
        onSuccess: (data) => {
            queryClient.invalidateQueries({queryKey: ["dailySpin"]});
            return data;
        },
    });

    return {
        spin,
        isSpun,
        spinData
    }
}