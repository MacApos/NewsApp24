export class newsAPI {
    static async fetchNews(city) {
        try {
            let pathVariable = city;
            if (city !== "") {
                const split = city.split(",").map(s => s.trim());
                pathVariable = split.slice(0,2).join("/");
            }

            const response = await fetch(`http://localhost:5000/${pathVariable}`);
            if (response.ok) {
                return response;
            }
        } catch (err) {
            console.warn(err);
        }
    }
}
