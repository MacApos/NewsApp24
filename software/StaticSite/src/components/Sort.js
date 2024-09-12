import React, {useState} from 'react';
import {useDispatch} from "react-redux";
import {setSort} from "../reducers/newsSlice";
import {ASC, DESC} from "../constants/constants";

export const Sort = () => {
    const datePublished = "datePublished";
    const categories = {
        Latest: {category: datePublished, order: DESC},
        Oldest: {category: datePublished, order: ASC}
    };

    const entries = Object.entries(categories);
    const dispatch = useDispatch();
    const [key, setKey] = useState(entries[0][0]);

    const handleClick = ([key, value]) => {
        setKey(() => key);
        dispatch(setSort(value));
    };

    return (
        <>
            <button type="button" className="btn btn-outline-light dropdown-toggle w-130-px" data-bs-toggle="dropdown">
                {key}
            </button>
            <ul className="dropdown-menu w-130-px">
                {entries.map(entry => {
                    const [key] = entry
                    return (
                        <li key={"sortBy" + key} className="dropdown-item">
                            <button className="btn btn-outline-light"
                                    onClick={() => handleClick(entry)}>
                                {key}
                            </button>
                        </li>
                    );
                })}
            </ul>
        </>
    );
};