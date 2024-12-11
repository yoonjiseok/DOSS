package com.example.moveon.converter;

import com.example.moveon.domain.step_record;
import com.example.moveon.domain.user;
import com.example.moveon.web.dto.PixelDTO.MainPixelRequestdto;
import com.example.moveon.web.dto.PixelDTO.MainPixelResponsedto;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class MainPixelConverter {

    public static step_record toStepRecord(MainPixelRequestdto.JoinDto request) {
        user us = MainPixelConverter.toStepRecordUser(request);

        return step_record.builder()
                .user(us)
                .steps(request.getSteps())
                .distance(request.getDistance())
                .build();
    }

    public static user toStepRecordUser(MainPixelRequestdto.JoinDto request) {
        return user.builder()
                .id(request.getRecord_user_id())
                .build();
    }

    public static MainPixelResponsedto.MainPageListDTO toJoinResultDTO(step_record stepRecord){
        return MainPixelResponsedto.MainPageListDTO.builder()
                .n_pixel(stepRecord.getSteps())
                .distance(stepRecord.getDistance())
                .heart_rate(stepRecord.getHeart_rate())
                .build();
    }
}
