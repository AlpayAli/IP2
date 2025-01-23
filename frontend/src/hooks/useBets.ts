import { useQuery } from "@tanstack/react-query";
import { getBettingRoundByGameId } from "../services/BettingRoundService.ts";
import { BettingRound } from "../model/BettingRound";

export function useBets(gameId: string | undefined) {
    const {
        data: bettingRound,
        isLoading,
        isError,
        error,
    } = useQuery<BettingRound, Error>({
        queryKey: ["bets", gameId],
        queryFn: () => {
            if (!gameId) {
                throw new Error("Game ID is null");
            }
            return getBettingRoundByGameId(gameId);
        },
        enabled: !!gameId, // Voorkom dat de query wordt uitgevoerd zonder gameId
    });

    const bets = bettingRound
        ? {
            actions: bettingRound.playerActionDtoList.reduce(
                (acc, action) => {
                    acc[action.playerName] = action.amount;
                    return acc;
                },
                {} as { [playerName: string]: number }
            ),
            activePlayer: bettingRound.activePlayer, // Voeg activePlayer toe
        }
        : { actions: {}, activePlayer: null }; // Zorg voor een fallback als bettingRound null is

    return {
        bets,
        isLoading,
        isError,
        error: isError && error ? error.message : null,
    };
}
