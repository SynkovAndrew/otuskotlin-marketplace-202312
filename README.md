StockTrack service

Modules:

1. Transport Rest
   - **models**: [stocktrack-api-v1-model-kotlin](project%2Fstocktrack-backend%2Fstocktrack-api-v1-model-kotlin)
   - **mapper**: [stocktrack-api-v1-mapper](project%2Fstocktrack-backend%2Fstocktrack-api-v1-mapper)
   - **generate models**: ./gradlew :project:stocktrack-backend:stocktrack-api-v1-model-kotlin:openApiGenerate
   - **tests**: ./gradlew :project:stocktrack-backend:stocktrack-api-v1-mapper:test
2. Transport Database
   - **inmemory**: [stocktrack-repository-in-memory](project%2Fstocktrack-backend%2Fstocktrack-repository-in-memory)
   - **postgres**: [stocktrack-repository-postgresql](project%2Fstocktrack-backend%2Fstocktrack-repository-postgresql)
   - **tests**:
     - ./gradlew :project:stocktrack-backend:stocktrack-repository-in-memory:test
     - ./gradlew :project:stocktrack-backend:stocktrack-repository-postgresql:test
3. Framework
   - **ktor**: [stocktrack-bootstrap-ktor](project%2Fstocktrack-backend%2Fstocktrack-bootstrap-ktor)
   - **tests**: ./gradlew :project:stocktrack-test:stocktrack-test-integration-ktor:test
4. Monitoring

