import { useQuery } from "@tanstack/react-query";
import { getLeaderboardByBalance, getLeaderboardByXp } from "../services/LeaderboardService.ts";

export const useLeaderboard = () => {
    const {
        isLoading: isLoadingBalanceLeaderboard,
        isError: isErrorLoadingBalanceLeaderboard,
        data: leaderboardByBalance = [],
    } = useQuery({
        queryKey: ["leaderboardByBalance"],
        queryFn: () => getLeaderboardByBalance(),
    });

    const {
        isLoading: isLoadingXpLeaderboard,
        isError: isErrorLoadingXpLeaderboard,
        data: leaderboardByXp = [],
    } = useQuery({
        queryKey: ["leaderboardByXp"],
        queryFn: () => getLeaderboardByXp(),
    });

    return {
        isLoadingBalanceLeaderboard,
        isErrorLoadingBalanceLeaderboard,
        leaderboardByBalance,
        isLoadingXpLeaderboard,
        isErrorLoadingXpLeaderboard,
        leaderboardByXp,
    };
};
