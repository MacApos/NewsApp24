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
        if (item === null || item === TRENDING) {
            dispatch(fetchNews(TRENDING));
        } else {
            dispatch(fetchNews(item));
        }
    }, []);


    if (status === PENDING) {
        content = <div className="spinner-border m-5 text-primary" style={{width: "3rem", height: "3rem"}}/>;
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
                <div className="row my-3">
                    <div className="col">
                        <div className="row justify-content-between align-items-center"
                             style={{height: "60px"}}>
                            <div className="col-auto">
                                <p className="h2 text-start my-0">{name}</p>
                            </div>
                            <div className="col-auto">
                                <Pagination/>
                            </div>

                        </div>
                    </div>
                </div>
                {articles.sort(compareFn(sort)).slice(start, stop).map((article, index) => {
                        return (
                            <Article key={name + "article" + index} article={article}/>
                        );
                    }
                )}
                <div className="row">
                    <div className="col">
                        <Pagination/>
                    </div>
                </div>
            </>;
    } else if (status === REJECTED) {
        content =
            <div className="vh-100">
                <div className="text-center">
                    <h1 className="fw-bold">404</h1>
                    <p className="fs-3"><span className="text-danger">Opps!</span> Page not found.</p>
                    <p className="lead">
                        The page you’re looking for doesn’t exist.
                    </p>
                    <a href="index.html" className="btn btn-primary">Go Home</a>
                </div>
            </div>;
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