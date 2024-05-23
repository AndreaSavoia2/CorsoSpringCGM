package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Consent;
import it.cgmconsulting.myblog.entity.ConsentId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository consentRepository;

    public void save(Consent consent){
        consentRepository.save(consent);
    }

    public Consent findByConsentId(ConsentId consentId){
        return consentRepository.findById(consentId).orElseThrow(() -> new ResourceNotFoundException("Consent", "id", consentId));
    }

    public List<Consent> getNewsletterMembers(){
        return consentRepository.findBySendNewsletterTrue();
    }
}
