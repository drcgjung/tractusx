#!/bin/bash

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

curl -X POST -H 'Content-Type: text/plain' --data "Hallo123" "http://localhost:8193/api/service/submodel/asset-2"
