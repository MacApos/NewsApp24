import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {newsFetched, selectNews} from "../features/reducers/newsSlice";

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
        console.log(suggestion.placePrediction.text.toString());
        return suggestion.placePrediction.text.toString();
    });
};

export const Form = () => {
    const dispatch = useDispatch();
    const news = useSelector(selectNews);

    const [query, setQuery] = useState("");
    const [propositions, setPropositions] = useState([]);

    useEffect(() => {
        (async () => {
            if (query.trim().length >= 3) {
                const cityPropositions = await autocomplete(query);
                console.log(propositions);
                setPropositions(() => cityPropositions);
            } else {
                setPropositions(() => []);
            }
        })();
    }, [query]);

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            const resul = await dispatch(newsFetched(query));
            console.log(query);
        } catch (err) {
            console.warn(err);
        }
    };

    return (
        <>
            <form onSubmit={handleSubmit}>
                <input type={"text"} value={query} list="propositions"
                       onChange={e => setQuery(e.target.value)}/>
                <datalist id="propositions">
                    {propositions.map(proposition => <option>{proposition}</option>)}
                </datalist>
                <input type={"submit"} value={"Search"}/>
            </form>
        </>

    );
};