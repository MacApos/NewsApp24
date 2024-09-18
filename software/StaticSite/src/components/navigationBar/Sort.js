import React from 'react';
import {useDispatch, useSelector} from "react-redux";
import {initialState, selectSort, setSort} from "../../reducers/newsSlice";
import {ASC} from "../../constants/constants";

export const Sort = () => {
    const dispatch = useDispatch();
    const sort = useSelector(selectSort);
    const datePublished = "datePublished";
    const categories = [
        initialState.sort,
        {name: "Oldest", category: datePublished, order: ASC}
    ];

    const handleClick = (value) => {
        dispatch(setSort(value));
    };

    return (
        <>
            <button type="button" className="btn btn-outline-light dropdown-toggle w-130-px" data-bs-toggle="dropdown">
                {sort.name}
            </button>
            <ul className="dropdown-menu w-130-px">
                {categories.map(category => {
                    const {name} = category;
                    return (
                        <li key={"sortBy" + name} className="dropdown-item">
                            <button className="btn btn-outline-light"
                                    onClick={() => handleClick(category)}>
                                {name}
                            </button>
                        </li>
                    );
                })}
            </ul>
        </>
    );
};