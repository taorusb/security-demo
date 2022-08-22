FROM openjdk:11.0.15
ENV SERVER_PORT=$SERVER_PORT
ENV TOP_VALUE_STOCKS_ENDPOINT=$TOP_VALUE_STOCKS_ENDPOINT
ENV CHANGED_VALUE_STOCKS_ENDPOINT=$CHANGED_VALUE_STOCKS_ENDPOINT
ENV GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID
ENV GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET
ENV DB_USER=$DB_USER
ENV DB_URL=$DB_URL
ENV DB_PORT=$DB_PORT
ENV DB_PASSWORD=$DB_PASSWORD
ENV DB_NAME=$DB_NAME
ENV DB_SCHEMA=$DB_SCHEMA
COPY . ./app

WORKDIR ./app
RUN chmod +x gradlew && ./gradlew build
WORKDIR ./build/libs/
ENTRYPOINT ["java", "-jar", "security-demo-0.0.1-SNAPSHOT.jar"]