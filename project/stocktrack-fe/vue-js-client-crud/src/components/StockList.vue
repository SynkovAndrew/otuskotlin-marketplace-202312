<template>
  <div class="row">
    <div class="col-md-6">
      <ul class="list-group">
        <li class="list-group-item"
            :class="{ active: index == currentIndex }"
            v-for="(stock, index) in stocks"
            :key="index"
            @click="setActiveStock(stock, index)"
        >
          {{ stock.name }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import StockService from "../services/StockService";

export default {
  name: "stock-list",
  data() {
    return {
      stocks: [],
      currentStock: null,
      currentIndex: -1,
      name: ""
    };
  },
  methods: {
    findAllStocks() {
      StockService.findAll()
          .then(response => {
            this.stocks = response.data;
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    },

    refreshList() {
      this.findAllStocks();
      this.currentStock = null;
      this.currentIndex = -1;
    },

    setActiveStock(stock, index) {
      this.currentStock = stock;
      this.currentIndex = index;
    },

    searchByName() {
      StockService.findAll()
          .then(response => {
            this.stocks = response.data;
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    }
  },
  mounted() {
    this.findAllStocks();
  }
};
</script>
