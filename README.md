StockTrack service

Modules:

1. Transport Rest
   - **models**: [stocktrack-api-v1-model-kotlin](project%2Fstocktrack-backend%2Fstocktrack-api-v1-model-kotlin)
   - **mapper**: [stocktrack-api-v1-mapper](project%2Fstocktrack-backend%2Fstocktrack-api-v1-mapper)
   - **generate models**: ./gradlew :project:stocktrack-backend:stocktrack-api-v1-model-kotlin:openApiGenerate
   - **tests**: 
     - ./gradlew :project:stocktrack-backend:stocktrack-api-v1-mapper:test
     - ./gradlew :project:stocktrack-test:stocktrack-test-e2e:test
2. Transport Database
   - **inmemory**: [stocktrack-repository-in-memory](project%2Fstocktrack-backend%2Fstocktrack-repository-in-memory)
   - **postgres**: [stocktrack-repository-postgresql](project%2Fstocktrack-backend%2Fstocktrack-repository-postgresql)
   - **tests**:
     - ./gradlew :project:stocktrack-backend:stocktrack-repository-in-memory:test
     - ./gradlew :project:stocktrack-backend:stocktrack-repository-postgresql:test
3. Business
   - adfsd
4. Framework
   - **ktor**: [stocktrack-bootstrap-ktor](project%2Fstocktrack-backend%2Fstocktrack-bootstrap-ktor)
   - **tests**: ./gradlew :project:stocktrack-test:stocktrack-test-integration-ktor:test
5. Run all the services
   - **backend**: docker-compose -f project/docker/docker-compose.yml up -d
   - **keycloak**: 
     - open http://localhost:8484/ and enter admin/admin
     - switch to stocktrack realm
     - go to Users and create one with credentials for next authentication
   - **frontend**: 
     - cd project/stocktrack-fe/vue-js-client-crud
     - npm run serve
     - open http://localhost:8080/ and enter just created user credentials
     - you can see the list of all the stocks
     - you can click on Snapshots "View" button and see value-time dependency

