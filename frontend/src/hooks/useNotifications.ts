import {useQuery} from "@tanstack/react-query";
import {getNotifications} from "../services/NotificationService.ts";

export function useNotifications() {


    const {
        data: notifications,
        isLoading: isLoadingNotifications,
        isError: isErrorNotifications,
        refetch: refetchNotifications,
    } = useQuery({
        queryKey: ['notifications'],
        queryFn: getNotifications,
        refetchInterval: 3000,
    })

    return {
        notifications,
        isLoadingNotifications,
        isErrorNotifications,
        refetchNotifications,
    }
}