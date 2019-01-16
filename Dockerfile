FROM java:8
LABEL maintainer="sigitkurniawanisme@gmail.com"
EXPOSE 8080
ADD target/spring-boot-tax-calculator-0.0.1-SNAPSHOT.jar spring-boot-tax-calculator-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","spring-boot-tax-calculator-0.0.1-SNAPSHOT.jar"]
