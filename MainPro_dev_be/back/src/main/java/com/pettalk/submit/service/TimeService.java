package com.pettalk.submit.service;

import com.pettalk.submit.dto.TimeFilter;
import com.pettalk.submit.entity.PetSitterApplicant;
import com.pettalk.submit.repository.PetSitterApplicantRepository;
import com.pettalk.wcboard.entity.WcBoard;
import com.pettalk.wcboard.repository.WcBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //private final 된직것만
@Slf4j
public class TimeService {
    private final PetSitterApplicantRepository paRepository;
    private final WcBoardRepository wcBoardRepository;

    public List<TimeFilter> getTimeByPetSitterId (Long petSitterId) {
        List<PetSitterApplicant> petSitterApplicants = paRepository.findByPetSitter_PetSitterId(petSitterId);
        return petSitterApplicants.stream()
                .map(petSitterApplicant -> {
                    WcBoard wcboard = wcBoardRepository.findByWcboardId(petSitterApplicant.getWcboardId());
                    if (wcboard != null) {
                        return new TimeFilter(wcboard.getStartTime(), wcboard.getEndTime());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
