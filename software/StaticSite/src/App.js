import React from "react";
import {Provider} from "react-redux";
import {store} from "./store/store";
import {Form} from "./components/navigationBar/Form";
import {Sort} from "./components/navigationBar/Sort";
import {StateList} from "./components/navigationBar/StateList";
import {PagesSelect} from "./components/navigationBar/PagesSelect";
import {NavigationBar} from "./components/navigationBar/NavigationBar";
import {TrendingButton} from "./components/navigationBar/TrendingButton";
import {Fetch} from "./components/news/Fetch";
import {Container} from "./components/news/Container";
import initMaps from "./utils/initPlaceAutocomplete";

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
                <Container>
                    <StateList/>
                    <Fetch/>
                </Container>
            </Provider>
        </>
    );
};
