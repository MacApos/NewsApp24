import {Provider, useDispatch} from "react-redux";
import {Form} from "./components/Form";
import {store} from "./app/store";
import {News} from "./components/News";
import {Sort} from "./components/Sort";
import {StateList} from "./components/StateList";
import {useEffect} from "react";
import {fetchNews} from "./features/reducers/newsSlice";
import {TRENDING} from "./features/reducers/newsAPI";
import {Pagination} from "./components/Pagination";

const initMaps = (g) => {
    (g => {
        var h, a, k, p = "The Google Maps JavaScript API", c = "google", l = "importLibrary", q = "__ib__",
            m = document, b = window;
        b = b[c] || (b[c] = {});
        var d = b.maps || (b.maps = {}), r = new Set, e = new URLSearchParams,
            u = () => h || (h = new Promise(async (f, n) => {
                await (a = m.createElement("script"));
                e.set("libraries", [...r] + "");
                for (k in g) e.set(k.replace(/[A-Z]/g, t => "_" + t[0].toLowerCase()), g[k]);
                e.set("callback", c + ".maps." + q);
                a.src = `https://maps.${c}apis.com/maps/api/js?` + e;
                d[q] = f;
                a.onerror = () => h = n(Error(p + " could not load."));
                a.nonce = m.querySelector("script[nonce]")?.nonce || "";
                m.head.append(a);
            }));
        d[l] ? console.warn(p + " only loads once. Ignoring:", g) : d[l] = (f, ...n) => r.add(f) && u().then(() => d[l](f, ...n));
    })({
        key: "AIzaSyA3kcTFb3b2pX6BRjRRn1gvfRW0uBX3c1M",
        v: "weekly",
    });
};


export const App = () => {
    initMaps();

    return (
        <Provider store={store}>
            <Form/>
            <Sort/>
            <Pagination/>
            <News/>
            {/*<StateList/>*/}
        </Provider>
    );
};

//

//
// const NewsList = () => {
//
//
//     useEffect(() => {
//
//     }, []);
//
//     return (
//         <>
//         </>
//     );
// };
//
//
