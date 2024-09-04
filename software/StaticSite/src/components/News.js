import * as React from 'react';
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {
    fetchNews, selectNews, selectSort, selectStatus,
    selectArticlesOnPage, selectPage
} from "../reducers/newsSlice";
import {ASC, PENDING, PLACEHOLDER, REJECTED, SUCCEEDED, TRENDING} from "../constants/constants";
import statesJson from "../states.json";
import {Article} from "./Article";
import {Pagination} from "./Pagination";


export const News = (props) => {
    const news = useSelector(selectNews);
    const status = useSelector(selectStatus);
    const sort = useSelector(selectSort);
    const page = useSelector(selectPage);
    const articlesOnPage = useSelector(selectArticlesOnPage);
    const dispatch = useDispatch();

    let content;
    useEffect(() => {
        const item = sessionStorage.getItem("cityName");
        if (item === null) {
            dispatch(fetchNews(TRENDING));
        } else {
            dispatch(fetchNews(item));
        }
    }, []);

    if (status === PENDING) {
        content = "...";
    } else if (status === SUCCEEDED) {
        let articles = news.articles;
        articles = articles.map(article => {
            return {
                ...article,
                datePublished: new Date(Date.parse(article.datePublished)),
            };
        });

        let name = news.name;
        const find = statesJson.find(countryState => countryState.name === news.state);
        name = name === TRENDING ? "Trending news" : find === undefined ? name : name + ", " + find.id;

        const start = (page - 1) * articlesOnPage;
        const stop = page * Math.min(articlesOnPage, articles.length);

        content =
            <>
                <p className="h2 text-start">{name}</p>
                {articles.slice(start, stop).sort(compareFn(sort)).map((article, index) => {
                        return (
                            <Article key={news.name + index} article={article}/>

                        );
                    }
                )}
            </>;
    } else if (status === REJECTED) {
        content = "ERROR!";
    }
    return (
        <>
            {content}
        </>
    );
};

const compareFn = (sort) => (a, b) => {
    const {category, order} = sort;
    const aCat = a[category];
    const bCat = b[category];
    const orderMultiplier = order === ASC ? 1 : -1;
    return (aCat < bCat ? -1 : aCat > bCat ? 1 : 0) * orderMultiplier;
};