/** 
 * Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
 *
 * See the AUTHORS file(s) distributed with this work for additional
 * information regarding authorship.
 *
 * See the LICENSE file(s) distributed with this work for
 * additional information regarding license terms.
 */

package net.catenax.semantics.idsadapter.restapi.controller;

import java.util.Map;

import net.catenax.semantics.idsadapter.service.IdsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.idsadapter.client.api.ConnectorApi;
import net.catenax.semantics.idsadapter.restapi.dto.Offer;
import net.catenax.semantics.idsadapter.restapi.dto.Source;
import net.catenax.semantics.idsadapter.service.AdapterService;

import io.swagger.annotations.*;

/**
 * Controller to implement the Adapters REST interface One face to the "business
 * partner" for investigating whats inside the adapter and one face to the "ids"
 * connector for accessing/triggering data accesses.
 */
@Controller
@RequestMapping("${openapi.semanticHub.base-path:/api/v1/adapter}")
@AllArgsConstructor
@Slf4j
@Api(tags="Adapter", value = "adapter", description = "Simple Semantic Adapter API")
public class AdapterController {
    private final AdapterService adapterService;
    private final IdsService idsService;

    /**
     * Simple hello
     * 
     * @return hello response
     */
    @GetMapping(value = "/hello", produces = "text/plain")
    public ResponseEntity<String> hello() {
        log.info("getting hello");
        return ResponseEntity.ok("hello");
    }

    /**
     * obtain info of the associated ids connector
     * 
     * @return self-description response
     */
    @GetMapping(value = "/idsinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> idsInfo() {
        return ResponseEntity.ok(idsService.getSelfDescription());
    }

    /**
     * publish a preconfigured source
     * 
     * @param name  source specification
     * @param offer body
     * @return register response
     */
    @PostMapping(value = "/offer/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Offer> offerResource(@PathVariable("name") String name, @RequestBody Offer offer) {
        return ResponseEntity.ok(adapterService.getOrCreateOffer(name, offer));
    }

    /**
     * publish a preconfigured twin
     * 
     * @param name  source specification
     * @param source Source
     * @return register response
     */
    @PostMapping(value = "/register/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerTwins(@PathVariable("name") String name, @RequestBody Source source) {
        try {
            return ResponseEntity.ok(adapterService.registerTwins(name, source));
        } catch(Exception e) {
            e.printStackTrace(System.err);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Download an offered, possibly aspect-json transformed source
     * 
     * @param parameters map of the parameters
     * @return response including the transformed source
     */
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> downloadAgreement(@RequestParam Map<String, String> parameters) {
        StreamingResponseBody streamingResponseBody = response -> {
            adapterService.downloadForAgreement(response, MediaType.APPLICATION_OCTET_STREAM_VALUE, parameters);
        };
        return ResponseEntity.ok(streamingResponseBody);
    }
}
