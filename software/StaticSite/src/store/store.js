import {combineReducers, configureStore} from "@reduxjs/toolkit";
import newsSlice from "../reducers/newsSlice";

const rootReducer = combineReducers({
    news: newsSlice
});

const store = configureStore({
    reducer: rootReducer,
});

export {store};

