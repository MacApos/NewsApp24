import React from 'react';
import {useDispatch} from "react-redux";
import {fetchNews} from "../reducers/newsSlice";
import {TRENDING} from "../constants/constants";

export const TrendingButton = () => {
    const dispatch = useDispatch();
    return (
        <>
            <button className="btn btn-outline-light" onClick={() => dispatch(fetchNews(TRENDING))}>
                Trending
            </button>
        </>
    );
};