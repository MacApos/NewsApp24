import React, {useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {selectSort, ASC, DESC, selectNews, setSort, setNews, selectArticles} from "../features/reducers/newsSlice";

const getObjectKeyName = (object, keyName) => {
    const s = Object.keys(object).find(key=>key===keyName);
    console.log(s);
    return s;
}

export const Sort = () => {
    const dispatch = useDispatch;

    const sort = useSelector(selectSort);
    const articles = useSelector(selectArticles);

    // const datePublished = getObjectKeyName(articles,"datePublished");
    // console.log(datePublished);
    // const random = getObjectKeyName(articles, "random");
    // const initialSort = [datePublished, DESC];
    // const [articlesSort, setArticlesSort] = useState([sort.category, sort.order]);
    //
    // const handleSortChange = (e) => {
    //     setArticlesSort(e.target.value);
    //     const [category, order]=articlesSort
    //     dispatch(setSort({category, order}));
    // };

    return (
        <>
            {/*<select value={articlesSort} onChange={handleSortChange}>*/}
            {/*    <option value={initialSort}>Latest</option>*/}
            {/*    <option value={[datePublished, DESC]}>Oldest</option>*/}
            {/*    <option value={[random, ASC]}>Random ASC</option>*/}
            {/*    <option value={[random, DESC]}>Random DESC</option>*/}
            {/*</select>*/}
        </>
    );
};