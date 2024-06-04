import http from "../http";

class StockService {
    find() {
        return http.post(
            "/stock/search",
            {
                "requestType": "search",
                "debug": {
                    "mode": "prod",
                    "stub": "success"
                },
                "filter": {
                    "searchString": ""
                }
            }
        );
    }
}

export default new StockService();