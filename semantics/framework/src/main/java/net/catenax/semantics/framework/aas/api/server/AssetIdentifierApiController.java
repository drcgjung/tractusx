package net.catenax.semantics.framework.aas.api.server;

import net.catenax.semantics.framework.aas.api.proxy.AssetIdentifierApi;
import net.catenax.semantics.framework.aas.api.proxy.AssetIdentifierApiDelegate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-04T18:11:14.812382100+01:00[Europe/Berlin]")
@RestController
@RequestMapping("${openapi.semantics.base-path:/{idsOffer}/shells}")
public class AssetIdentifierApiController implements AssetIdentifierApi {

    private final AssetIdentifierApiDelegate delegate;

    @org.springframework.beans.factory.annotation.Autowired
    public AssetIdentifierApiController(AssetIdentifierApiDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public AssetIdentifierApiDelegate getDelegate() {
        return delegate;
    }
}
