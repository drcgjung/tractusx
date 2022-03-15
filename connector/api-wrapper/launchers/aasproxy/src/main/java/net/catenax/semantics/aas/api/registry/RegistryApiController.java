package net.catenax.semantics.aas.api.registry;

import org.springframework.web.bind.annotation.RestController;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-07T08:24:07.248914900+01:00[Europe/Berlin]")
@RestController
public class RegistryApiController implements RegistryApi {

    private final RegistryApiDelegate delegate;

    @org.springframework.beans.factory.annotation.Autowired
    public RegistryApiController(RegistryApiDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public RegistryApiDelegate getDelegate() {
        return delegate;
    }
}
