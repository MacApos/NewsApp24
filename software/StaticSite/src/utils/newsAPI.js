import {API_URI, TRENDING} from "../constants/constants";

export class newsAPI {
    static async fetchNews(city) {
        try {
            if (city === "") {
                return;
            }
            sessionStorage.setItem("cityName", city);
            const pathVariable = city === TRENDING ? TRENDING.toLowerCase():
                city.split(",")
                .map(s => s.trim())
                .slice(0, 2)
                .join("/");

            const response = await fetch(API_URI + pathVariable);
            if (response.ok) {
                return response;
            }
        } catch (err) {
            console.warn(err);
        }
    }
}
