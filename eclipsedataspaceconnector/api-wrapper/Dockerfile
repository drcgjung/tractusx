FROM openjdk:11-jre-slim-buster
ARG JAR
ARG HTTP_PROXY

ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8090
ENV SPRING_PROFILES_ACTIVE local
ENV IDSADAPTER_SERVICEURL http://registry-service:4242
ENV IDSADAPTER_CONNECTORTYPE edc
ENV IDSADAPTER_CONNECTORPROTOCOL http
ENV IDSADAPTER_CONNECTORHOST provider-control-plane:9191
ENV IDSADAPTER_CONNECTORURL http://provider-control-plane:9191
ENV IDSADAPTER_CONNECTORUSER X-Api-Key
ENV IDSADAPTER_CONNECTORPASSWORD 123456
ENV AASPROXY_REGISTRYURL http://registry-service:4242
ENV AASPROXY_WRAPPERURL http://api-wrapper:9191/api/service

RUN if [[ -n "${HTTP_PROXY}"  ]]; then echo "Acquire::http::Proxy \"${HTTP_PROXY}\"" >> /etc/apt/apt.conf.d/proxy.conf; fi
RUN if [[ -n "${HTTP_PROXY}" ]]; then echo "Acquire::https::Proxy \"${HTTP_PROXY}\"" >> /etc/apt/apt.conf.d/proxy.conf; fi

RUN apt-get -y upgrade
RUN apt-get -y update
RUN apt-get -y install curl

RUN if [[ -n "${HTTP_PROXY}"  ]]; then rm -f /etc/apt/apt.conf.d/proxy.conf; fi

WORKDIR /app
COPY $JAR app.jar

EXPOSE 9191
EXPOSE 8090

# health status is determined by the availability of the /health endpoint
HEALTHCHECK --interval=5s --timeout=5s --retries=10 CMD curl --fail -X GET http://localhost:8181/api/check/health || exit 1

ENTRYPOINT java \
    -Didsadapter.serviceUrl=${IDSADAPTER_SERVICEURL}\
    -Didsadapter.connectorType=${IDSADAPTER_CONNECTORTYPE}\
    -Didsadapter.connectorProtocol=${IDSADAPTER_CONNECTORPROTOCOL}\
    -Didsadapter.connectorHost=${IDSADAPTER_CONNECTORHOST}\
    -Didsadapter.connectorUrl=${IDSADAPTER_CONNECTORURL}\
    -Didsadapter.connectorUser=${IDSADAPTER_CONNECTORUSER}\
    -Didsadapter.connectorPassword=${IDSADAPTER_CONNECTORPASSWORD}\
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}\
    -Daasproxy.registryUrl=${AASPROXY_REGISTRYURL}\
    -Daasproxy.wrapperUrl=${AASPROXY_WRAPPERURL}\
    -Djava.security.edg=file:/dev/.urandom \
    -Dedc.fs.config="configuration.properties"\
    -jar app.jar
