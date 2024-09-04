import React from 'react';
import {PLACEHOLDER} from "../constants/constants";

const AnchorWrapper = (props) => {
    return (
        <a href={props.href} target="_blank">
            {props.children}
        </a>
    );
};

export const Article = ({article}) => {
    const image = article.image;
    return (
        <div className="row mb-3">
            <div className="col">
                <div className="card text-bg-success">
                    <div className="row">
                        <div className="col-auto">
                            <img className="rounded-start" src={image === null ? PLACEHOLDER : image}
                                 alt="" width={300} height={"auto"}/>
                        </div>
                        <div className="col">
                            <div className="card-body text-start">
                                <AnchorWrapper href={article.url}>
                                    <h5 className="card-title">
                                        {article.name}
                                    </h5>
                                </AnchorWrapper>
                                <p className="card-text text-body-secondary">
                                    {formatDate(article.datePublished)}
                                </p>
                                <p className="card-text">{article.description}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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