FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q package -DskipTests

FROM tomcat:9.0-jdk17
ENV JAVA_OPTS="-Xmx256m -Xms128m"
COPY --from=build /build/target/epms.war /usr/local/tomcat/webapps/ROOT.war
COPY docker-entrypoint.sh /usr/local/bin/epms-entrypoint.sh
RUN chmod +x /usr/local/bin/epms-entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/usr/local/bin/epms-entrypoint.sh"]
