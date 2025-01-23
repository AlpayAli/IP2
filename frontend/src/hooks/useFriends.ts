import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';
import {
    getFriends,
    addFriend,
    addFriendByNickname,
    getFriendRequests,
    declineFriendRequest
} from "../services/FriendService.ts";

export const useFriends = () => {
    const queryClient = useQueryClient();

    // Helper functie om queries ongeldig te maken
    const invalidateFriendQueries = () => {
        queryClient.invalidateQueries({queryKey: ['friends']});
        queryClient.invalidateQueries({queryKey: ['friendRequests']});
    };

    const {
        isLoading: isLoadingFriends,
        isError: isErrorLoadingFriends,
        data: friends = [],
        refetch
    } = useQuery({
        queryKey: ['friends'],
        queryFn: () => getFriends(),
        refetchInterval: 1000,
    });

    const {
        isLoading: isLoadingFriendRequests,
        isError: isErrorLoadingFriendRequests,
        data: friendRequests = [],
    } = useQuery({
        queryKey: ['friendRequests'],
        queryFn: () => getFriendRequests(),
        refetchInterval: 1000,
    });

    const {
        mutate: addFriendMutate,
        isError: isErrorAddingFriend,
    } = useMutation({
        mutationFn: (friendId: string) => addFriend(friendId),
        onSuccess: invalidateFriendQueries,
    });

    const {
        mutate: addFriendByNickName,
        isError: isErrorAddingFriendByNickName,
    } = useMutation({
        mutationFn: (nickname: string) => addFriendByNickname(nickname),
        onSuccess: invalidateFriendQueries,
    });

    const {
        mutate: mutateDeclineFriendRequest,
        isError: isErrorDecliningFriendRequest,
    } = useMutation({
        mutationFn: (playerId: string) => declineFriendRequest(playerId),
        onSuccess: invalidateFriendQueries,
    });

    return {
        isLoadingFriends,
        isErrorLoadingFriends,
        isLoadingFriendRequests,
        isErrorLoadingFriendRequests,
        isErrorDecliningFriendRequest,
        mutateDeclineFriendRequest,
        friendRequests,
        friends,
        refetch,
        addFriendByNickname: addFriendByNickName,
        isErrorAddingFriendByNickName,
        addFriend: addFriendMutate,
        isErrorAddingFriend,
    };
};

export const useFriendsNotification = () => {
    const queryClient = useQueryClient();
    // Helper functie om queries ongeldig te maken
    const invalidateFriendQueries = () => {
        queryClient.invalidateQueries({queryKey: ['friends']});
        queryClient.invalidateQueries({queryKey: ['friendRequests']});
    };


    const {
        mutate: mutateDeclineFriendRequest,
        isError: isErrorDecliningFriendRequest,
    } = useMutation({
        mutationFn: (playerId: string) => declineFriendRequest(playerId),
        onSuccess: invalidateFriendQueries,
    });

    const {
        mutate: addFriendMutate,
        isError: isErrorAddingFriend,
    } = useMutation({
        mutationFn: (friendId: string) => addFriend(friendId),
        onSuccess: invalidateFriendQueries,
    });

    return {
        mutateDeclineFriendRequest,
        isErrorDecliningFriendRequest,
        addFriend: addFriendMutate,
        isErrorAddingFriend,
    }
}
