FROM maven:3.8.5-openjdk-17

COPY . /usr/local/service
WORKDIR /usr/local/service


CMD ["mvn", "clean","package"]
