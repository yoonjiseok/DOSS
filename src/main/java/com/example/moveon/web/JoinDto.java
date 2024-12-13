package com.example.moveon.web;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class JoinDto {
    Long userId;

    Integer n_pixel;

    BigInteger distance;
    
    Integer hart_rate;
}
