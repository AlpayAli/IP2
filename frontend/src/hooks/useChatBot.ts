import {askChatBot, calculateStats} from "../services/ChatBotService";
import { useMutation, useQueryClient } from "@tanstack/react-query";

export const useChatBot = () => {
    const queryClient = useQueryClient();

    const {
        status,
        isError: isErrorLoadingChatBotResponse,
        data: chatBotResponse,
        mutateAsync: sendMessage,  // Gebruik mutateAsync voor async functies
    } = useMutation({
        mutationFn: (message: string) => askChatBot(message),  // Functie die de API-aanroep doet
        onSuccess: (response) => {
            queryClient.setQueryData(['chatBotResponse'], response);
        },
        onError: (error) => {
            console.error('Error occurred:', error);
        }
    });

    return {
        status,
        isErrorLoadingChatBotResponse,
        chatBotResponse,
        sendMessage,
    };
};

export const useCalculateStats = () => {
    const queryClient = useQueryClient();

    const {
        status,
        isError: isErrorCalculatingStats,
        data: statsResponse,
        mutateAsync: calculateStatsAsync, // Gebruik mutateAsync voor async functies
    } = useMutation({
        mutationFn: (params: {
            minPlayers: number;
            maxPlayers: number;
            playTime: number;
            minAge: number;
            usersRated: number;
            domains: string[];
            mechanics: string[];
        }) => calculateStats(
            params.minPlayers,
            params.maxPlayers,
            params.playTime,
            params.minAge,
            params.usersRated,
            params.domains,
            params.mechanics
        ),
        onSuccess: (response) => {
            queryClient.setQueryData(['statsResponse'], response);
        },
        onError: (error) => {
            console.error('Error occurred while calculating stats:', error);
        }
    });

    return {
        status,
        isErrorCalculatingStats,
        statsResponse,
        calculateStatsAsync,
    };
};