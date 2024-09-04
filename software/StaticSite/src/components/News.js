import * as React from 'react';
import {
    fetchNews, selectNews, selectSort, selectStatus,
    PENDING, REJECTED, SUCCEEDED, compareFn, selectArticlesOnPage, selectPage
} from "../features/reducers/newsSlice";
import {useDispatch, useSelector} from "react-redux";
import {TRENDING} from "../features/reducers/newsAPI";
import {useEffect} from "react";


const AnchorWrapper = (props) => {
    return (
        <a href={props.href} target="_blank">
            {props.children}
        </a>
    );
};

const addLeadingZeroToDate = (number) => {
    return number <= 9 ? `0${number}` : number;
};

const formatDate = (date) => {
    return `${date.getFullYear()}.` +
        [date.getMonth() + 1, date.getDate()].map(date => addLeadingZeroToDate(date)).join(".") +
        " " + [date.getHours(), date.getMinutes()]
            .map(date => addLeadingZeroToDate(date)).join(":");
};

export const News = () => {
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
                random: Math.floor(Math.random() * 100)
            };
        });

        const start = (page - 1) * articlesOnPage;
        const stop = page * Math.min(articlesOnPage, articles.length);
        console.log(start, stop);

        content =
            <ul>{`${news.name}, ${news.state}`}
                {articles.slice(start, stop).sort(compareFn(sort)).map((article, index) => {
                            const image = article.image;
                            return (
                                <li>
                                    <div>
                                        <h4>
                                            <AnchorWrapper href={article.url}>
                                                {`${index + 1}. ${article.name}`}
                                            </AnchorWrapper>
                                        </h4>
                                        {/*<p>*/}
                                        {/*    {article.random}*/}
                                        {/*</p>*/}
                                        {/*<p>*/}
                                        {/*    {formatDate(article.datePublished)}*/}
                                        {/*</p>*/}

                                        {/*<p>{article.description}</p>*/}
                                        {/*{image !== null &&*/}
                                        {/*    <AnchorWrapper href={article.url}>*/}
                                        {/*        <img src={image} alt={article.name} width={300} height={"auto"}/>*/}
                                        {/*    </AnchorWrapper>}*/}
                                    </div>

                                </li>
                            );
                        }
                    )}
            </ul>;
    } else if (status === REJECTED) {
        content = "ERROR!";
    }
    return (
        <>
            {content}
        </>
    );
};
