import React from 'react';
import {PLACEHOLDER} from "../constants/constants";

const AnchorWrapper = (props) => {
    return (
        <a className="text-capitalize fs-3 text-primary" href={props.href} target="_blank">
            {props.children}
        </a>
    );
};

export const Article = ({article}) => {
    const image = article.image;
    return (
        <div className="card rounded-3 mb-4 bg-light border-primary border-2">
            <div className="row align-items-center text-primary">
                <div className="col-auto">
                    <AnchorWrapper href={article.url}>
                        <img className="rounded-start-3" src={image === null ? PLACEHOLDER : image}
                             alt={article.name} width={350} height={"auto"}
                        />
                    </AnchorWrapper>
                </div>
                <div className="col align-self-start">
                    <div className="card-body text-start">
                        <h5 className="card-title mb-3">
                            <AnchorWrapper href={article.url}>
                                {article.name}
                            </AnchorWrapper>
                        </h5>
                        <p className="card-text fs-4">
                            {formatDate(article.datePublished)}
                        </p>
                        <p className="card-text fs-4">{article.description}</p>
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