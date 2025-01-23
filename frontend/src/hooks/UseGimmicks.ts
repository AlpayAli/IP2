import {useQuery} from "@tanstack/react-query";
import {getAvailableGimmicks, getGimmicks} from "../services/GimmickService";
import {purchaseGimmick} from "../services/GimmickService";
import {useMutation} from "@tanstack/react-query";
import {getPlayerGimmicks} from "../services/GimmickService";


export function useGimmicks() {
    const {
        isLoading: isLoadingGimmicks,
        isError: isErrorLoadingGimmicks,
        data: gimmicks,
        refetch,
    } = useQuery({
        queryKey: ['gimmicks'],
        queryFn: getGimmicks,
        refetchInterval: 30000,
    });

    return {
        isLoadingGimmicks,
        isErrorLoadingGimmicks,
        gimmicks,
        refetch,
    };
}


export function usePlayerGimmicks() {
    const {
        isLoading: isLoadingPlayerGimmicks,
        isError: isErrorLoadingPlayerGimmicks,
        data: playerGimmicks,
        refetch,
    } = useQuery({
        queryKey: ['playerGimmicks'],
        queryFn: getPlayerGimmicks,
    });

    return {
        isLoadingPlayerGimmicks,
        isErrorLoadingPlayerGimmicks,
        playerGimmicks,
        refetch,
    };
}


export function usePurchaseGimmick() {
    const {
        mutate: purchase,
        isPending: isPurchasingGimmick,
        isError: isErrorPurchasingGimmick,
        data: purchasedGimmick,
        reset,
    } = useMutation({
        mutationFn: ({gimmickId, type}: {gimmickId: string; type: string}) => purchaseGimmick(gimmickId, type),
    });

    return {
        purchase,
        isPurchasingGimmick,
        isErrorPurchasingGimmick,
        purchasedGimmick,
        reset,
    };
}


export function useAvailableGimmicks() {
    const {
        isLoading: isLoadingAvailableGimmicks,
        isError: isErrorLoadingAvailableGimmicks,
        data: availableGimmicks,
        refetch,
    } = useQuery({
        queryKey: ['availableGimmicks'],
        queryFn: getAvailableGimmicks,
    });

    return {
        isLoadingAvailableGimmicks,
        isErrorLoadingAvailableGimmicks,
        availableGimmicks,
        refetch,
    };
}

