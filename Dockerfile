FROM openjdk:11.0.3

ENV ELASTIC_APM_VERSION 1.7.0

RUN wget http://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/$ELASTIC_APM_VERSION/elastic-apm-agent-$ELASTIC_APM_VERSION.jar
RUN mv elastic-apm-agent-$ELASTIC_APM_VERSION.jar elastic-apm-agent.jar

VOLUME /tmp

ADD target/*.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /app.jar" ]