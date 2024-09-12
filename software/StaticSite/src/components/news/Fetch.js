import * as React from 'react';
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {fetchNews, selectStatus} from "../../reducers/newsSlice";
import {PENDING, REJECTED, FULFILLED, TRENDING} from "../../constants/constants";
import {Fulfilled} from "./Fulfilled";
import {Rejected} from "./Rejected";

export const Fetch = () => {
    const status = useSelector(selectStatus);
    const dispatch = useDispatch();

    let content;
    useEffect(() => {
        const item = sessionStorage.getItem("cityName");
        if (item === null || item === TRENDING) {
            dispatch(fetchNews(TRENDING));
        } else {
            dispatch(fetchNews(item));
        }
    }, [dispatch]);


    if (status === PENDING) {
        content = <div className="spinner-border m-5" style={{width: "3rem", height: "3rem"}}/>;
    } else if (status === FULFILLED) {
        content = <Fulfilled/>;
    } else if (status === REJECTED) {
        content = <Rejected/>;
    }
    return (
        <>
            {content}
        </>
    );
};
