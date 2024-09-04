import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {newsAPI} from "../utils/newsAPI";
import {DESC, PENDING, REJECTED, SUCCEEDED} from "../constants/constants";

const initialState = {
    response: {
        news: {},
        status: "idle",
        error: null,
    },
    sort: {
        category: "datePublished",
        order: DESC
    },
    page: 1,
    articlesOnPage: 20
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
            setSort: (state, action) => {
                state.sort = action.payload;
            },
            setArticlesOnPage: (state, action) => {
                state.articlesOnPage = action.payload;
            },
            setPage: (state, action) => {
                state.page = action.payload;
            }
        },
        extraReducers: (builder) => {
            builder
                .addCase(fetchNews.pending, (state) => {
                    state.response.status = PENDING;
                })
                .addCase(fetchNews.fulfilled, (state, action) => {
                    state.response.status = SUCCEEDED;
                    state.response.news = action.payload;
                    state.sort = initialState.sort;
                })
                .addCase(fetchNews.rejected, (state) => {
                    state.response.status = REJECTED;
                    state.response.error = "Unknown Error";
                    console.warn("rejected");
                });
        },
        selectors: {
            selectNews: state => state.response.news,
            selectArticles: state => state.response.news.articles,
            selectStatus: state => state.response.status,
            selectError: state => state.response.error,
            selectSort: state => state.sort,
            selectPage: state => state.page,
            selectArticlesOnPage: state => state.articlesOnPage,
        },
    }
);

export const {
    selectNews, selectArticles,
    selectStatus, selectSort,
    selectPage, selectArticlesOnPage
} = newsSlice.selectors;
export const {
    setSort, setArticlesOnPage,
    setPage
} = newsSlice.actions;
export default newsSlice.reducer;