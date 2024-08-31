import {combineReducers, configureStore} from "@reduxjs/toolkit";
import newsSlice from "../features/reducers/newsSlice";

const rootReducer = combineReducers({
    news:newsSlice
});

// const persistConfig = {
//     key:"root",
//     storage,
//     whitelist: ["a"]
// }

// const persistedReducer = persistReducer(persistConfig, rootReducer)

const store = configureStore({
    reducer: rootReducer,
});

// const persistor = persistStore(store)
export {store};

