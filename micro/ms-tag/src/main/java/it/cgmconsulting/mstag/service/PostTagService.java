package it.cgmconsulting.mstag.service;

import it.cgmconsulting.mstag.entity.PostTag;
import it.cgmconsulting.mstag.entity.PostTagId;
import it.cgmconsulting.mstag.entity.Tag;
import it.cgmconsulting.mstag.repository.PostTagRepository;
import it.cgmconsulting.mstag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;


    public ResponseEntity<?> addTagsToPost(int postId, Set<String> tagNames) {
        Set<Tag> tags = tagRepository.getTags(tagNames);
        List<PostTag> postTags = new ArrayList<>();
        for (Tag t : tags) {
            postTags.add(new PostTag(new PostTagId(postId,t)));
        }
        // --------------------------------  stream ------------------------------------
        /*List<PostTag> postTags = tags.stream()
                .map(t -> new PostTag(new PostTagId(postId, t)))
                .collect(Collectors.toList());*/

        postTagRepository.saveAll(postTags);
        return ResponseEntity.status(201).body(postTags);
    }

    /*public ResponseEntity<?> updateTagsToPost(int postId, Set<String> tagNames) {
        Set<Tag> newTags = tagRepository.getTags(tagNames);
        Set<Tag> oldTags = postTagRepository.getTags(postId);
        Set<Tag> tagsToRemove = new HashSet<>();

        Set<PostTag> postTagsToRemove = new HashSet<>();
        Set<PostTag> postTags = new HashSet<>();

        for (Tag t : oldTags) {
            if (!newTags.contains(t)) {
                tagsToRemove.add(t);
            }
        }

        oldTags.removeAll(tagsToRemove);
        oldTags.addAll(newTags);

        for (Tag t : tagsToRemove) {
            postTagsToRemove.add(new PostTag(new PostTagId(postId, t)));
        }
        postTagRepository.deleteAll(postTagsToRemove);
        for (Tag t : oldTags) {
            postTags.add(new PostTag(new PostTagId(postId, t)));
        }
        postTagRepository.saveAll(postTags);

        return ResponseEntity.ok(postTags);
    }*/

    @Transactional
    public ResponseEntity<?> updateTagsToPost(int postId, Set<String> tagNames) {

        // se il set<String> tagNames è vuoto, vengono eliminati tutti i record sulla tabella post_tag
        postTagRepository.deleteByPostId(postId);
        Set<Tag> newTags = tagRepository.getTags(tagNames);
        Set<PostTag> newPostTags = new HashSet<>();
        for(Tag t : newTags){
            newPostTags.add(new PostTag(new PostTagId(postId, t)));
        }
        postTagRepository.saveAll(newPostTags);
        return ResponseEntity.ok(newPostTags);
    }

    public ResponseEntity<?> getTagsByPost(int postId) {
        Set<String> tags = postTagRepository.getTagsByPost(postId);
        return ResponseEntity.ok(tags);
    }
}
