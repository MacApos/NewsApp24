import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {fetchNews} from "../reducers/newsSlice";

const autocomplete = async (input) => {
    const google = window.google;
    const {AutocompleteSessionToken, AutocompleteSuggestion} = await google.maps.importLibrary("places");
    let request = {
        input,
        includedPrimaryTypes: ["locality"],
        includedRegionCodes: ["us"],
        language: "en-US",
    };

    request.sessionToken = new AutocompleteSessionToken();
    const {suggestions} = await AutocompleteSuggestion.fetchAutocompleteSuggestions(request);
    return suggestions.map(suggestion => {
        return suggestion.placePrediction.text.toString();
    });
};

export const Form = () => {
    const dispatch = useDispatch();

    const [query, setQuery] = useState("New York, NY, USA");
    const [propositions, setPropositions] = useState([]);

    useEffect(() => {
        (async () => {
            if (query.trim().length >= 3) {
                const cityPropositions = await autocomplete(query);
                setPropositions(() => cityPropositions);
            } else {
                setPropositions(() => []);
            }
        })();
    }, [query]);

    const handleSubmit = (e) => {
        e.preventDefault();
        dispatch(fetchNews(query));
    };

    return (
        <>
            <form onSubmit={handleSubmit}>
                <div className="row">
                    <div className="col pe-0">
                        <input className="form-control bg-secondary" type="search" placeholder="Search" value={query}
                               list="propositions" required minLength={3}
                               onChange={e => setQuery(e.target.value)}/>
                        <datalist id="propositions">
                            {propositions.length > 1 && propositions.map((proposition, index) =>
                                <option key={"proposition" + index} onMouseOver={() => console.log("click")}>
                                    {proposition}
                                </option>)}
                        </datalist>
                    </div>
                    <div className="col-auto">
                        <button className="btn btn-secondary" type="submit">Search</button>
                    </div>
                </div>
            </form>
        </>
    );
};