import {useEffect, useState} from "react";
import statesJson from "./states.json";



const Form = ({news}) => {
    const onChange = (e) => {
        e.preventDefault();
        setInput(e.target.value);
    };

    const [input, setInput] = useState("");
    return (
        <form onSubmit={e => e.preventDefault()}>
            <input type={"text"} value={input} onChange={onChange}/>
        </form>
    );
};

const StateList = () => {
    statesJson = statesJson.map(s => {
        return {...s, "clicked": false};
    });
    const [states, setStates] = useState(statesJson);

    const handleStateClicked = (id) => {
        setStates(() => states.map(state => {
                state.clicked = state.id === id;
                return state;
            }
        ));
    };

    return (
        <>
            <button onClick={event => console.log("click")}
                 onBlur={event => console.log("blur")}>CLICK
            </button>
            <ul>
                {states.map((state, index) =>
                    <li key={state.id}>
                        <StateItem state={state} onClick={handleStateClicked} onBlur={handleStateClicked}/>
                    </li>)}
            </ul>
        </>
    );
};

const StateItem = ({state, onClick, onBlur}) => {
    const handleClick = () => {
        if (typeof onClick === "function") {
            onClick(state.id);
        }
    };

    return (
        <div key={state.id} onClick={handleClick} onBlur={() => {
            if (typeof onBlur === "function") {
                console.log("blur");
                onBlur();
            }
        }}>
            {state.name}
            {state.clicked &&
                <ul>
                    {state.cities.map((city, index) =>
                        <li key={`${city.name}-${index}`}>
                            {city.name}
                        </li>)}
                </ul>}
        </div>
    );
};

const fetchData = async () => {
    try {
        const result = await fetch(`http://localhost:5000/new york/new york`);
        console.log(result);
        const pokemon = await result.json();
        console.log(pokemon);
    } catch (err) {
        console.log(err);
    }
};

const NewsList = () =>{


    useEffect(() => {

    }, []);

    return (
        <>
        </>
    )
}

export const App = () => {
    const [news, setNews] = useState("test");
    return (
        <>
            <Form news={news}/>
            <StateList/>
        </>
    );
};
