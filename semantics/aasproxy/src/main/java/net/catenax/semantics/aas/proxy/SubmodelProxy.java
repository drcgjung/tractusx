package net.catenax.semantics.aas.proxy;

import feign.Feign;
import lombok.RequiredArgsConstructor;
import net.catenax.semantics.framework.aas.api.proxy.AssetIdentifierApiDelegate;
import net.catenax.semantics.aas.api.shell.SubmodelInterfaceApi;
import net.catenax.semantics.framework.aas.model.Submodel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * The Submodel Repository Proxy implements the service layer behind the web protocol layer.
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
     * dynamically generate a delegate
     * @param assetIdentifier
     * @param submodelIdentifier
     * @return
     */
    @Override
    public ResponseEntity<Submodel> getSubmodelById(String assetIdentifier, String submodelIdentifier) {
        String endpoint=storage.getEndpoint(assetIdentifier,submodelIdentifier);
        SubmodelInterfaceApi api=builder.target(SubmodelInterfaceApi.class,endpoint);
        return ResponseEntity.ok(api.getSubmodel(Map.of()));
    }

    @Override
    public ResponseEntity<Submodel> getSubmodel(String assetIdentifier, String submodelIdentifier, String level, String content, String extent) {
        String endpoint=storage.getEndpoint(assetIdentifier,submodelIdentifier);
        SubmodelInterfaceApi api=builder.target(SubmodelInterfaceApi.class,endpoint);
        return ResponseEntity.ok(api.getSubmodel(level,content,extent));
    }
}
