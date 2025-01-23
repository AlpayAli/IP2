import {getAchievements} from "../services/AchievementService.ts";
import {useQuery} from "@tanstack/react-query";
import {getRecentAchievement} from "../services/AchievementService.ts";

export function useAchievements() {
    const {
        isLoading: isLoadingAchievements,
        isError: isErrorLoadingAchievements,
        data: achievements,
        refetch,
    } = useQuery({
        queryKey: ['achievements'],
        queryFn: getAchievements,
        refetchInterval: 30000, // Optioneel interval om automatisch te verversen
    });

    return {
        isLoadingAchievements,
        isErrorLoadingAchievements,
        achievements,
        refetch,
    };
}





export function useRecentAchievements(enable: boolean) {
    const {
        isLoading: isLoadingRecentAchievements,
        isError: isErrorLoadingRecentAchievements,
        data: recentAchievements,
        refetch,
    } = useQuery({
        queryKey: ['recentAchievements'],
        queryFn: getRecentAchievement,
        enabled: enable, // Alleen actief als `enable` true is
        refetchInterval: enable ? 1000 : false, // Poll elke 5 seconden alleen als `enable` true is
    });

    return {
        isLoadingRecentAchievements,
        isErrorLoadingRecentAchievements,
        recentAchievements,
        refetch,
    };
}


