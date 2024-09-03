import React from 'react';
import {setArticlesOnPage, setPage} from "../features/reducers/newsSlice";
import {useDispatch} from "react-redux";

export const PagesSelect = () => {
    const dispatch = useDispatch();
    const style = {display: "inline"};

    return (
        <>
            <div>{"Articles on page "}
                <form style={style}>
                    <input type={"number"} onChange={e => {
                        dispatch(setArticlesOnPage(e.target.value));
                        dispatch(setPage(1));
                    }}/>
                </form>
            </div>
            {/*<select  onChange={handleSortChange}>*/}
            {/*<option value={joinWithComa(datePublished, DESC)}>Latest</option>*/}
            {/*<option value={joinWithComa(datePublished, ASC)}>Oldest</option>*/}
            {/*<option value={joinWithComa(random, DESC)}>Random DESC</option>*/}
            {/*<option value={joinWithComa(random, ASC)}>Random ASC</option>*/}
            {/*</select>*/}
        </>
    );
};