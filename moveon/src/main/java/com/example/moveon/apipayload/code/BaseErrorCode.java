package com.example.moveon.apipayload.code;

import com.example.moveon.apipayload.code.status.ErrorReasonDTO;

public interface BaseErrorCode {

    ErrorReasonDTO getReason();

    ErrorReasonDTO getReasonHttpStatus();
}
