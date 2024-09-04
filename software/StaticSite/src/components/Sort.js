import React from 'react';
import {useDispatch, useSelector} from "react-redux";
import {ASC, DESC, selectSort, setSort} from "../features/reducers/newsSlice";

export const Sort = () => {
    const dispatch = useDispatch();
    const sort = useSelector(selectSort);
    const {category, order} = sort;
    const datePublished = "datePublished";
    const random = "random";

    const handleSortChange = (e) => {
        const [category, order] = e.target.value.split(",");
        dispatch(setSort({category, order}));
    };

    const style = {display: "inline"};
    return (
        <>
            <select className="btn btn-outline-light" value={joinWithComa(category, order)} onChange={handleSortChange}>
                <option value={joinWithComa(datePublished, DESC)}>Latest</option>
                <option value={joinWithComa(datePublished, ASC)}>Oldest</option>
                <option value={joinWithComa(random, DESC)}>RDESC</option>
                <option value={joinWithComa(random, ASC)}>RASC</option>
            </select>


        </>
    );
};

function joinWithComa() {
    return [...arguments].join(",");
}