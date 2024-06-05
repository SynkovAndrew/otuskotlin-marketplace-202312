import { createWebHistory, createRouter } from "vue-router";

const routes =  [
    {
        path: "/",
        alias: "/stocks",
        name: "stocks",
        component: () => import("./components/StockList")
    },
    {
        path: "/stock/:stockId/snapshots",
        name: "snapshots",
        component: () => import("./components/StockSnapshots.vue")
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;