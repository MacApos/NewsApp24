import React from "react";
import {Provider} from "react-redux";
import {store} from "./store/store";
import {Form} from "./components/Form";
import {Sort} from "./components/Sort";
import {StateList} from "./components/StateList";
import {PagesSelect} from "./components/PagesSelect";
import {NavigationBar} from "./components/NavigationBar";
import {TrendingButton} from "./components/TrendingButton";
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
