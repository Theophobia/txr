import storage from "redux-persist/lib/storage";
import {persistReducer, persistStore} from "redux-persist";
import rootReducer from "./reducers";
import {configureStore} from "@reduxjs/toolkit";
import thunk from "redux-thunk";

const persistConfig = {
	key: 'root',
	storage: storage,
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
	reducer: persistedReducer,
	devTools: true,
	middleware: [thunk]
});

export const persistor = persistStore(store)
