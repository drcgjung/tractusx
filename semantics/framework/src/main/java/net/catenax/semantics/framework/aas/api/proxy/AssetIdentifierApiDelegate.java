package net.catenax.semantics.framework.aas.api.proxy;

import net.catenax.semantics.framework.aas.model.OperationRequest;
import net.catenax.semantics.framework.aas.model.OperationResult;
import net.catenax.semantics.framework.aas.model.Submodel;
import net.catenax.semantics.framework.aas.model.SubmodelElement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * A delegate to be called by the {@link AssetIdentifierApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-04T18:11:14.812382100+01:00[Europe/Berlin]")
public interface AssetIdentifierApiDelegate {

    Logger log = LoggerFactory.getLogger(AssetIdentifierApi.class);

    default Optional<ObjectMapper> getObjectMapper(){
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest(){
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    /**
     * @see AssetIdentifierApi#deleteSubmodelById
     */
    default ResponseEntity<Void> deleteSubmodelById( String  assetIdentifier,
         String  submodelIdentifier) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#deleteSubmodelElementByPath
     */
    default ResponseEntity<Void> deleteSubmodelElementByPath( String  assetIdentifier,
         String  submodelIdentifier,
         String  idShortPath) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#getAllSubmodelElements
     */
    default ResponseEntity<List<SubmodelElement>> getAllSubmodelElements( String  assetIdentifier,
         String  submodelIdentifier,
         String  level,
         String  content,
         String  extent) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("[ \"\", \"\" ]", List.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#getOperationAsyncResult
     */
    default ResponseEntity<OperationResult> getOperationAsyncResult( String  assetIdentifier,
         String  submodelIdentifier,
         String  idShortPath,
         String  handleId,
         String  content) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{\r\n  \"outputArguments\" : [ null, null ],\r\n  \"requestId\" : \"requestId\",\r\n  \"executionResult\" : {\r\n    \"success\" : true,\r\n    \"messages\" : [ {\r\n      \"code\" : \"code\",\r\n      \"messageType\" : \"Undefined\",\r\n      \"text\" : \"text\",\r\n      \"timestamp\" : \"timestamp\"\r\n    }, {\r\n      \"code\" : \"code\",\r\n      \"messageType\" : \"Undefined\",\r\n      \"text\" : \"text\",\r\n      \"timestamp\" : \"timestamp\"\r\n    } ]\r\n  },\r\n  \"executionState\" : \"Initiated\",\r\n  \"inoutputArguments\" : [ {\r\n    \"value\" : \"\"\r\n  }, {\r\n    \"value\" : \"\"\r\n  } ]\r\n}", OperationResult.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#getSubmodel
     */
    default ResponseEntity<Submodel> getSubmodel( String  assetIdentifier,
         String  submodelIdentifier,
         String  level,
         String  content,
         String  extent) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", Submodel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#getSubmodelById
     */
    default ResponseEntity<Submodel> getSubmodelById( String  assetIdentifier,
         String  submodelIdentifier) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", Submodel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#getSubmodelElementByPath
     */
    default ResponseEntity<SubmodelElement> getSubmodelElementByPath( String  assetIdentifier,
         String  submodelIdentifier,
         String  idShortPath,
         String  level,
         String  content,
         String  extent) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", SubmodelElement.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#invokeOperation
     */
    default ResponseEntity<OperationResult> invokeOperation( String  assetIdentifier,
         String  submodelIdentifier,
         String  idShortPath,
         OperationRequest  body,
         Boolean  async,
         String  content) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{\r\n  \"outputArguments\" : [ null, null ],\r\n  \"requestId\" : \"requestId\",\r\n  \"executionResult\" : {\r\n    \"success\" : true,\r\n    \"messages\" : [ {\r\n      \"code\" : \"code\",\r\n      \"messageType\" : \"Undefined\",\r\n      \"text\" : \"text\",\r\n      \"timestamp\" : \"timestamp\"\r\n    }, {\r\n      \"code\" : \"code\",\r\n      \"messageType\" : \"Undefined\",\r\n      \"text\" : \"text\",\r\n      \"timestamp\" : \"timestamp\"\r\n    } ]\r\n  },\r\n  \"executionState\" : \"Initiated\",\r\n  \"inoutputArguments\" : [ {\r\n    \"value\" : \"\"\r\n  }, {\r\n    \"value\" : \"\"\r\n  } ]\r\n}", OperationResult.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#postSubmodelElement
     */
    default ResponseEntity<SubmodelElement> postSubmodelElement( String  assetIdentifier,
         String  submodelIdentifier,
         SubmodelElement  body,
         String  level,
         String  content,
         String  extent) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", SubmodelElement.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#postSubmodelElementByPath
     */
    default ResponseEntity<SubmodelElement> postSubmodelElementByPath( String  assetIdentifier,
         String  submodelIdentifier,
         String  idShortPath,
         SubmodelElement  body,
         String  level,
         String  content,
         String  extent) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", SubmodelElement.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#putSubmodel
     */
    default ResponseEntity<Void> putSubmodel( String  assetIdentifier,
         String  submodelIdentifier,
         Submodel  body,
         String  level,
         String  content,
         String  extent) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#putSubmodelById
     */
    default ResponseEntity<Submodel> putSubmodelById( String  assetIdentifier,
         String  submodelIdentifier,
         Submodel  body) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", Submodel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @see AssetIdentifierApi#putSubmodelElementByPath
     */
    default ResponseEntity<Void> putSubmodelElementByPath( String  assetIdentifier,
         String  submodelIdentifier,
         String  idShortPath,
         SubmodelElement  body,
         String  level,
         String  content,
         String  extent) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AssetIdentifierApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
