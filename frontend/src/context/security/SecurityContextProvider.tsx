import {ReactNode, useEffect, useState} from 'react'
import SecurityContext from './SecurityContext.ts'
import {addAccessTokenToAuthHeader, removeAccessTokenFromAuthHeader} from '../../services/auth.ts'
import {isExpired} from 'react-jwt'
import keycloak from "../../config/keycloakAdapter.ts";
import {Loading} from "../../components/general/Loading.tsx";


interface IWithChildren {
    children: ReactNode
}

export default function SecurityContextProvider({children}: IWithChildren) {
    const [loggedInUser, setLoggedInUser] = useState<string | undefined>(undefined);
    // const [playerInfo, setPlayerInfo] = useState<Player | undefined>(undefined); FIXME: Implement playerInfo
    const [isKeycloakInitialized, setIsKeycloakInitialized] = useState(false);

    useEffect(() => {
        keycloak
            .init({onLoad: 'login-required'})
            .then(() => setIsKeycloakInitialized(true))
            .catch((error) => {
                console.error('Keycloak initialization failed:', error);
                setIsKeycloakInitialized(false);
            });
    }, []);


    if (!isKeycloakInitialized) {
        return <Loading/>;
    }

    keycloak.onAuthSuccess = () => {
        addAccessTokenToAuthHeader(keycloak.token)
        setLoggedInUser(keycloak.idTokenParsed?.given_name)
    }

    keycloak.onAuthLogout = () => {
        removeAccessTokenFromAuthHeader()
    }

    keycloak.onAuthError = () => {
        removeAccessTokenFromAuthHeader()
    }

    keycloak.onTokenExpired = () => {
        keycloak.updateToken(-1).then(function () {
            addAccessTokenToAuthHeader(keycloak.token)
            setLoggedInUser(keycloak.idTokenParsed?.given_name)
        })
    }

    function login() {
        const redirect = window.location.origin + window.location.pathname;
        keycloak.login({redirectUri: redirect});
    }

    function logout() {
        const logoutOptions = {redirectUri: import.meta.env.VITE_REACT_APP_URL}
        keycloak.logout(logoutOptions)
    }

    function isAuthenticated() {
        if (keycloak.token) return !isExpired(keycloak.token)
        else return false
    }

    return (
        <SecurityContext.Provider
            value={{
                isAuthenticated,
                loggedInUser,
                login,
                logout,
            }}
        >
            {children}
        </SecurityContext.Provider>
    )
}
