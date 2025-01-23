import {useQuery} from "@tanstack/react-query";
import {PlayerProfile} from "../model/PlayerProfile.ts";
import {getPlayerProfile} from "../services/PlayerProfileService.ts";

export function usePlayerInformation(username : string  ) {
    const {isLoading, isError, data: playerProfile} = useQuery<PlayerProfile>({
        queryKey: ['playerProfile', username],
        queryFn: () => getPlayerProfile(username),
        refetchInterval : 10000
    });

    return {
        isLoading,
        isError,
        playerProfile
    }
}