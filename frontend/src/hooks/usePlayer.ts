import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {getPlayer, updatePlayer, updateUsername} from "../services/PlayerService.ts";
import {Player} from "../model/Player.ts";


export function usePlayer() {
    const queryClient = useQueryClient();

    const {
        data: player,
        isLoading: isLoadingPlayer,
        isError: isErrorPlayer,
        refetch: refetchPlayer,
    } = useQuery({
        queryKey: ["players"],
        queryFn: () => getPlayer(),
        refetchInterval: 3000,
    })

    const {mutate: editPlayer} = useMutation({
        mutationFn: (updatedPlayer: Player) => {
            return updatePlayer(updatedPlayer);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ["players"]});
        },
    });

    return {
        currentPlayer: player,
        isLoadingPlayer,
        isErrorPlayer,
        refetchPlayer,
        editPlayer
    }
}

export function useChangeName() {
    const queryClient = useQueryClient();

    const {mutate: changeName} = useMutation({
        mutationFn: ({newName, file}: { newName: string; file?: File }) => {
            return updateUsername(newName, file);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ["players"]}); // Refetch player data
        },
    });

    return {
        changeName,
    };
}

