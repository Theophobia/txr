import React, { createContext } from 'react';
import useAuth, {AuthHook} from "./useAuth";

export const AuthContext = createContext<null | AuthHook>(null);

export const AuthProvider = ({ children }) => {
	const auth = useAuth();

	return (
		<AuthContext.Provider value={ auth }>
			{children}
		</AuthContext.Provider>
	);
};
