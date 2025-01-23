import axios from "axios";
import keycloak from "./keycloakAdapter.ts";

axios.defaults.baseURL = import.meta.env.VITE_BACKEND_URL || throwEnvError('VITE_BACKEND_URL');
axios.defaults.withCredentials = true;

function throwEnvError(varName: string): never {
    throw new Error(`Environment variable ${varName} is not defined`);
}

axios.interceptors.request.use(
    (config) => {
        const token = keycloak.token;
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axios;
