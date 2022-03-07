package net.catenax.semantics.adapter;

import net.catenax.semantics.framework.aas.api.proxy.AssetIdentifierApiDelegate;
import net.catenax.semantics.framework.aas.model.*;
import net.catenax.semantics.framework.BaseAdapter;
import net.catenax.semantics.framework.IdsConnector;
import net.catenax.semantics.framework.config.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * An adapter for the AAS API
 */
@Service
public class AASAdapter extends BaseAdapter implements AssetIdentifierApiDelegate {

    public AASAdapter(Config configurationData, IdsConnector connector) {
        super(configurationData);
        setIdsConnector(connector);
    }

    @Override
    public ResponseEntity<Submodel> getSubmodel(String assetIdentifier, String submodelIdentifier, String level, String content, String extent) {
        Submodel model=new Submodel();
        LangString desc=new LangString();
        desc.setLanguage("EN_US");
        desc.setText("Sample Catena-X Submodel Implementation");
        model.setDescription(List.of(desc));
        LangString disp=new LangString();
        disp.setLanguage("EN_US");
        disp.setText("Catena-X Sample Submodel");
        model.setDisplayName(List.of(disp));
        model.setCategory("AAS;BAMM;SQL");
        model.setIdShort("sql-traceability");
        model.setIdentification(submodelIdentifier);
        model.setKind(ModelingKind.TEMPLATE);
        ModelType modelType=new ModelType();
        modelType.setName(ModelTypes.ENTITY);
        model.setModelType(modelType);
        GlobalReference reference=new GlobalReference();
        reference.setValue(List.of("urn:bamm:com.catenaX:0.0.1#Traceability"));
        model.setSemanticId(reference);
        return ResponseEntity.ok(model);
    }
}
