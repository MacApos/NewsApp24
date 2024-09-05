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
        <a href={article.url} target="_blank">
            <div className="card mb-4 bg-light border-primary shadow"
                 style={{borderWidth: "1px 2px 2px 1px"}}>
                <div className="row align-items-end text-primary">
                    <div className="col-auto align-self-start text-start">
                        <img className="float-start me-3 mb-1" src={image === null ? PLACEHOLDER : image}
                             alt={article.name} width={400} height={"auto"}/>
                        <h5 className="card-title text-capitalize fs-3 text-primary my-3">
                            {article.name}
                        </h5>
                        <p className="card-text fs-4">
                            {formatDate(article.datePublished)}
                        </p>
                        <p className="card-text  mb-3 text-bottom fs-4">
                            {article.description}
                        </p>
                    </div>
                </div>
            </div>
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