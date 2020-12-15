package com.ti.test.connector.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ti.test.exception.CallingRestException;
import com.ti.test.model.api.response.BaseApiResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ExtractingResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

@Slf4j
class BaseRestConnector<INPUT, OUPUT> {

    private static final String BASE_URL = "https://sandbox.platfr.io";
    private static final HttpHeaders COMMON_HEADERS = new HttpHeaders(new LinkedMultiValueMap<>(Stream.of(
            new AbstractMap.SimpleEntry<>("Api-Key", singletonList("FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")),
            new AbstractMap.SimpleEntry<>("Auth-Schema", singletonList("S2S"))
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private ObjectMapper objectMapper;

    protected <OUTPUT, T extends BaseApiResponse<OUTPUT>> ResponseEntity<T> doCall(RestConfiguration restConfiguration, INPUT request, Class<T> responseObjectClass) {

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        RestTemplate restTemplate = setUpRestTemplate(messageConverter);
        restTemplate.getMessageConverters().add(messageConverter);

        URI uri = buildUri(BASE_URL + restConfiguration.getUrl(), restConfiguration.getQueryParams(), restConfiguration.getUriParams());
        HttpEntity<INPUT> entity = new HttpEntity<>(request, buildHeaders(restConfiguration.getHeaders()));
        return restTemplate.exchange(uri, restConfiguration.getMethod(), entity, responseObjectClass);
    }

    private RestTemplate setUpRestTemplate(MappingJackson2HttpMessageConverter messageConverter) {
        ExtractingResponseErrorHandler errorHandler = setUpErrorHandler();
        errorHandler.setMessageConverters(singletonList(messageConverter));
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(30000))
                .setReadTimeout(Duration.ofMillis(30000))
                .errorHandler(errorHandler)
                .build();
    }

    private ExtractingResponseErrorHandler setUpErrorHandler() {
        ExtractingResponseErrorHandler errorHandler = new ExtractingResponseErrorHandler();
        EnumMap<HttpStatus.Series, Class<? extends RestClientException>> seriesMapping = new EnumMap<>(HttpStatus.Series.class);
        seriesMapping.put(HttpStatus.Series.CLIENT_ERROR, CallingRestException.class);
        seriesMapping.put(HttpStatus.Series.SERVER_ERROR, CallingRestException.class);
        errorHandler.setSeriesMapping(seriesMapping);
        return errorHandler;
    }

    private HttpHeaders buildHeaders(HttpHeaders requestHeaders) {
        HttpHeaders finalHeaders = new HttpHeaders(COMMON_HEADERS);
        if (requestHeaders != null) {
            finalHeaders.putAll(requestHeaders);
        }
        return finalHeaders;
    }

    private URI buildUri(String requestUrl, MultiValueMap<String, String> queryParams, Map<String, String> uriParams) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(requestUrl);
        if (queryParams != null) {
            builder.queryParams(queryParams);
        }

        builder.uriVariables(new HashMap<>(uriParams));

        URI uri = builder.build().toUri();
        log.debug("URI: {}", uri);
        return uri;
    }

    @Getter
    @Setter
    protected static class RestConfiguration {

        private HttpMethod method = HttpMethod.GET;
        private HttpHeaders headers = new HttpHeaders();
        private String url = Strings.EMPTY;
        private Map<String, String> uriParams = new HashMap<>();
        private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    }
}
