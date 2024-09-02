import * as React from 'react';
import {PENDING, REJECTED, selectNews, selectStatus, SUCCEEDED} from "../features/reducers/newsSlice";
import {useSelector} from "react-redux";


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
    let content = status;

    let articles = JSON.parse(JSON.stringify(news)).articles;
    const articlesExists = typeof articles !== "undefined";
    if (articlesExists) {
        articles=articles.map(article => {
                return {
                    ...article,
                    random: Math.floor(Math.random() * 100)
                };
            }
        );
    }

    if (status === PENDING) {
        content = "...";
    } else if (status === SUCCEEDED) {
        content =
            <ul>{`${news.name}, ${news.state}`}
                {articlesExists && articles.map((article, index) => {
                    console.log(article);
                    const image = article.image;
                    const datePublished = new Date(Date.parse(article.datePublished));
                    return (
                        <li>
                            <div>
                                <h4>
                                    <AnchorWrapper href={article.url}>
                                        {article.name}
                                    </AnchorWrapper>
                                </h4>
                                <p>
                                    {article.random}
                                </p>
                                <p>
                                    {formatDate(datePublished)}
                                </p>

                                <p>{article.description}</p>
                                {image !== null &&
                                    <AnchorWrapper href={article.url}>
                                        <img src={image} alt={article.name} width={300} height={"auto"}/>
                                    </AnchorWrapper>}
                            </div>

                        </li>
                    );
                })
                }
            </ul>;
        // console.log(articles);
    } else if (status === REJECTED) {
        content = "ERROR!";
    }
    return (
        <>
            {content}
        </>
    );
};
