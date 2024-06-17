package it.cgmconsulting.mspost.configuration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mspost.exception.ResourceNotFoundException;
import it.cgmconsulting.mspost.repository.PostRepository;
import it.cgmconsulting.mspost.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BeanManagement {

    @Value("${application.security.internalToken}")
    private String internalToken;

    private final PostRepository postRepository;

    @Bean("getWriters")
    @Scope("prototype")
    @CircuitBreaker(name="a-tentativi", fallbackMethod = "fallbackMethodGetWriters")
    public Map<String, String> getWriters(){

        Set<Integer> authorIds = postRepository.getAuthorsIds();

        RestTemplate restTemplate = new RestTemplate();

        String url= Consts.GATEWAY+"/"+Consts.MS_AUTH+"/v99/role";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<Set<Integer>> httpEntity = new HttpEntity<>(authorIds, headers);
        try{
            ResponseEntity<?> r = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            if(r.getStatusCode().equals(HttpStatus.OK))
                return (Map<String, String>) r.getBody();
        } catch (RestClientException e){
            log.error("Method GetWriters: "+e.getMessage());
            //return null;
            throw e;
        }
        return new HashMap<String, String >();
    }

    // la fallback deve avere stesso ritorno e paramtri + exception
    public Map<String, String> fallbackMethodGetWriters(Exception e){
        log.error(e.getMessage());
        //return null;
        return new HashMap<String, String >();
    }


    // ----------------- RICHIESTA CON RUOLO ---------------------------
        /*RestTemplate restTemplate = new RestTemplate();
        String url = Consts.GATEWAY+"/"+Consts.MS_AUTH+"/v99/role/WRITER";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
*/
        /*try{
            ResponseEntity<?> r = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
            if(r.getStatusCode().equals(HttpStatus.OK)){
                return (Map<String, String>) r.getBody();
            }
        } catch (ResourceNotFoundException e){
            log.error(e.getMessage());
            return null;
        }
        return null;*/
}
