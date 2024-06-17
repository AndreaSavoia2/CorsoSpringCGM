package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import it.cgmconsulting.mspost.service.PdfService;
import it.cgmconsulting.mspost.service.PostService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;
    private final PostService postService;

    @GetMapping("/v0/pdf/{postId}")
    public ResponseEntity<?> createPdf(@PathVariable @Min(1) int postId){
        ResponseEntity<?> responsePost = postService.getPostDetail(postId);
        InputStream pdfFile = null;
        ResponseEntity<InputStreamResource> response = null;

        if(responsePost.getStatusCode() == HttpStatusCode.valueOf(200)){
            try{
                PostDetailResponse post = (PostDetailResponse) responsePost.getBody();
                pdfFile = pdfService.createPdf(post);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Method", "GET");
                headers.add("Access-Control-Allow-Header", "Content-Type");
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                headers.add("Content-disposition", "inline; filename="+post.getId()+".pdf");

                response = new ResponseEntity<InputStreamResource>(
                        new InputStreamResource(pdfFile),
                        headers,
                        HttpStatus.OK
                );

            } catch (Exception e){
                response = new ResponseEntity<InputStreamResource>(
                        new InputStreamResource(InputStream.nullInputStream(), "ERROR CREATING PDF FILE"),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

        }
        return response;
    }


}
