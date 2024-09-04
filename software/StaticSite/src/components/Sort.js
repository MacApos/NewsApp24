import React from 'react';
import {useDispatch, useSelector} from "react-redux";
import {selectSort, setSort} from "../reducers/newsSlice";
import {ASC, DESC} from "../constants/constants";

export const Sort = () => {
    const dispatch = useDispatch();
    const sort = useSelector(selectSort);
    const {category, order} = sort;
    const datePublished = "datePublished";

     const handleSelectChange = (e) => {
        const [category, order] = e.target.value.split(",");
        dispatch(setSort({category, order}));
    };

    return (
        <>
            <select className="btn btn-outline-light" value={joinWithComa(category, order)} onChange={handleSelectChange}>
                <option value={joinWithComa(datePublished, DESC)}>Latest</option>
                <option value={joinWithComa(datePublished, ASC)}>Oldest</option>
            </select>
        </>
    );
};

function joinWithComa() {
    return [...arguments].join(",");
}