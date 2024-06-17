package it.cgmconsulting.mspost.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mspost.configuration.BeanManagement;
import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.exception.GenericException;
import it.cgmconsulting.mspost.exception.ResourceNotFoundException;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import it.cgmconsulting.mspost.payload.response.SectionResponse;
import it.cgmconsulting.mspost.repository.PostRepository;
import it.cgmconsulting.mspost.repository.SectionRepository;
import it.cgmconsulting.mspost.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    @Value("${application.security.internalToken}")
    private String internalToken;

    private final PostRepository postRepository;
    private final SectionRepository sectionRepository;
    private final Map<String,String> getWriters;
    private final BeanManagement bean;
    private final CircuitBreakerService circuitBreakerService;


    public ResponseEntity<?> createdPost(PostRequest request, int author) {
        Post post = new Post(request.getTitle(), request.getPostImage(), author);
        postRepository.save(post);
        PostResponse p = new PostResponse(post.getId(), post.getTitle(), post.getPublicationDate());
        bean.getWriters();
        if (!getWriters.isEmpty()){
            p.setAuthor(getWriters.get(String.valueOf(post.getAuthor())));
        }
        return ResponseEntity.status(201).body(p);

    }

    public Post findById(int id){
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post","id", id));
    }

    public ResponseEntity<?> getPostDetail(int id) {
        // recuperare il post se esistente e pubblicato
        PostDetailResponse p = postRepository.getPostDetail(id, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));

        // recuperare le sezioni relative al post in questione
        Set<SectionResponse> section = sectionRepository.getSectionsResponse(id);
        p.setSections(section);

        p.setAuthor(getWriters.get(p.getAuthor()));

        //recuperare i tag associati al post
        p.setTagNames(circuitBreakerService.getTagNames(id).getBody());
        p.setAverage(circuitBreakerService.getAverage(id));

        return ResponseEntity.ok(p);
    }

    public ResponseEntity<?> getPostDetailBis(int id) {
        Post p = postRepository.findByIdAndPublicationDateIsNotNullAndPublicationDateLessThanEqual(id, LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        PostDetailResponse pdr = new PostDetailResponse(p.getId(), p.getTitle(), p.getPublicationDate(), p.getPostImage());

        Set<SectionResponse> sections = p.getSections().stream().map(SectionResponse::mapToResponse).collect(Collectors.toSet());
        pdr.setSections(sections);

        pdr.setAuthor(getWriters.get(String.valueOf(p.getAuthor())));
        pdr.setTagNames(circuitBreakerService.getTagNames(id).getBody());
        pdr.setAverage(circuitBreakerService.getAverage(id));
        return ResponseEntity.ok(pdr);
    }

 /*   //@CircuitBreaker(name="a-tentativi", fallbackMethod = "fallbackMethodGetWriters")
    private ResponseEntity<Set<String>> postTags(int postId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY + "/" + Consts.MS_TAG + "/v99/" + postId;

        ResponseEntity<Set<String>> response = null;
        try{
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<Set<String>>() {}
            );
        } catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(new HashSet<>());
        }
        return response;
    }

    //@CircuitBreaker(name="a-tentativi", fallbackMethod = "fallbackMethodGetWriters")
    private double getAverage(int postId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<Double> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY + "/" + Consts.MS_RATING + "/v99/" + postId;

        ResponseEntity<Double> average = null;
        try{
            average = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Double.class);
        }catch (RestClientException e){
            log.error(e.getMessage());
            return 0d;
        }

        return average.getBody() != null ? average.getBody() : 0d;
    }
*/

   /* private ResponseEntity<String> getUsername(int UserId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = "http://localhost:9090/ms-auth/v99/"+UserId;

        ResponseEntity<String> username = null;
        try{
            username = restTemplate.exchange(url, HttpMethod.GET, httpEntity,String.class);
        }catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

        return username;
    }*/

    @Transactional
    public ResponseEntity<?> publish(int id, LocalDate publicationDate) {
        // se publicationdate non viene passata = localdate.now
        // altrimenti deve essere una data nel futuro
        // per poter pubblicare bisogno anche verificare che ci sia almeno una senzione associata al post
        Post post = findById(id);
        if (post.getSections().isEmpty()){
            throw new GenericException("No post's sections found", HttpStatus.CONFLICT);
        }

        if (publicationDate != null && publicationDate.isBefore(LocalDate.now())){
            throw new GenericException("The date must be NOW or in the future", HttpStatus.CONFLICT);
        }

        if(publicationDate == null){
            publicationDate = LocalDate.now();
        }

        post.setPublicationDate(publicationDate);
        post.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok("Post published");
    }

    public ResponseEntity<?> getLastPublishedPost(int pageNumber, int pageSize, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.valueOf(direction.toUpperCase()),
                sortBy);
        Page<PostResponse> posts = postRepository.getLastPublishedPost(pageable,LocalDate.now());
        List<PostResponse> list = posts.getContent();
        Map<String, String> m = getWriters;
        for (PostResponse p : list){
            p.setAuthor(getWriters.get(p.getAuthor()));
        }
        return ResponseEntity.ok(list);
    }
    public ResponseEntity<Boolean> existsById(int postId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postRepository.existsByIdAndPublicationDateIsNotNullAndPublicationDateLessThanEqual(postId, LocalDate.now()));
    }

}
