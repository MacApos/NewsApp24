import React from "react";
import {Provider} from "react-redux";
import {store} from "./store/store";
import {Form} from "./components/Form";
import {News} from "./components/News";
import {Sort} from "./components/Sort";
import {StateList} from "./components/StateList";
import {PagesSelect} from "./components/PagesSelect";
import {NavigationBar} from "./components/NavigationBar";
import {NewsContainer} from "./components/NewsContainer";
import {initMaps} from "./utils/initMaps";
import {TrendingButton} from "./components/TrendingButton";
import {Pagination} from "./components/Pagination";

export const App = () => {
    initMaps();
    return (
        <>
            <Provider store={store}>
                <NavigationBar>
                    <TrendingButton/>
                    <Sort/>
                    <PagesSelect/>
                    <Form/>
                </NavigationBar>
                <NewsContainer>
                    <StateList/>
                    <News/>
                    <Pagination/>
                </NewsContainer>

            </Provider>
        </>
    );
};
