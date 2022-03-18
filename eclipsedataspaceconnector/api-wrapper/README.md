# EDC data plane & API-Wrapper

## Prerequisites

- If you build behind a corporate proxy please set the environment variables ${HTTP_PROXY_HOST}, ${HTTP_PROXY_HOST} and ${HTTP_PROXY_PORT}
- EDC artifacts published on [EDC patch branch](https://github.com/drcgjung/DataSpaceConnector/tree/release/catena-x)
  - Build & publish to maven local: `./gradlew -Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTP_PROXY_HOST} -Dhttps.proxyPort=${HTTP_PROXY_PORT} clean build publishToMavenLocal -x test`
- Jdk 11
- Docker and Docker compose

## Run

```bash
export MAVEN_OPTS="-Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTP_PROXY_HOST} -Dhttps.proxyPort=${HTTP_PROXY_PORT}"
cd ../../semantics
mvn clean install -DskipTests
cd ../eclipsedataspaceconnector/api-wrapper
./gradlew -Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTP_PROXY_HOST} -Dhttps.proxyPort=${HTTP_PROXY_PORT} clean build
cd launchers
rm -rf adapter
rm -rf services
ln -s ../../../semantics/adapter .
ln -s ../../../semantics/services .
cd aasproxy
mvn clean install -DskipTests
cd ../..
docker-compose up --build

# Register assets & contract definition
./0-init-provider.sh

# Make a GET call through the EDCs or a POST
./1-aas-client-get.sh
./2-aas-client-post.sh

# Make AAS calls through the EDCs 
./3-aas-submodel-get.sh
./4-aas-submodel-post.sh

```

## Open Questions

- Call vom AAS-Client zum API-Wrapper zum Daten holen (`current.png` Call 23), oder Callback Mechanismus?
- Wie kommen wir an den "richtigen" Contract? (`planned.png` Call 13, `current.png` Call 10)
  - Wonach sollen wir suchen? Welches Attribut/Property?

## Mögliche Komplikationen

- Initialer Call von AAS-Client zum API-Wrapper kann unter Umständen in ein Timeout laufen.
  - Beispiel:
    - Wenn EDCs zu lange brauchen
    - Contract manuell genehmigt werden muss
  - Lösung: Asynchronität & State-Machine
- Nur eine `/proxy-callback` URL pro Connector!
  - Doof, wenn mehrere Backend-Data-Services den Proxy-Flow verwenden wollen.
- Token zum Abrufen der Daten läuft nach einer gewissen Zeit ab

### Generate certificate

Currently, there is one certificates already provided. If you want to create an own you have to do the following:

```bash
mkdir certs
cd certs
# You have to enter an email (everything else can be empty)
openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem
# Give 123456 as password
openssl pkcs12 -inkey key.pem -in cert.pem -export -out cert.pfx
cd ..
```

Then put the cert.pem content into the vault property files.
