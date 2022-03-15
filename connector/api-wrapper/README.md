# EDC data plane & API-Wrapper

## Prerequisites

- EDC artifacts published on maven local at the commit `a0c4411dbfec1ee8298aecb04a5145d9419503f5` of the [EDC main branch](https://github.com/eclipse-dataspaceconnector/DataSpaceConnector)
  - Build & publish to maven local: `./gradlew clean build publishToMavenLocal -x test`
- Jdk 11
- Docker and Docker compose

## Run

```bash
./gradlew clean build
cd launchers
rm -rf adapter
rm -rf services
rm -rf aasproxy
ln -s ../../../semantics/adapter .
ln -s ../../../semantics/services .
ln -s ../../../semantics/aasproxy .
cd ..
docker-compose up --build

# Register assets & contract definition
./0-init-provider.sh

# Make a GET call through the EDCs or a POST
./1-aas-client-get.sh
./2-aas-client-post.sh
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
