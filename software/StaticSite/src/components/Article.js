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
        <div className="card text-bg-success rounded-3 mb-3">
            <div className="row align-items-center">
                <div className="col-auto">
                    <AnchorWrapper href={article.url}>
                        <img className="rounded-start-3" src={image === null ? PLACEHOLDER : image}
                             alt={article.name} width={350} height={"auto"}
                        />
                    </AnchorWrapper>
                </div>
                <div className="col align-self-start">
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