<template>
  <div class="list row">
    <div class="col-md-8">
      <div class="input-group mb-3">
        <input type="text" class="form-control" placeholder="Search by ***"
               v-model="name"/>
        <div class="input-group-append">
          <button class="btn btn-outline-secondary" type="button"
                  @click="searchByName"
          >
            Search
          </button>
        </div>
      </div>
    </div>
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
    <div class="col-md-6">
      <div v-if="currentStock">
        <h4>Stock</h4>
        <div>
          <label><strong>Name:</strong></label> {{ currentStock.name }}
        </div>
        <div>
          <label><strong>Value:</strong></label> {{ currentStock.value }}
        </div>

        <a class="badge badge-warning"
           :href="'/stocks/' + currentStock.id"
        >
          Edit
        </a>
      </div>
      <div v-else>
        <br />
        <p>Please click on a Stock...</p>
      </div>
    </div>
  </div>
</template>

<script>
import StockService from "../services/StockService";

export default {
  name: "favourites",
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

<style>
.list {
  text-align: left;
  max-width: 750px;
  margin: auto;
}
</style>