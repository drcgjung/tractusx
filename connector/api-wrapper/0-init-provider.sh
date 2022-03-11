#!/bin/bash

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

# Create and register Asset
curl -X POST -H 'X-Api-Key: 123456' -H 'Content-Type: application/json' --data "@resources/asset1.json" http://127.0.0.1:8181/api/assets
curl -X POST -H 'X-Api-Key: 123456' -H 'Content-Type: application/json' --data "@resources/asset2.json" http://127.0.0.1:8181/api/assets

# Create and register Contract Definition
curl -X POST -H 'X-Api-Key: 123456' -H 'Content-Type: application/json' --data "@resources/contractdefinition.json" http://127.0.0.1:8181/api/contractdefinitions
