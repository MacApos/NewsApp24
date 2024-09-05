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
        if (numberOfPages > 1) {
            const pages = Array.from({length: numberOfPages}, (_, i) => i + 1);
            list =
                <div className="btn-group">
                    <button className="btn btn-outline-primary border-end-0"
                            onClick={() => page > 1 && dispatch(setPage(page - 1))}>
                        &lt;
                    </button>
                    {pages.map(pageIdx => {
                            return (
                                <button key={"page" + pageIdx}
                                        className={"btn btn-outline-primary border-end-0" +
                                            (pageIdx === page ? " active" : "")}
                                        onClick={() => dispatch(setPage(pageIdx))}>
                                    {pageIdx}
                                </button>);
                        }
                    )}
                    <button className="btn btn-outline-primary"
                            onClick={() => page < numberOfPages && dispatch(setPage(page + 1))}>
                        &gt;
                    </button>
                </div>;
        }
    }
    return list;
};