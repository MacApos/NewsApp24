import {TRENDING} from "../constants/constants";

export class newsAPI {
    static async fetchNews(city) {
        try {
            if (city === "") {
                return;
            }
            let pathVariable;
            if (city === TRENDING) {
                pathVariable = TRENDING.toLowerCase();
            } else {
                const split = city.split(",").map(s => s.trim());
                pathVariable = split.slice(0, 2).join("/");
            }
            sessionStorage.setItem("cityName", city);
            const response = await fetch(`http://localhost:5000/${pathVariable}`);
            if (response.ok) {
                return response;
            }
        } catch (err) {
            console.warn(err);
        }
    }
}
