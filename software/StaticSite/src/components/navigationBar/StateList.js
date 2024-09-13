import React from "react";
import {STATES} from "../../constants/constants";
import {useDispatch} from "react-redux";
import {fetchNews} from "../../reducers/newsSlice";

export const StateList = () => {
    return (
        <>
            {STATES.map((state) =>
                <div key={state.name} className="row">
                    <div className="col">
                        <div className="btn-group dropend w-100">
                            <button className="btn btn-outline-dark dropdown-toggle
                            text-wrap border-start-0 border-end-0 border-top-0"
                                    data-bs-toggle="dropdown">
                                {state.name}
                            </button>
                            <StateItem state={state}/>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
};

const StateItem = ({state}) => {
    const dispatch = useDispatch();
    const cities = state.cities;
    return (
        <ul className="dropdown-menu">
            {cities.sort((a, b) => a.capital > b.capital ? -1 : a.population < b.population ? 1 : 0)
                .map(city => {
                        const cityName = city.name;
                        const cityKey = cityName + "," + state.id;
                        return (
                            <div key={cityKey}>
                                <li className="dropdown-item"
                                    onClick={() => dispatch(fetchNews(cityKey))}>
                                    <button className="btn btn-outline-dark text-start">
                                        {cityName}
                                    </button>
                                </li>
                                {cities.length > 1 && city.capital &&
                                    <li>
                                        <hr className="dropdown-divider my-0"/>
                                    </li>}
                            </div>);
                    }
                )}
        </ul>


    );
};