package it.cgmconsulting.banner.controller;

import it.cgmconsulting.banner.entity.Campaign;
import it.cgmconsulting.banner.entity.Company;
import it.cgmconsulting.banner.service.MainService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    // api per la chiamate esterne

    // post/get per entità company campaing

    /* ********* Company ************** */
    @PostMapping("/companies")
    public ResponseEntity<?> addCompany(@RequestParam @NotBlank @Size(max = 255, min = 1) String companyName){
        return ResponseEntity.status(201).body(mainService.addCompany(companyName));
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<?> getCompany(@PathVariable @Min(1) int id){
        Optional<Company> company = mainService.getCompany(id);
        if (company.isPresent()){
            return ResponseEntity.ok(company);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Company found");
    }

    @GetMapping("/companies")
    public ResponseEntity<?> getCompanies(
            @RequestParam(defaultValue = "0") int pageNumber, //numero di pagina da partire
            @RequestParam(defaultValue = "10") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "companyName") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction
    ){
        List<Company> list = mainService.getCompanies(pageNumber, pageSize, sortBy, direction);
        if(list.isEmpty())
            return ResponseEntity.status(404).body("No companies found");
        return ResponseEntity.ok(list);
    }

    /* ********* CAMPAIGN ************** */
    @PostMapping("/campaigns")
    public ResponseEntity<?> addCampaign(
            @RequestParam @NotNull @FutureOrPresent LocalDate startDate,
            @RequestParam @NotNull @FutureOrPresent LocalDate endDate,
            @RequestParam @Min(1) int companyId,
            @RequestParam @NotBlank @Size(max = 255, min=1) String product,
            @RequestParam MultipartFile file
    ){
        Map<Boolean, Object> response = mainService.addCampaing(startDate, endDate, companyId, product, file);
        if (response.containsKey(Boolean.FALSE)){
            return ResponseEntity.status(400).body(response.get(Boolean.FALSE));
        }
        return ResponseEntity.status(201).body(response.get(Boolean.TRUE));
    }

    @GetMapping("/campaigns")
    public ResponseEntity<?> getCampaigns(
            @RequestParam(defaultValue = "0") int pageNumber, //numero di pagina da partire
            @RequestParam(defaultValue = "10") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "endDate") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction
    ){
        // elenco campagna paginate e ordinate di defaul per endDase e desc
        List<Campaign> list = mainService.getCampaigns(pageNumber,pageSize,sortBy,direction);
        if(list.isEmpty())
            return ResponseEntity.status(404).body("No Campaign found");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/api/campaigns")
    public ResponseEntity<?> getBanner(@RequestHeader("Authorization") String id){
        String banner = mainService.getBanner(id);
        if(banner != null){
            return ResponseEntity.status(HttpStatus.OK).body(banner);
        }
        return ResponseEntity.status(404).body(null);
    }

}
