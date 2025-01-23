import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {acceptInvite, declineInvite, inviteAllFriends, sendInvite} from "../services/InviteService.ts";
import {getFriends} from "../services/FriendService.ts";

export const useInvite = () => {
    const queryClient = useQueryClient()

    const {
        isLoading: isLoadingFriends,
        isError: isErrorLoadingFriends,
        data: friends = []
    } = useQuery({
        queryKey: ['friends'],
        queryFn: () => getFriends(),
    });

    const {
        mutate: invite,
    } = useMutation({
        mutationFn: ({username, gameId}: { username: string, gameId: string }) => sendInvite(username, gameId),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['invites']})
        },
    })

    const {
        mutate: inviteAll,
    } = useMutation({
        mutationFn: ({gameId}: { gameId: string }) => inviteAllFriends(gameId),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['inviteAll']})
        },
    })

    const {
        mutate: accept,
        isPending: isAccepting,
        data: acceptedInvite,
        isSuccess: isAccepted,
    } = useMutation({
        mutationFn: async (invitationId: string) => {
            return acceptInvite(invitationId);
        },
        onSuccess: (data) => {
            queryClient.invalidateQueries({queryKey: ['acceptedInvites']});
            return data;
        },
    })

    const {
        mutate: decline,
    } = useMutation({
        mutationFn: (invitationId: string) => declineInvite(invitationId),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['invites']})
        },
    })

    return {
        isLoadingFriends,
        isErrorLoadingFriends,
        friends,
        invite,
        inviteAll,
        accept,
        isAccepting,
        isAccepted,
        acceptedInvite,
        decline
    }
}