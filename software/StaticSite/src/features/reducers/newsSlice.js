import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {newsAPI} from "./newsAPI";

const PENDING = "pending";
const SUCCEEDED = "succeeded";
const REJECTED = "rejected";
const ASC = "ascending";
const DESC = "descending";

const initialState = {
    newsResponse: {
        news: {},
        status: "idle",
        error: null,
    },
    sort: {
        category: "datePublished",
        order: ASC | DESC
    }
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
            setNews: (state, action) => {
                state.newsResponse.news = action.payload;
            },
            setSort: (state, action) => {
                state.sort = action.payload;
            }
        },
        extraReducers: (builder) => {
            builder
                .addCase(fetchNews.pending, (state) => {
                    state.newsResponse.status = PENDING;
                })
                .addCase(fetchNews.fulfilled, (state, action) => {
                    state.newsResponse.status = SUCCEEDED;
                    state.newsResponse.news = action.payload;
                })
                .addCase(fetchNews.rejected, (state) => {
                    state.newsResponse.status = REJECTED;
                    state.newsResponse.error = "Unknown Error";
                    console.warn("rejected");
                });
        },
        selectors: {
            selectNews: state => state.newsResponse.news,
            selectArticles: state => state.newsResponse.news.articles,
            selectStatus: state => state.newsResponse.status,
            selectError: state => state.newsResponse.error,
            selectSort: state => state.sort,
        },
    }
);

export {PENDING, SUCCEEDED, REJECTED, ASC, DESC};
export const {
    selectNews, selectStatus,
    selectError, selectSort,
    selectArticles
} = newsSlice.selectors;
export const {setNews, setSort} = newsSlice.actions;
export default newsSlice.reducer;