/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.aas.proxy;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap;

/**
 * A little memory storage to
 * help mapping ids to remote endpoints
 */
@Component
public class RewriteStorage {

    @RequiredArgsConstructor
    public static class ModelKey {
        final private String assetId;
        final private String submodelId;

        public String getAssetId() {
            return assetId;
        }

        public String getSubmodelId() {
            return submodelId;
        }

        @Override
        public int hashCode() {
            return assetId.hashCode()*submodelId.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if(other instanceof ModelKey) {
                ModelKey otherKey=(ModelKey) other;
                return assetId.equals(otherKey.assetId) && submodelId.equals(otherKey.submodelId);
            }
            return false;
        }
    }

    Map<ModelKey,String> urnMap=new HashMap<>();

    /**
     * ensure encoding
     * @param identifier to encode
     * @return encoded identifier
     */
    protected String urlEncode(String identifier) {
        return identifier.replace("#","%23").replace(":","%3A");
    }

    public synchronized String getEndpoint(String assetId, String submodelId) {
        assetId=urlEncode(assetId);
        submodelId=urlEncode(submodelId);
        return urnMap.get(new ModelKey(assetId,submodelId));
    }

    public synchronized ModelKey setEndpoint(String assetId, String submodelId, String endpoint) {
        assetId=urlEncode(assetId);
        submodelId=urlEncode(submodelId);
        ModelKey key=new ModelKey(assetId,submodelId);
        urnMap.put(key,endpoint);
        return key;
    }
}
