import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {newsAPI} from "./newsAPI";

const PENDING = "pending";
const SUCCEEDED = "succeeded";
const REJECTED = "rejected";

const initialState = {
    news: [],
    status: "idle",
    error: null,
};

export const fetchNews = createAsyncThunk(
    'news/fetchNews',
    async (city) => {
        const response = await newsAPI.fetchNews(city);
        return await response.json();
    },
);

const newsSlice = createSlice({
        name: "news",
        initialState,
        reducers: {
            updateNews: (state, action) => {
                state.news = action.payload;
            }
        },
        extraReducers: (builder) => {
            builder
                .addCase(fetchNews.pending, (state) => {
                    state.status = PENDING;
                })
                .addCase(fetchNews.fulfilled, (state, action) => {
                    state.status = SUCCEEDED;
                    state.news = action.payload;
                })
                .addCase(fetchNews.rejected, (state) => {
                    state.status = REJECTED;
                    state.error = "Unknown Error";
                    console.warn("rejected");
                });
        },
        selectors: {
            selectNews: state => state.news,
            selectStatus: state => state.status,
            selectError: state => state.error,
        },
    }
);

export {PENDING, SUCCEEDED, REJECTED};
export const {
    selectNews,
    selectStatus, selectError
} = newsSlice.selectors;
export const {updateNews} = newsSlice.actions;
export default newsSlice.reducer;