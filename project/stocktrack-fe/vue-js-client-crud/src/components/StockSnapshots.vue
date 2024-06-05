<template>
  <div class="table-container">
<!--    <table class="styled-table">
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
    </table>-->
    <Bar v-if="loaded"
        id="my-chart-id"
        :options="chartOptions"
        :data="chartData"
    />
  </div>
</template>

<script>
import StockSnapshotService from "../services/StockSnapshotService";
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
  name: "stock-snapshots",
  components: { Bar },
  data() {
    return {
      snapshots: [],
      stockId: this.$route.params.stockId,
      currentSnapshot: null,
      currentIndex: -1,
      name: "",
      loaded: false,
      chartData: {
        labels: [],
        datasets: []
      },
      chartOptions: {
        responsive: true
      }
    };
  },
  methods: {
    findByStockId(stockId) {
      StockSnapshotService.findByStockId(stockId)
          .then(response => {
            this.snapshots = response.data.snapshots;

            const labels = []
            const data = []

            for (let i = 0; i < this.snapshots.length; i++) {
              labels.push(this.snapshots[i].timestamp)
              data.push(this.snapshots[i].value)
            }

            this.chartData.datasets = [
              {
                label: 'Data One',
                backgroundColor: '#f87979',
                data: data
              }
            ]
            this.chartData.labels = labels
            this.loaded = true

           // console.log(labels);
           // console.log(data);
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
    this.loaded = false
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