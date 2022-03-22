package org.eclipse.dataspaceconnector.apiwrapper;

import okhttp3.OkHttpClient;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractOfferService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractNegotiationService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.HttpProxyService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.TransferProcessService;
import org.eclipse.dataspaceconnector.spi.WebService;
import org.eclipse.dataspaceconnector.spi.system.Inject;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.system.configuration.Config;

public class ApiWrapperExtension implements ServiceExtension {

    @Inject
    private WebService webService;

    @Inject
    private OkHttpClient httpClient;

    @Override
    public String name() {
        return "AAS-API-Wrapper";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var config = createApiWrapperConfig(context.getConfig());
        var monitor = context.getMonitor();
        var typeManager = context.getTypeManager();

        var contractOfferService = new ContractOfferService(monitor, typeManager, httpClient);
        var contractOfferRequestService = new ContractNegotiationService(monitor, typeManager, httpClient);
        var transferProcessService = new TransferProcessService(monitor, typeManager, httpClient);
        var httpProxyService = new HttpProxyService(monitor, httpClient);

        webService.registerResource(new ApiWrapperController(
                monitor,
                contractOfferService,
                contractOfferRequestService,
                transferProcessService,
                httpProxyService,
                config
        ));
    }

    private ApiWrapperConfig createApiWrapperConfig(Config config) {
        ApiWrapperConfig.Builder builder = ApiWrapperConfig.Builder.newInstance();

        builder.consumerEdcUrl(config.getString(ApiWrapperConfigKeys.CONSUMER_EDC_URL));

        var consumerEdcApiKeyName = config.getString(ApiWrapperConfigKeys.CONSUMER_EDC_APIKEY_NAME, null);
        if (consumerEdcApiKeyName != null) {
            builder.consumerEdcApiKeyName(consumerEdcApiKeyName);
        }

        var consumerEdcApiKeyValue = config.getString(ApiWrapperConfigKeys.CONSUMER_EDC_APIKEY_VALUE, null);
        if (consumerEdcApiKeyValue != null) {
            builder.consumerEdcApiKeyValue(consumerEdcApiKeyValue);
        }

        var basicAuthUsers = config.getConfig(ApiWrapperConfigKeys.BASIC_AUTH).getRelativeEntries();
        if (!basicAuthUsers.isEmpty()) {
            builder.basicAuthUsers(basicAuthUsers);
        }

        return builder.build();
    }
}
