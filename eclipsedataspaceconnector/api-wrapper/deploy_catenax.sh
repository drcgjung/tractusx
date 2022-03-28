#
# Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

#
# Shell script to build and run Catena-X@Home for testing purposes.
#
# Prerequisites:
#   Windows, (git)-bash shell, java 11 (java) and maven (mvn) in the $PATH.
#
# Synposis:
#   ./deploy_catenax.sh
#
# Comments:
#

az login --tenant 495463c3-0991-4659-9cc5-94b4a3f7b1d6
az acr login --name cxtsiacr.azurecr.io
docker push cxtsiacr.azurecr.io/edc/consumer-control-plane:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/edc/consumer-data-plane:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/edc/consumer-api-wrapper:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/edc/provider-control-plane:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/edc/provider-data-plane:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/edc/provider-api-wrapper:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/backend/simple-aas-adapter:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/semantics/services:feature-ART3-305-rohrputzer-support-latest
docker push cxtsiacr.azurecr.io/edc/consumer-aas-proxy:feature-ART3-305-rohrputzer-support-latest
