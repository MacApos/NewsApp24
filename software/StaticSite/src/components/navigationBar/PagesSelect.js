import React, {useState} from 'react';
import {setArticlesOnPage, setPage} from "../../reducers/newsSlice";
import {useDispatch} from "react-redux";

export const PagesSelect = () => {
    const numbers = [5, 10, 20];
    const dispatch = useDispatch();
    const [number, setNumber] = useState(numbers[1]);

    const handleClick = (number) => {
        setNumber(number);
        dispatch(setArticlesOnPage(number));
        dispatch(setPage(1));
    };

    return (
        <>
            <button type="button" className="btn btn-outline-light dropdown-toggle w-90-px" data-bs-toggle="dropdown">
                {number}
            </button>
            <ul className="dropdown-menu w-90-px">
                {numbers.map(number =>
                    <li key={"numberOfPages" + number} className="dropdown-item">
                        <button className="btn btn-outline-light"
                                onClick={() => handleClick(number)}>
                            {number}
                        </button>
                    </li>
                )}
            </ul>
        </>
    );
};