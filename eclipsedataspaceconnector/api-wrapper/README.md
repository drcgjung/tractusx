<!---
Copyright (c) 2021-2022 ZF Friedrichshafen AG & T-Systems International GmbH (Catena-X Consortium)

See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
-->

# EDC data plane & API-Wrapper

## Prerequisites

### For Running
- If you are sitting behind a corporate proxy please set the environment variables ${HTTP_PROXY_HOST}, ${HTTP_PROXY_HOST} and ${HTTP_PROXY_PORT}
- Openssl
- Docker and Docker compose
- Azure CLI

### For Building
- EDC artifacts published on [EDC patch branch](https://github.com/drcgjung/DataSpaceConnector/tree/release/catena-x)
  - Build & publish to maven local: `./gradlew -Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTP_PROXY_HOST} -Dhttps.proxyPort=${HTTP_PROXY_PORT} clean build publishToMavenLocal -x test`
- Jdk 11
- Apache Maven

## Generate certificate

Currently, there is one certificate already provided. In order to generate the required pfx file to be put in to the vault properties,
you have to do the following:

```bash
mkdir certs
cd certs
# You have to enter an email (everything else can be empty)
openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem
# Give 123456 as password
openssl pkcs12 -inkey key.pem -in cert.pem -export -out cert.pfx
cd ..
```

## Pull (and Start)

First you need to decide whether you just want to use the prebuilt images or whether you want to
build by yourself. In the latter case, please go to Section 'Build (and Start)'.

Otherwise, you may utter the following commands (and then skip the next Section 'Build (and Start)' by advancing to section 'Play')

```bash
./run_local.sh
```

## Build (snd Start)

The following command will build (and start) Catena-X@Home

Please be sure that you setup the right certficates (see section 'Generate certificate').

```bash
./build_run_local.sh
```

## Play

Here are some sample commands to the API Wrapper 

```bash
# Register assets & contract definition
./0-init-provider.sh

# Make a GET call through the EDCs or a POST
./1-aas-client-get.sh
./2-aas-client-post.sh

# Make AAS calls through the EDCs 
./3-aas-submodel-get.sh
./4-aas-submodel-post.sh
```

More Interactions with the Catena-X components can be found in the [Postman Collection](../../catenax.postman_collection.json) using the
[Localhost Postman Environment](../../catenax.localhost.postman_environment.json). Both need to be imported into [Postman](http://postman.com).

The most important sections therein would be Catenax>Semantic Layer>AAS Proxy (Data Consumer Side) and Catena-X>Semantic Layer>Semantic Adapter (Data Provider Side).

## Push and Deploy

After successful build & run, the artifacts may be pushed to the 'Hotel Budapest' environment.

```bash
./deploy_catenax.sh
```

Helm Chart deployment & Postman Environment for Hotel Budapest upcoming ...

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

