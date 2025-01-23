import {ReactNode, useContext, useEffect} from 'react'
import SecurityContext from '../context/security/SecurityContext.ts'

export interface RouteGuardProps {
    children: ReactNode;
}

export function RouteGuard({ children }: RouteGuardProps) {
    const { isAuthenticated, login } = useContext(SecurityContext);

    useEffect(() => {
        if (!isAuthenticated()) {
            login();
        }
    }, [isAuthenticated, login]);

    if (isAuthenticated()) {
        return <>{children}</>;
    }
    return null;
}