# demo_interview
Swagger-ui : http://localhost:8080/swagger-ui/index.html#/

requirements : java17 / docker / gradle
建置過程
1. 專案目錄底下 執行 ./gradlew bootJar
2. docker build --build-arg JAR_FILE=build/libs/demo-1.0.jar -t demo .
3. docker-compose up -d

專案使用技術
1. Spring Boot
2. Mybatis (MySQL)
3. Redis
4. Kafka
5. Swagger UI
6. 反射
7. Stream (Java8)


# demo_interview
