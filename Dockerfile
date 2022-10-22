FROM openjdk:11.0.15
ARG SERVER_PORT
ENV SERVER_PORT=$SERVER_PORT
ARG TOP_VALUE_STOCKS_ENDPOINT
ENV TOP_VALUE_STOCKS_ENDPOINT=$TOP_VALUE_STOCKS_ENDPOINT
ARG CHANGED_VALUE_STOCKS_ENDPOINT
ENV CHANGED_VALUE_STOCKS_ENDPOINT=$CHANGED_VALUE_STOCKS_ENDPOINT
ARG GOOGLE_CLIENT_ID
ENV GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ENV GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET
ARG DB_USER
ENV DB_USER=$DB_USER
ARG DB_URL
ENV DB_URL=$DB_URL
ARG DB_PORT
ENV DB_PORT=$DB_PORT
ARG DB_PASSWORD
ENV DB_PASSWORD=$DB_PASSWORD
ARG DB_NAME
ENV DB_NAME=$DB_NAME
ARG DB_SCHEMA
ENV DB_SCHEMA=$DB_SCHEMA
COPY . ./app

WORKDIR ./app
RUN chmod +x gradlew && ./gradlew build
WORKDIR ./build/libs/
ENTRYPOINT ["java", "-jar", "security-demo-0.0.1-SNAPSHOT.jar"]