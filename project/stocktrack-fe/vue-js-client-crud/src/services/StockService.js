import Http from "../http";

class StockService {
    find() {
        return Http.getAxiosClient().post(
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