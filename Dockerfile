# 開発環境用 Dockerfile

FROM openjdk:17-jdk-slim

# Mavenをインストール
RUN apt-get update && apt-get install -y maven curl

# 作業ディレクトリを /app に設定
WORKDIR /app

# 依存関係をキャッシュするためにpom.xmlだけ先にコピー
COPY app/pom.xml ./

# 依存関係をインストール（オフラインモードの準備）
RUN mvn dependency:go-offline

# ソースコードをコピー
COPY app/src ./src

# 開発用のコマンド
CMD ["mvn", "spring-boot:run"]

# 必要であればポートを公開
EXPOSE 8080


# 本番環境 Dockerfile 本番環境では以下のコメントアウトを外す

## ビルド用のステージ
#FROM openjdk:17-jdk-slim AS build
#
## 必要なツールをインストール
#RUN apt-get update && apt-get install -y maven
#
## 作業ディレクトリを設定
#WORKDIR /app
#
## POM ファイルを先にコピー（キャッシュを活用）
#COPY app/pom.xml ./
#
## 依存関係を事前にダウンロード
#RUN mvn dependency:go-offline
#
## ソースコードをコピー（キャッシュが壊れない）
#COPY app/src ./src
#
## JAR ファイルをビルド
#RUN mvn clean install -DskipTests
#
## 実行用のステージ（軽量化）
#FROM openjdk:17-jre-slim
#
## 作業ディレクトリを設定
#WORKDIR /app
#
## ビルド済みの JAR ファイルをコピー
#COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar /app.jar
#
## アプリケーションを起動
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#
## ポート公開
#EXPOSE 8080
