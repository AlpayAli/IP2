import Keycloak from 'keycloak-js';

const realm = import.meta.env.VITE_KEYCLOAK_REALM ?? throwEnvError('VITE_KEYCLOAK_REALM');
const url = import.meta.env.VITE_KEYCLOAK_URL ?? throwEnvError('VITE_KEYCLOAK_URL');
const clientId = import.meta.env.VITE_KEYCLOAK_CLIENT_ID ?? throwEnvError(' VITE_KEYCLOAK_CLIENT_ID');

const keycloak = new Keycloak({
    realm,
    url,
    clientId,
});

function throwEnvError(varName: string): never {
    throw new Error(`Environment variable ${varName} is not defined`);
}

export default keycloak;
