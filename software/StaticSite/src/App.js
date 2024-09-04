import React from "react";
import {Provider} from "react-redux";
import {store} from "./app/store";
import {Form} from "./components/Form";
import {News} from "./components/News";
import {Sort} from "./components/Sort";
import {StateList} from "./components/StateList";
import {PagesSelect} from "./components/PagesSelect";
import {NavigationBar} from "./components/NavigationBar";
import {NewsContainer} from "./components/NewsContainer";
import {initMaps} from "./features/reducers/initMaps";

export const App = () => {
    initMaps();
    return (
        <>
            <Provider store={store}>
                <NavigationBar>
                    <Sort/>
                    <PagesSelect/>
                    <Form/>
                </NavigationBar>
                <NewsContainer>
                    <StateList/>
                    <News/>
                </NewsContainer>
            </Provider>
        </>
    );
};
