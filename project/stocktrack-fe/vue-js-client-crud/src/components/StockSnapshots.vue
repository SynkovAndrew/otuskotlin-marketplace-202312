<template>
  <div class="table-container">
    <table class="styled-table">
      <thead>
      <tr>
        <th>Value</th>
        <th>Timestamp</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(snapshot, index) in snapshots" :key="index">
        <td :class="{ active: index == currentIndex }">{{ snapshot.value }}</td>
        <td :class="{ active: index == currentIndex }">{{ snapshot.timestamp }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import StockSnapshotService from "../services/StockSnapshotService";

export default {
  name: "stock-snapshots",
  data() {
    return {
      snapshots: [],
      stockId: this.$route.params.stockId,
      currentSnapshot: null,
      currentIndex: -1,
      name: ""
    };
  },
  methods: {
    findByStockId(stockId) {
      StockSnapshotService.findByStockId(stockId)
          .then(response => {
            this.snapshots = response.data.snapshots;
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    },

    refreshList() {
      this.find();
      this.currentSnapshot = null;
      this.currentIndex = -1;
    },

    setActiveSnapshot(snapshot, index) {
      this.currentSnapshot = snapshot;
      this.currentIndex = index;
    },
  },
  mounted() {
    this.findByStockId(this.stockId);
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