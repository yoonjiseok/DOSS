package com.example.moveon.service.MainService;

import com.example.moveon.converter.MainPixelConverter;
import com.example.moveon.domain.step_record;
import com.example.moveon.domain.user;
import com.example.moveon.repository.StepRecordRepository;
import com.example.moveon.web.dto.PixelDTO.MainPixelRequestdto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainCommandServiceImpl implements MainCommandService {

    private final StepRecordRepository stepRecordRepository;


    @Transactional
    public step_record addrecord(MainPixelRequestdto.JoinDto request) {

        step_record u_step_record = MainPixelConverter.toStepRecord(request);
        //user u_user = MainPixelConverter.toStepRecordUser(request);

        return stepRecordRepository.save(u_step_record);
    }
}
