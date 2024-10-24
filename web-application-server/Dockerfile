# 빌드 이미지로 Amazon Corretto 21 JDK 이미지를 사용
FROM amazoncorretto:21 AS build

# 소스코드를 복사할 작업 디렉토리
WORKDIR /app

# Gradle 설정 파일들 복사
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle

# Gradle로 종속성을 먼저 설치
RUN ./gradlew dependencies --no-daemon

# 호스트 머신의 소스코드를 작업 디렉토리로 복사
COPY . /app

# Gradle 빌드를 실행하여 JAR 파일 생성
RUN ./gradlew clean build --no-daemon

# 런타임 이미지로 Amazon Corretto 21 지정 (Slim 버전 사용 가능)
FROM amazoncorretto:21-alpine

# 애플리케이션을 실행할 작업 디렉토리
WORKDIR /app

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사
COPY --from=build /app/build/libs/*.jar /app/web-application-server.jar

# 8080 포트 노출
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java"]
CMD ["-jar", "web-application-server.jar"]
