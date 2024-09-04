import React from 'react';
import {selectArticlesOnPage, setArticlesOnPage} from "../reducers/newsSlice";
import {useDispatch, useSelector} from "react-redux";

export const PagesSelect = () => {
    const dispatch = useDispatch();
    const articlesOnPage = useSelector(selectArticlesOnPage);

    const handleSelectChange = (e) => {
            dispatch(setArticlesOnPage(Number(e.target.value)));
    };

    return (
        <>
            <select className="btn btn-outline-light"
                    value={articlesOnPage} onChange={handleSelectChange}>
                {[5, 10, 20].map(number => <option key={"pages" + number}
                                                   value={number}>{number}</option>)}
            </select>
        </>
    );
};