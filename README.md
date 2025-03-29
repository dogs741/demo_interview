# demo_interview
Swagger-ui : http://localhost:8080/swagger-ui/index.html#/

requirements : java17 / docker / gradle
建置過程
1. 專案目錄底下 執行 ./gradlew bootJar
2. docker build --build-arg JAR_FILE=build/libs/demo-1.0.jar -t demo .
3. docker-compose up -d



# demo_interview
