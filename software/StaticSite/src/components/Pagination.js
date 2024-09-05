import React from 'react';
import {useDispatch, useSelector} from "react-redux";
import {selectArticles, selectArticlesOnPage, selectPage, setPage} from "../reducers/newsSlice";

export const Pagination = () => {
    const dispatch = useDispatch();
    const articlesOnPage = useSelector(selectArticlesOnPage);
    const page = useSelector(selectPage);
    const articles = useSelector(selectArticles);


    let list = null;
    if (articles) {
        const numberOfArticles = articles.length;
        const numberOfPages = Math.ceil(numberOfArticles / articlesOnPage);
        console.log(page);
        console.log(page < numberOfArticles);
        console.log(page > 1);
        if (numberOfPages > 1) {
            const pages = Array.from({length: numberOfPages}, (_, i) => i + 1);
            list =
                <nav aria-label="Page navigation example">
                    <ul className="pagination">
                        <li className="page-item">
                            <button className="page-link"
                                    onClick={() => page > 1 && dispatch(setPage(page - 1))}>
                                <span>&laquo;</span>
                            </button>
                        </li>
                        {pages.map(pageIdx => <li className="page-item">
                            <button className="page-link "
                                    onClick={() => dispatch(setPage(pageIdx))}>{pageIdx}</button>
                        </li>)}
                        <li className="page-item">
                            <button className="page-link"
                                    onClick={() => page < numberOfPages && dispatch(setPage(page + 1))}>
                                <span>&raquo;</span>
                            </button>
                        </li>
                    </ul>
                </nav>;
        }
    }
    return list;
};