package it.cgmconsulting.mspost.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mspost.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CircuitBreakerService {

    @Value("${application.security.internalToken}")
    String internalToken;

    @CircuitBreaker(name ="a-tempo-tags", fallbackMethod = "fallbackMethodGetTagNames")
    public ResponseEntity<Set<String>> getTagNames(int postId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY + "/" + Consts.MS_TAG + "/v99/" + postId;

        ResponseEntity<Set<String>> tagNames = null;
        try{
            tagNames = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
                    new ParameterizedTypeReference<Set<String>>() {});
        } catch (RestClientException e){
            log.error(e.getMessage());
            //return ResponseEntity.status(500).body(new HashSet<>());
            throw e;
        }
        return tagNames;
    }

    @CircuitBreaker(name ="a-tempo-ratings", fallbackMethod = "fallbackMethodGetAverage")
    public double getAverage(int postId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY + "/" + Consts.MS_RATING + "/v99/" + postId;

        ResponseEntity<Double> response = null;
        try{
            response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Double.class);
        } catch (Exception e){
            log.error(e.getMessage());
            //return 0d;
            throw e;
        }
        return response.getBody() != null ? response.getBody() : 0d;
    }

    public ResponseEntity<Set<String>> fallbackMethodGetTagNames(int postId, Throwable e){
        log.error(e.getMessage());
        log.info("Utilizzo fallback method per getTagNames()");
        return ResponseEntity.ok(new HashSet<>());
    }

    public double fallbackMethodGetAverage(int postId, Throwable e){
        log.error(e.getMessage());
        log.info("Utilizzo fallback method per getAverage()");
        return 0d;
    }
}
