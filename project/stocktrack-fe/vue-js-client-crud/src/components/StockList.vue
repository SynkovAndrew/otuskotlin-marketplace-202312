<template>
  <!--  <div class="row">
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
    </div>-->
  <div class="table-container">
    <table class="styled-table">
      <thead>
      <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Category</th>
        <th>Snapshots</th>
      </tr>
      </thead>
      <tbody>
      <tr :class="{ active: index == currentIndex }" v-for="(stock, index) in stocks" :key="index">
        <td>{{ stock.id.value }}</td>
        <td>{{ stock.name }}</td>
        <td>{{ stock.category }}</td>
        <td>
          <router-link :to="`/stock/${stock.id.value}/snapshots`" class="btn btn-outline-info mx-1">View</router-link>
        </td>
      </tr>
      </tbody>
    </table>
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
    find() {
      StockService.find()
          .then(response => {
            this.stocks = response.data.body;
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    },

    refreshList() {
      this.find();
      this.currentStock = null;
      this.currentIndex = -1;
    },

    setActiveStock(stock, index) {
      this.currentStock = stock;
      this.currentIndex = index;
    },
  },
  mounted() {
    this.find();
  }
};
</script>
<style scoped>
.table-container {
  width: 100%;
  margin: 20px 0;
  overflow-x: auto;
}

.styled-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 18px;
  text-align: left;
}

.styled-table thead tr {
  background-color: #2c3e50;
  color: #ffffff;
  text-align: left;
}

.styled-table th,
.styled-table td {
  padding: 12px 15px;
}

.styled-table tbody tr {
  border-bottom: 1px solid #dddddd;
}

.styled-table tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
  border-bottom: 2px solid #2c3e50;
}

.styled-table tbody tr:hover {
  background-color: #f1f1f1;
  cursor: pointer;
}
</style>