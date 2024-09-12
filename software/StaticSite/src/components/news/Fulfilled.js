import React from 'react';
import {Pagination} from "./Pagination";
import {Article} from "./Article";
import {useSelector} from "react-redux";
import {selectArticlesOnPage, selectNews, selectPage, selectSort} from "../../reducers/newsSlice";
import {ASC, STATES, TRENDING} from "../../constants/constants";

export const Fulfilled = () => {
    const news = useSelector(selectNews);
    const sort = useSelector(selectSort);
    const page = useSelector(selectPage);
    const articlesOnPage = useSelector(selectArticlesOnPage);

    let articles = news.articles;
    articles = articles.map(article => {
        return {
            ...article,
            datePublished: new Date(Date.parse(article.datePublished)),
        };
    });

    let name = news.name;
    const find = STATES.find(countryState => countryState.name === news.state);
    name = name === TRENDING ? "Trending news" : find === undefined ? name : name + ", " + find.id;

    const start = (page - 1) * articlesOnPage;
    const stop = page * Math.min(articlesOnPage, articles.length);
    return (
        <>
            <div className="row my-3">
                <div className="col">
                    <div className="row justify-content-between align-items-center"
                         style={{height: "60px"}}>
                        <div className="col">
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
            <div className="row text-center mb-4">
                <div className="col">
                    <Pagination/>
                </div>
            </div>
        </>
    );
};

const compareFn = (sort) => (a, b) => {
    const {category, order} = sort;
    const aCat = a[category];
    const bCat = b[category];
    const orderMultiplier = order === ASC ? 1 : -1;
    return (aCat > bCat ? 1 : aCat < bCat ? -1 : 0) * orderMultiplier;
};