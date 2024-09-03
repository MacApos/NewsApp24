import React from 'react';
import {useDispatch, useSelector} from "react-redux";
import {selectArticles, selectArticlesOnPage, selectSort} from "../features/reducers/newsSlice";

export const Pagination = () => {
    const dispatch = useDispatch();
    const articlesOnPage = useSelector(selectArticlesOnPage);
    const articles = useSelector(selectArticles);

    let list = null;
    if (articles) {
        const numberOfPages= Math.ceil(articles.length / articlesOnPage);
        // style="width:10%; float:left;

        const from = Array.from({length: numberOfPages});
        list =
            <ul style={{}}>
                {from.map(pageIdx=><li style={{width:"10%", float:"left"}}></li>)}
            </ul>;
    }

    return list;
};