package com.example.moveon.service.MainService;

import com.example.moveon.domain.step_record;
import com.example.moveon.web.dto.PixelDTO.MainPixelRequestdto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


public interface MainCommandService {
    step_record addrecord(MainPixelRequestdto.@Valid JoinDto request);

}
