import http from "../http";

class StockService {
    findAll() {
        return http.get("/stocks");
    }
}

export default new StockService();