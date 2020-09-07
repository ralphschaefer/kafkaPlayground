FROM cnfldemos/cp-server-connect-datagen:0.3.2-5.5.0

# WORKDIR /opt/service
COPY  mysql-connector-java-8.0.21.jar /usr/share/java/kafka-connect-jdbc
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-http:latest
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-rabbitmq:latest
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-cassandra:latest