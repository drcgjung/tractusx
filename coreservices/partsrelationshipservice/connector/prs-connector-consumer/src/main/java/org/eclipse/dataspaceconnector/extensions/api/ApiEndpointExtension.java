package org.eclipse.dataspaceconnector.extensions.api;

import org.eclipse.dataspaceconnector.extensions.job.InMemoryJobStore;
import org.eclipse.dataspaceconnector.extensions.job.JobOrchestrator;
import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.StatusCheckerRegistry;

import java.util.Set;

public class ApiEndpointExtension implements ServiceExtension {

    @Override
    public Set<String> requires() {
        return Set.of(
                "edc:webservice",
                "dataspaceconnector:transferprocessstore"
        );
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var monitor = context.getMonitor();

        var webService = context.getService(WebService.class);
        var processManager = context.getService(TransferProcessManager.class);
        var processStore = context.getService(TransferProcessStore.class);

        var orchestrator = new JobOrchestrator(processManager, new InMemoryJobStore());

        webService.registerController(new ConsumerApiController(context.getMonitor(), processManager, processStore, orchestrator));

        var statusCheckerReg = context.getService(StatusCheckerRegistry.class);
        statusCheckerReg.register("File", new FileStatusChecker(monitor));
    }
}
