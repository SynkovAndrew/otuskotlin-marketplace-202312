import { createWebHistory, createRouter } from "vue-router";

const routes =  [
    {
        path: "/auth-details",
        name: "login",
        component: () => import("./components/LoginView")
    },
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