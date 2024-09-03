import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {selectArticles, selectArticlesOnPage, selectSort, setPage} from "../features/reducers/newsSlice";

export const Pagination = () => {
    const dispatch = useDispatch();
    const articlesOnPage = useSelector(selectArticlesOnPage);
    const articles = useSelector(selectArticles);

    let list = null;
    if (articles) {
        const numberOfPages = Math.ceil(articles.length / articlesOnPage);
        const pages = Array.from({length: numberOfPages}, (_, i) => i + 1);
        list =
            <ul style={{display: "block", listStyle: "none"}}>
                {pages.map(pageIdx => <li style={{width: "5%", float: "left"}}
                onClick={() => dispatch(setPage(pageIdx))}>{pageIdx}</li>)}
            </ul>;
    }

    return list;
};