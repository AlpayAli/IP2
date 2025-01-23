import {isRegistered, register} from "../services/PlayerService.ts";
import {useEffect, useState} from "react";

export const usePlayerRegistration = () => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const checkAndRegister = async () => {
            try {
                const response = await isRegistered();
                if (!response.data) {
                    await register();
                }
            } catch (err: any) {
                setError(err.response?.data?.message || "Er is een fout opgetreden");
            } finally {
                setLoading(false);
            }
        };
        checkAndRegister();
    }, []);

    return { loading, error };
};
