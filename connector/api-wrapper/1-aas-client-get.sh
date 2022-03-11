#!/bin/bash

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

curl -X GET "http://localhost:8193/api/service/submodel/asset-1"
