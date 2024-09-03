import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {newsAPI} from "./newsAPI";

const PENDING = "pending";
const SUCCEEDED = "succeeded";
const REJECTED = "rejected";
const ASC = "ascending";
const DESC = "descending";

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

export const compareFn = (sort) => (a, b) => {
    const {category, order} = sort
    const aCat = a[category];
    const bCat = b[category];
    const orderMultiplier = order === ASC ? 1 : -1;
    return (aCat < bCat ? -1 : aCat > bCat ? 1 : 0) * orderMultiplier;
};

const newsSlice = createSlice({
        name: "news",
        initialState,
        reducers: {
            setNews: (state, action) => {
                state.response.news = action.payload;
            },
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

export {PENDING, SUCCEEDED, REJECTED, ASC, DESC};
export const {
    selectNews,selectArticles, selectStatus,
    selectError, selectSort,
    selectPage, selectArticlesOnPage
} = newsSlice.selectors;
export const {sortNews, setNews, setSort,
    setArticlesOnPage, setPage} = newsSlice.actions;
export default newsSlice.reducer;