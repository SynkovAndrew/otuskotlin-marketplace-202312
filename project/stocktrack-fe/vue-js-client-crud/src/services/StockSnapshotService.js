import http from "../http";

class StockSnapshotService {
    findByStockId(stockId) {
        return http.post(
            "/snapshot/find",
            {
                "requestType": "find_snapshot",
                "stockId": stockId
            }
        );
    }
}

export default new StockSnapshotService();