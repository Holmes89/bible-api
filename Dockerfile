FROM java:8
VOLUME /tmp
# Maintainer
MAINTAINER "Joel Holmes <Holmes89@gmail.com>"
ADD target/bible-api*.jar bible-api.jar
RUN bash -c 'touch /bible-api.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bible-api.jar"]