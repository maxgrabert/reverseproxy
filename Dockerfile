FROM adoptopenjdk:11-jre-openj9

RUN mkdir -p /opt/grabert/

COPY target/reverseproxy-*.jar /opt/grabert/reverseproxy.jar

CMD ["java", "-jar", "/opt/grabert/reverseproxy.jar"]
