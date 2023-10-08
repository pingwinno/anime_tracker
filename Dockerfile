FROM eclipse-temurin:17-alpine

COPY . /tmp
RUN mkdir /opt/app
WORKDIR /tmp
RUN ./mvnw package && rm -rf ~/.m2  && mv target/anime_downloader_bot-0.0.1-SNAPSHOT.jar /opt/app/anime-downloader.jar \
 && cd / && rm -rf /tmp/*

CMD ["java", "-jar", "/opt/app/anime-downloader.jar"]