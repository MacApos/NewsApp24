import React from 'react';
import {setArticlesOnPage, setPage} from "../features/reducers/newsSlice";
import {useDispatch} from "react-redux";

export const PagesSelect = () => {
    const dispatch = useDispatch();
    const style = {display: "inline"};

    return (
        <>
            <select className="btn btn-outline-light">
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
            </select>
            {/*<input type={"number"} onChange={e => {*/}
            {/*    dispatch(setArticlesOnPage(e.target.value));*/}
            {/*    dispatch(setPage(1));*/}
            {/*}}/>*/}
            {/*<select  onChange={handleSortChange}>*/}
            {/*<option value={joinWithComa(datePublished, DESC)}>Latest</option>*/}
            {/*<option value={joinWithComa(datePublished, ASC)}>Oldest</option>*/}
            {/*<option value={joinWithComa(random, DESC)}>Random DESC</option>*/}
            {/*<option value={joinWithComa(random, ASC)}>Random ASC</option>*/}
            {/*</select>*/}
        </>
    );
};