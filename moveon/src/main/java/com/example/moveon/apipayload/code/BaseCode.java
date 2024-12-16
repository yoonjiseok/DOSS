package com.example.moveon.apipayload.code;

import com.example.moveon.apipayload.code.status.ReasonDTO;

public interface BaseCode {
    ReasonDTO getReason();

    ReasonDTO getReasonHttpStatus();
}
