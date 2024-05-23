package it.cgmconsulting.banner.service;

import it.cgmconsulting.banner.entity.Campaign;
import it.cgmconsulting.banner.entity.Company;
import it.cgmconsulting.banner.repository.CampaignRepository;
import it.cgmconsulting.banner.repository.CompanyRepository;
import it.cgmconsulting.banner.repository.CounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainService {

    @Value("${application.image.banner.path}")
    private String path;
    @Value("${application.image.banner.size}")
    private long size;
    @Value("${application.image.banner.height}")
    private int height;
    @Value("${application.image.banner.width}")
    private int width;
    @Value("${application.image.banner.extensions}")
    private String[] extensions;

    private final CompanyRepository companyRepository;
    private final CounterRepository counterRepository;
    private final CampaignRepository campaignRepository;
    private final ImageService imageService;

    public Company addCompany(String companyName){
        Company company = Company.builder().companyName(companyName).build();
        return companyRepository.save(company);
    }

    public Optional<Company> getCompany(int id){
        return companyRepository.findById(id);
    }

    public List<Company> getCompanies(int pageNumber,int pageSize, String sortBy, String direction){
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.valueOf(direction.toUpperCase()),
                sortBy);

        return companyRepository.findAll(pageable).getContent();
    }

    public Map<Boolean, Object> addCampaing(LocalDate startDate, LocalDate endDate, int companyId, String product, MultipartFile file) {
        Map<Boolean, Object> response = new HashMap<>();
        Optional<Company> company = getCompany(companyId);

        if (company.isEmpty()){
            response.put(false,"Company not found");
            return response;
        }
        if (endDate.isBefore(startDate)){
            response.put(false,"Company end date is before start date");
            return response;
        }
        if (imageService.checkSize(file,size)){
            response.put(false,"Wrong size image");
            return response;
        }
        if (!imageService.checkDimensions(width,height,file)){
            response.put(false,"Wrong image width or height");
            return response;
        }
        if (!imageService.checkExtensions(extensions,file)){
            response.put(false,"Extension not allowed");
            return response;
        }

        Campaign campaign = Campaign.builder()
                .company(company.get())
                .endDate(endDate)
                .image(file.getOriginalFilename())
                .product(product)
                .startDate(startDate)
                .build();
        campaignRepository.save(campaign);

        if (!imageService.uploadImage(file, campaign.getId(), path)){
            response.put(false,"Something went wrong uploading the image");
            return response;
        }

        campaign.setImage(path+campaign.getImage());
        response.put(true, campaign);

        return response;
    }

    public List<Campaign>  getCampaigns(int pageNumber,int pageSize, String sortBy, String direction){
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.valueOf(direction.toUpperCase()),
                sortBy);

        return campaignRepository.findAll(pageable).getContent();
    }

    public String getBanner(String id){
        String banner = campaignRepository.getBanner(id, LocalDate.now());
        if (banner != null){
            return path+banner;
        }
        return null;
    }

}
