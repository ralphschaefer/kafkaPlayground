FROM openjdk:11
WORKDIR /opt/service
COPY target/scala-2.13/simpleREST.jar .
# RUN java -jar /opt/service/simpleREST.jar
CMD ["java", "-jar", "/opt/service/simpleREST.jar"]