import React from 'react';
import {PLACEHOLDER} from "../../constants/constants";

export const Article = ({article}) => {
    const image = article.image;
    return (
        <a href={article.url} target="_blank" rel="noreferrer">
            <div className="card mb-4 bg-light border-primary shadow"
                 style={{borderWidth: "1px 2px 2px 1px"}}>
                <div className="row mw-100 mx-0">
                    <div className="col-auto px-0">
                        <img src={image === null ? PLACEHOLDER : image}
                             alt={article.name} width={400} height={"auto"}/>
                    </div>

                    <div className="col card-text text-start text-primary p-2_5 fs-4">
                        <h5 className="card-title text-capitalize fs-3 mb-3">
                            {article.name}
                        </h5>
                        <p>
                            {formatDate(article.datePublished)}
                        </p>
                        <p className="mb-0">
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