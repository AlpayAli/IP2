import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { getMessages, sendMessage } from "../services/MessageService";

export function useMessages(gameId: string) {
    const queryClient = useQueryClient();

    const {
        isLoading: isLoadingMessages,
        isError: isErrorLoadingMessages,
        data: messages,
        refetch,
    } = useQuery({
        queryKey: ["messages", gameId],
        queryFn: () => getMessages(gameId),
        refetchInterval: 1000,
    });

    const { mutate: sendMessageMutation, isError: isErrorSendingMessage } = useMutation({
        mutationFn: (content: string) => sendMessage(gameId, content),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["messages", gameId] });
        },
    });

    return {
        messages,
        isLoadingMessages,
        isErrorLoadingMessages,
        refetch,
        sendMessage: sendMessageMutation,
        isErrorSendingMessage,
    };
}