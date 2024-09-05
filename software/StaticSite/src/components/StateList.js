import React, {useState} from "react";
import statesJson from "../states.json";

export const StateList = () => {
    statesJson = statesJson.map(s => {
        return {...s, "selected": false};
    });
    const [states, setStates] = useState(statesJson);

    const handleStateSelection = (id, selectionFlag) => {
        setStates(prevStates =>
            prevStates.map(state => {
                if (state.id === id) {
                    return {
                        ...state,
                        selected: selectionFlag
                    };
                }
                return state;
            })
        );
    };

    return (
        <>
            {states.map((state) =>
                <div key={state.name} className="row">
                    <div className="col">
                        <div className="btn-group dropend px-0 w-100">
                            <button type="button"
                                    className="btn btn-outline-dark dropdown-toggle text-wrap border-start-0 border-end-0 border-top-0"
                                    data-bs-toggle="dropdown">
                                {state.name}
                            </button>
                            <ul className="dropdown-menu">
                                <li className="dropdown-item">Action</li>
                                <li>
                                    <hr className="dropdown-divider"/>
                                </li>
                                <li><a className="dropdown-item" href="#">Something else
                                    here</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
};


const StateItem = ({state, onMouseOver}) => {
    const handleMouseEvent = (selectionFlag = true) => {
        if (typeof onMouseOver === "function") {
            onMouseOver(state.id, selectionFlag);
        }
    };

    return (
        <>{state.name}</>
    );
};

// <div onMouseOver={() => handleMouseEvent()}
//      onMouseOut={() => handleMouseEvent(false)}>
//     {state.name}
//     {state.selected &&
//         <ul>
//             {state.cities.map((city, index) =>
//                 <li key={`${city.name}-${index}`}>
//                     {city.name}
//                 </li>)}
//         </ul>
//     }
// </div>