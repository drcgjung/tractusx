package org.eclipse.dataspaceconnector.apiwrapper;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.model.NegotiationStatusResponse;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractNegotiationService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractOfferService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.HttpProxyService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.TransferProcessService;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.negotiation.ContractOfferRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.edr.EndpointDataReference;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Consumes()
@Produces({MediaType.APPLICATION_JSON})
@Path("/service")
public class ApiWrapperController {


    // Connection configurations
    private static final String IDS_PATH = "/api/v1/ids/data";
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("\\{\"data\":\"(?<embeddedData>.*)\"\\}");
    private final String consumerConnectorUrl;

    private final Monitor monitor;
    private final ContractOfferService contractOfferService;
    private final ContractNegotiationService contractNegotiationService;
    private final TransferProcessService transferProcessService;
    private final HttpProxyService httpProxyService;


    // In-memory state
    private final Map<String, EndpointDataReference> endpointDataReferences = new HashMap<>();
    private Map<String, String> header;

    public ApiWrapperController(Monitor monitor,
                                ContractOfferService contractOfferService,
                                ContractNegotiationService contractNegotiationService,
                                TransferProcessService transferProcessService,
                                HttpProxyService httpProxyService,
                                ApiWrapperConfig config) {
        this.monitor = monitor;
        this.contractOfferService = contractOfferService;
        this.contractNegotiationService = contractNegotiationService;
        this.transferProcessService = transferProcessService;
        this.httpProxyService = httpProxyService;

        this.consumerConnectorUrl = config.getConsumerEDCUrl();

        if (config.getConsumerEdcApiKeyValue() != null) {
            this.header = Collections.singletonMap(config.getConsumerEdcApiKeyName(), config.getConsumerEdcApiKeyValue());
        }
    }

    @GET
    @Path("/{assetId}/{subUrl:.+}")
    public String getWrapper(@QueryParam("provider-connector-url") String providerConnectorUrl, @PathParam("assetId") String assetId, @PathParam("subUrl") String subUrl, @Context UriInfo uriInfo) throws InterruptedException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        // Initialize and negotiate everything
        var agreementId = initializeContractNegotiation(providerConnectorUrl, assetId);

        // Initiate transfer process
        transferProcessService.initiateHttpProxyTransferProcess(
                agreementId,
                assetId,
                consumerConnectorUrl,
                providerConnectorUrl + IDS_PATH,
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
            data = this.httpProxyService.sendGETRequest(dataReference,subUrl,queryParams);
            Matcher dataMatcher=RESPONSE_PATTERN.matcher(data);
            while(dataMatcher.matches()) {
                data=dataMatcher.group("embeddedData");
                data=data.replace("\\\"","\"").replace("\\\\","\\");
                dataMatcher=RESPONSE_PATTERN.matcher(data);
            }
        } catch (IOException e) {
            monitor.severe("Call against consumer control plane failed!", e);
        }
        return data;
    }

    @POST
    @Path("/{assetId}/{subUrl:.+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postWrapper(@QueryParam("provider-connector-url") String providerConnectorUrl, @PathParam("assetId") String assetId, @PathParam("subUrl") String subUrl, String body, @Context UriInfo uriInfo) throws InterruptedException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        // Initialize and negotiate everything
        var agreementId = initializeContractNegotiation(providerConnectorUrl, assetId);

        // Initiate transfer process
        transferProcessService.initiateHttpProxyTransferProcess(
                agreementId,
                assetId,
                consumerConnectorUrl,
                providerConnectorUrl + IDS_PATH,
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
                    subUrl,
                    queryParams,
                    body,
                    Objects.requireNonNull(okhttp3.MediaType.parse("application/json"))
            );
            Matcher dataMatcher=RESPONSE_PATTERN.matcher(data);
            while(dataMatcher.matches()) {
                data=dataMatcher.group("embeddedData");
                data=data.replace("\\\"","\"").replace("\\\\","\\");
                dataMatcher=RESPONSE_PATTERN.matcher(data);
            }
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

    private String initializeContractNegotiation(String providerConnectorUrl, String assetId) throws InterruptedException {


        var contractOffer = contractOfferService.findContractOffer4AssetId(
                assetId,
                consumerConnectorUrl,
                providerConnectorUrl + IDS_PATH,
                header
        );

        // Initiate negotiation
        var contractOfferRequest = ContractOfferRequest.Builder.newInstance()
                .contractOffer(contractOffer)
                .connectorId("provider")
                .connectorAddress(providerConnectorUrl + IDS_PATH)
                .protocol("ids-multipart")
                .build();
        var negotiationId = contractNegotiationService.initiateNegotiation(
                contractOfferRequest,
                consumerConnectorUrl,
                header
        );

        // Check negotiation state
        NegotiationStatusResponse negotiationResponse = null;

        while (negotiationResponse == null || !Objects.equals(negotiationResponse.getStatus(), "CONFIRMED")) {
            Thread.sleep(1000);
            negotiationResponse = contractNegotiationService.getNegotiationState(
                    negotiationId,
                    consumerConnectorUrl,
                    header
            );
        }

        return negotiationResponse.getContractAgreementId();
    }
}
