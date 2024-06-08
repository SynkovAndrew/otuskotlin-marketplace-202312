import Http from "../http";

class StockSnapshotService {
    findByStockId(stockId) {
        return Http.getAxiosClient().post(
            "/snapshot/find",
            {
                "requestType": "find_snapshot",
                "stockId": stockId
            }
        );
    }
}

export default new StockSnapshotService();