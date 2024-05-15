package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.ReasonId;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.ReasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReasonService {

    private final ReasonRepository reasonRepository;

    public String addReason(String reason, LocalDate startDate, int severity){
        // verificare che non esista già un altra reson con quel nome in corso di validità
        if (reasonRepository.existsByReasonIdReasonAndEndDateIsNull(reason)){
            return null;
        }

        Reason r = new Reason(
                new ReasonId(reason,startDate),
                severity
        );
        reasonRepository.save(r);
        return "Reason " + reason + " successfully created";

    }

    @Transactional
    public String deleteReason(String reason, LocalDate now){
        //verificare che ci sia una reason da invalidare, che abbia enddate a null oppure che abbia un enddate futura
        List<Reason> validReasons = reasonRepository.getValidReason(reason, now);

        if (validReasons.isEmpty()){
            return "No reason to invalidate";
        }else if (validReasons.size() == 1){
            validReasons.getFirst().setEndDate(now);
        } else {
            for (Reason r : validReasons){
                if (r.getEndDate() != null){
                    r.setEndDate(now);
                }else {
                    reasonRepository.delete(r);
                }
            }
        }

        return "Reason" + reason + " invalitaded";
    }

    public List<ReasonId> getValidReasons() {
        List<ReasonId> validReasons = reasonRepository.getValidReasons(LocalDate.now());
        List<ReasonId> newValidReasons = new ArrayList<>();
        for (ReasonId r : validReasons) {
            if(r.getStartDate().isBefore(LocalDate.now().plusDays(1)))
                newValidReasons.add(r);
        }
        return newValidReasons;
    }

    public Reason findReasonById(ReasonId reasonId) {
        return reasonRepository.findById(new ReasonId(reasonId.getReason(), reasonId.getStartDate())).orElseThrow(
                () -> new ResourceNotFoundException("Reason", "Id", new ReasonId(reasonId.getReason(), reasonId.getStartDate())));
    }
}
