import statesJson from "../states.json";
import {useState} from "react";

export const StateList = () => {
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