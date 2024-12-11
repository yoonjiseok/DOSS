package com.example.moveon.apipayload.exception.handler;

import com.example.moveon.apipayload.code.BaseErrorCode;
import com.example.moveon.apipayload.exception.GeneralException;

public class TempHandler extends GeneralException {

  public TempHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
