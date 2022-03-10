package net.catenax.semantics.aas.proxy;

import feign.Feign;
import lombok.RequiredArgsConstructor;
import net.catenax.semantics.aas.api.shell.SubmodelInterfaceApi;
import net.catenax.semantics.framework.aas.api.proxy.AssetIdentifierApiDelegate;
import net.catenax.semantics.framework.aas.model.OperationRequest;
import net.catenax.semantics.framework.aas.model.OperationResult;
import net.catenax.semantics.framework.aas.model.Submodel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * The Submodel Repository Proxy implements the service layer behind the web protocol layer.
 * TODO use the EDC API Wrapper
 */
@Service
@RequiredArgsConstructor
public class SubmodelProxy implements AssetIdentifierApiDelegate {
    /**
     * we got a rewrite storage for endpoints
     */
    protected final RewriteStorage storage;

    /**
     * we need to build delegates on the fly
     */
    protected final Feign.Builder builder;

    /**
     * create a client based on the
     * actual endpoint
     * @param assetIdentifier identifier of the twin
     * @param submodelIdentifier identifier of the representation/aspect
     * @return client pointing to the correct original url
     */
    private SubmodelInterfaceApi getSubmodelInterfaceApi(String assetIdentifier, String submodelIdentifier) {
        String endpoint=storage.getEndpoint(assetIdentifier, submodelIdentifier);
        SubmodelInterfaceApi api=builder.target(SubmodelInterfaceApi.class,endpoint);
        return api;
    }

    /**
     * dynamically generate a delegate
     * @param idsOffer is ignored
     * @param assetIdentifier
     * @param submodelIdentifier
     * @return final submodel result
     */
    @Override
    public ResponseEntity<Submodel> getSubmodelById(String idsOffer, String assetIdentifier, String submodelIdentifier) {
        SubmodelInterfaceApi api = getSubmodelInterfaceApi(assetIdentifier, submodelIdentifier);
        return ResponseEntity.ok(api.getSubmodel(Map.of()));
    }


    /**
     * dynamically generate a delegate
     * @oaram idsOffer is ignored
     * @param assetIdentifier identifier of the twin
     * @param submodelIdentifier identifier of the aspect
     * @return final submodel result
     */
    @Override
    public ResponseEntity<Submodel> getSubmodel(String idsOffer,String assetIdentifier, String submodelIdentifier, String level, String content, String extent) {
        SubmodelInterfaceApi api = getSubmodelInterfaceApi(assetIdentifier, submodelIdentifier);
        return ResponseEntity.ok(api.getSubmodel(level,content,extent));
    }

    /**
     * dynamically generate a delegate
     * @param idsOffer is ignored
     * @param assetIdentifier identifier of the twin
     * @param submodelIdentifier identifier of the aspect
     * @param idShortPath name of the operation
     * @param handleId handle of the call
     * @param content how the result should be presented
     * @return operation result
     */
    @Override
    public ResponseEntity<OperationResult> getOperationAsyncResult(String idsOffer,String assetIdentifier, String submodelIdentifier, String idShortPath, String handleId, String content) {
        SubmodelInterfaceApi api = getSubmodelInterfaceApi(assetIdentifier, submodelIdentifier);
        return ResponseEntity.ok(api.getOperationAsyncResult(idShortPath,handleId,content));
    }

    /**
     * dynamically generate a delegate
     * @param idsOffer is ignored
     * @param assetIdentifier identifier of the twin
     * @param submodelIdentifier identifier of the aspect
     * @param idShortPath name of the operation
     * @param body request body for the operation
     * @param async whether it should be executed asynchronously
     * @param content how the result should be presented
     * @return operation result
     */
    @Override
    public ResponseEntity<OperationResult> invokeOperation(String idsOffer, String assetIdentifier, String submodelIdentifier, String idShortPath, OperationRequest body, Boolean async, String content) {
        SubmodelInterfaceApi api = getSubmodelInterfaceApi(assetIdentifier, submodelIdentifier);
        return ResponseEntity.ok(api.invokeOperation(body,idShortPath, async, content));
    }
}
