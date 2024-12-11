package com.example.moveon.web.dto.PixelDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;

public class MainPixelResponsedto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageListDTO {
        Long userId;
        Integer n_pixel;
        Integer distance;
        Integer heart_rate;

        // id 수로 걸음 수 측정
        Long step_id;
    }
}
