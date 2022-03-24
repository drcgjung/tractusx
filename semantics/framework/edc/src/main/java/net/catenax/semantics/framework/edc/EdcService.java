/*
Copyright (c) 2021-2022 T-Systems International GmbH
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.edc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import net.catenax.semantics.framework.*;
import net.catenax.semantics.framework.auth.TokenWrapper;
import net.catenax.semantics.framework.config.*;
import net.catenax.semantics.framework.ids.BaseConnector;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Conditional(EdcConfigurationCondition.class)
public class EdcService<Cmd extends Command, O extends Offer, Ct extends Catalog, Co extends Contract, T extends Transformation> extends BaseConnector<Cmd,O,Ct,Co,T> {

    private HttpClient edcClient;

    public EdcService(Config<Cmd,O,Ct,Co, T> configurationData,
                      List<BackendAdapter> adapters,
                      List<Transformer> transformers) {
        super(configurationData,adapters,transformers);

        String proxyHost=System.getProperty("http.proxyHost");

        boolean useApiKey = "X-Api-Key".equals(configurationData.getConnectorUser());

        String header;
        String token;

        if(!useApiKey) {
            header=TokenWrapper.AUTHORIZATION_HEADER;
            token = "Basic " + Base64.encodeBase64((configurationData.getConnectorUser() + ":" + configurationData.getConnectorPassword()).getBytes());
        } else {
            header=configurationData.getConnectorUser();
            token= configurationData.getConnectorPassword();
        }

        var interceptor=new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                httpRequest.addHeader(header, token);
            }
        };

        if (proxyHost != null && !proxyHost.isEmpty()) {
            boolean noProxy = false;
            for (String noProxyHost : System.getProperty("http.nonProxyHosts","localhost").split("\\|")) {
                noProxy = noProxy || configurationData.getServiceUrl().contains(noProxyHost.replace("*",""));
            }
            if (!noProxy) {
                HttpHost httpProxyHost = new HttpHost(proxyHost, Integer.parseInt(System.getProperty("http.proxyPort","80")));
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(httpProxyHost);
                HttpClientBuilder clientBuilder = HttpClients.custom();
                clientBuilder = clientBuilder.setRoutePlanner(routePlanner);
                clientBuilder = clientBuilder.addInterceptorFirst(interceptor);
                edcClient = clientBuilder.build();
            }
        }
        if (edcClient == null) {
            HttpClientBuilder clientBuilder = HttpClients.custom();
            clientBuilder.addInterceptorFirst(interceptor);
            edcClient = clientBuilder.build();
        }
    }

    public Offer getOrCreateOffer(String title) {
        O result=configurationData.getOffers().get(title);
        HttpPost httppost = new HttpPost(configurationData.getConnectorUrl() + "/api/assets");
        httppost.addHeader("accept", "*/*");
        httppost.setHeader("Content-type", "application/json");
        String thatPayLoad="{\n" +
                "  \"asset\": {\n" +
                "    \"asset:prop:id\": \""+title+"\",\n" +
                "    \"asset:prop:name\": \""+ result.getDescription()+"\",\n" +
                "    \"asset:prop:contenttype\": \"application/json\",\n" +
                "    \"asset:prop:policy-id\": \"use-eu\"\n" +
                "  },\n" +
                "  \"dataAddress\": {\n" +
                "    \"endpoint\": \""+configurationData.getAdapterUrl()+"/"+title+"\",\n" +
                "    \"type\": \"HttpData\"\n" +
                "  }\n" +
                "}";
        try {
            httppost.setEntity(new StringEntity(thatPayLoad));
            HttpResponse twinResponse = edcClient.execute(httppost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Contract getOrCreateContract(String title) {
        Co result=configurationData.getContracts().get(title);
        HttpPost httppost = new HttpPost(configurationData.getConnectorUrl() + "/api/contractdefinitions");
        httppost.addHeader("accept", "*/*");
        httppost.setHeader("Content-type", "application/json");

        var relevantOffers= configurationData.getOffers().entrySet().stream().filter(offerEntry -> {
           return title.equals(offerEntry.getValue().getContract()) ;
        });

        String permissions = relevantOffers.map( offerEntry -> {
            return "      {\n" +
                   "        \"edctype\": \"dataspaceconnector:permission\",\n" +
                   "        \"uid\": null,\n" +
                   "        \"target\": \""+offerEntry.getKey()+"\",\n" +
                   "        \"action\": {\n" +
                   "          \"type\": \"USE\"\n" +
                   "        },\n"+
                   "        \"constraints\": [],\n" +
                   "        \"duties\": []\n" +
                   "      }\n";
        }).collect(Collectors.joining(","));
        String thatPayLoad="{\n" +
                "  \"id\": \""+title+"\",\n" +
                "  \"accessPolicy\": {\n" +
                "    \"uid\": \"AP_0\",\n" +
                "    \"permissions\": [\n" + permissions +
                "    ],\n" +
                "    \"prohibitions\": [],\n" +
                "    \"obligations\": [],\n" +
                "    \"extensibleProperties\": {},\n" +
                "    \"@type\": {\n" +
                "      \"@policytype\": \"set\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"contractPolicy\": {\n" +
                "    \"uid\": \"CP_0\",\n" +
                "    \"permissions\": [\n" + permissions +
                "    ],\n" +
                "    \"prohibitions\": [],\n" +
                "    \"obligations\": [],\n" +
                "    \"extensibleProperties\": {},\n" +
                "    \"@type\": {\n" +
                "      \"@policytype\": \"set\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"selectorExpression\": {\n" +
                "    \"criteria\": []\n" +
                "  }\n" +
                "}\n";
        try {
            httppost.setEntity(new StringEntity(thatPayLoad));
            HttpResponse twinResponse = edcClient.execute(httppost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object getSelfDescription() {
        return new Object();
    }

}
