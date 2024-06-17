package it.cgmconsulting.mscomment.configuration;

import it.cgmconsulting.mscomment.repository.CommentRepository;
import it.cgmconsulting.mscomment.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BeanManagement {

    @Value("${application.security.internalToken}")
    String internalToken;

    private final CommentRepository commentRepository;

    @Bean("getMembers")
    @Scope("prototype")
    public Map<String, String> getMembers(){

        Set<Integer> authorIds = commentRepository.getAuthorIds();

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
            log.error(e.getMessage());
            return null;
        }
        return null;
    }
}

