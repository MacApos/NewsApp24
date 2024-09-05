import React from 'react';
import {selectArticlesOnPage, setArticlesOnPage, setPage} from "../reducers/newsSlice";
import {useDispatch, useSelector} from "react-redux";

export const PagesSelect = () => {
    const dispatch = useDispatch();
    const articlesOnPage = useSelector(selectArticlesOnPage);

    const handleSelectChange = (e) => {
            dispatch(setArticlesOnPage(Number(e.target.value)));
            dispatch(setPage(1));
    };

    return (
        <>
            <select className="btn btn-outline-light"
                    value={articlesOnPage} onChange={handleSelectChange}>
                {[5, 10, 20].map(number => <option key={"numberOfPages" + number}
                                                   value={number}>{number}</option>)}
            </select>
        </>
    );
};