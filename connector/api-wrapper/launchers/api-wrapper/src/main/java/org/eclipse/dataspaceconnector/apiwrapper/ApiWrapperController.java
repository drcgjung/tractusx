package org.eclipse.dataspaceconnector.apiwrapper;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractNegotiationService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractOfferService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.HttpProxyService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.TransferProcessService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.model.NegotiationStatusResponse;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.negotiation.ContractOfferRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.edr.EndpointDataReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Consumes()
@Produces({MediaType.APPLICATION_JSON})
@Path("/service")
public class ApiWrapperController {

    private final Monitor monitor;
    private final ContractOfferService contractOfferService;
    private final ContractNegotiationService contractNegotiationService;
    private final TransferProcessService transferProcessService;
    private final HttpProxyService httpProxyService;

    private final Map<String, String> header = Map.of("X-Api-Key", "123456");

    private final String consumerControlPlaneBaseUrl = "http://consumer-control-plane:9191/api";
    private final String providerControlPlaneIDSUrl = "http://provider-control-plane:9191/api/v1/ids/data";

    private final Map<String, EndpointDataReference> endpointDataReferences = new HashMap<>();

    public ApiWrapperController(Monitor monitor,
                                ContractOfferService contractOfferService,
                                ContractNegotiationService contractNegotiationService,
                                TransferProcessService transferProcessService, HttpProxyService httpProxyService) {
        this.monitor = monitor;
        this.contractOfferService = contractOfferService;
        this.contractNegotiationService = contractNegotiationService;
        this.transferProcessService = transferProcessService;
        this.httpProxyService = httpProxyService;
    }

    @GET
    @Path("/submodel/{assetId}")
    public String getWrapper(@PathParam("assetId") String assetId) throws InterruptedException {

        // Initialize and negotiate everything
        // TODO do this only if no agreement is already existing
        var agreementId = initializeContractNegotiation(assetId);

        // Initiate transfer process
        transferProcessService.initiateHttpProxyTransferProcess(
                agreementId,
                assetId,
                consumerControlPlaneBaseUrl,
                providerControlPlaneIDSUrl,
                header
        );

        EndpointDataReference dataReference = null;
        while (dataReference == null) {
            Thread.sleep(1000);
            dataReference = endpointDataReferences.get(agreementId);
        }

        // Get data through data plane
        String data = "";
        try {
            data = this.httpProxyService.sendGETRequest(dataReference);
        } catch (IOException e) {
            monitor.severe("Call against consumer control plane failed!", e);
        }
        return data;
    }

    @POST
    @Path("/submodel/{assetId}")
    public String postWrapper(@PathParam("assetId") String assetId) throws InterruptedException {

        // Initialize and negotiate everything
        // TODO do this only if no agreement is already existing
        var agreementId = initializeContractNegotiation(assetId);

        // Initiate transfer process
        transferProcessService.initiateHttpProxyTransferProcess(
                agreementId,
                assetId,
                consumerControlPlaneBaseUrl,
                providerControlPlaneIDSUrl,
                header
        );

        EndpointDataReference dataReference = null;
        while (dataReference == null) {
            Thread.sleep(1000);
            dataReference = endpointDataReferences.get(agreementId);
        }

        // Get data through data plane
        String data = "";
        try {
            data = this.httpProxyService.sendPOSTRequest(
                    dataReference,
                    "Random data",
                    Objects.requireNonNull(okhttp3.MediaType.parse("text/plain"))
            );
        } catch (IOException e) {
            monitor.severe("Call against consumer control plane failed!", e);
        }
        return data;
    }

    @POST
    @Path("/proxy-callback")
    public void pullData(EndpointDataReference dataReference) {
        endpointDataReferences.put(dataReference.getContractId(), dataReference);
        monitor.debug("Endpoint Data Reference received and stored for agreement: " + dataReference.getContractId());
    }

    private String initializeContractNegotiation(String assetId) throws InterruptedException {

        var contractOffer = contractOfferService.findContractOffer4AssetId(
                assetId,
                consumerControlPlaneBaseUrl,
                providerControlPlaneIDSUrl,
                header
        );

        // Initiate negotiation
        var contractOfferRequest = ContractOfferRequest.Builder.newInstance()
                .contractOffer(contractOffer)
                .connectorId("provider")
                .connectorAddress(providerControlPlaneIDSUrl)
                .protocol("ids-multipart")
                .build();
        var negotiationId = contractNegotiationService.initiateNegotiation(
                contractOfferRequest,
                consumerControlPlaneBaseUrl,
                header
        );

        // Check negotiation state
        NegotiationStatusResponse negotiationResponse = null;

        while (negotiationResponse == null || !Objects.equals(negotiationResponse.getStatus(), "CONFIRMED")) {
            Thread.sleep(1000);
            negotiationResponse = contractNegotiationService.getNegotiationState(
                    negotiationId,
                    consumerControlPlaneBaseUrl,
                    header
            );
        }

        return negotiationResponse.getContractAgreementId();
    }
}
