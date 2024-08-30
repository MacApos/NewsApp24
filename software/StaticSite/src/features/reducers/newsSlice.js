import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {newsAPI} from "./newsAPI";

const initialState = {
    posts: [],
    status: "idle",
    error: null,
};

const fetchNews = createAsyncThunk(
    'news/fetchNews',
    async (city) => {
        const response = await newsAPI.fetchNews(city);
        return response.json();
    },
);

const newsSlice = createSlice({
        name: "news",
        initialState,
    extraReducers:(builder)=>{
            builder
                .addCase(fetchPosts.pending, (state, action) => {
                    state.status = "pending";
                })
                .addCase(fetchPosts.fulfilled, (state, action) => {
                    state.status = "succeeded";
                    // Save the fetched posts into state
                    state.posts = action.payload;
                })
                .addCase(fetchPosts.rejected, (state, action) => {
                    state.status = "rejected";
                    state.error = action.error.message ?? "Unknown Error";
                })
    },
        reducers: {

        },
        selectors: {
            selectNews: (state) => state
        },

    }
);

export const {newsFetched} = newsSlice.actions;

export const {selectNews} = newsSlice.selectors;

export default newsSlice.reducer;