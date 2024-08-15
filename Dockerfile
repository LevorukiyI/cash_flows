FROM maven:3.9.8-amazoncorretto-21 AS build
RUN yum install -y binutils

RUN mkdir /usr/src/project
COPY . /usr/src/project
WORKDIR /usr/src/project
RUN mvn package -DskipTests
RUN jar xf target/cash_flows-1.jar
RUN jdeps --ignore-missing-deps  \
    -q \
    --recursive \
    --multi-release 21 \
    --class-path 'BOOT-INF/lib/*'  \
    --print-module-deps target/cash_flows-1.jar > deps.info
RUN jlink --add-modules $(cat deps.info) \
    --strip-debug \
    --compress=2 \
    --no-header-files \
    --no-man-pages \
    --output /myjre
FROM debian:bookworm-slim as runtime
ENV JAVA_HOME=/user/java/jdk21
ENV PATH=$JAVA_HOME/bin:$PATH
COPY --from=build /myjre $JAVA_HOME
RUN mkdir /project/
COPY --from=build /usr/src/project/target/cash_flows-1.jar /project/
WORKDIR /project
ENTRYPOINT ["java", "-jar", "cash_flows-1.jar"]



