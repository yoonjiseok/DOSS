package com.example.moveon.web.dto.PixelDTO;

import com.example.moveon.domain.user;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class MainPixelRequestdto {

    @Getter
    public static class JoinDto{
        @NotNull
        Long record_user_id;

        @NotNull
        Integer distance;

        @NotNull
        Integer steps;

        Integer heart_rate;


    }
}
