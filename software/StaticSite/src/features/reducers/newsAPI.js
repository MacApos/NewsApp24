export class newsAPI {
    static async fetchNews(city) {
        try {
            const split = city.split(",");
            const result = await fetch(`http://localhost:5000/${split[0]}/${split[1]}`);
            return await result.json();
        } catch (err) {
            console.log(err);
        }
    }
}
