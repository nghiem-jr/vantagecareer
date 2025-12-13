package com.nghiemdd.vantagecareer.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nghiemdd.vantagecareer.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException ex) {
        RestResponse<Object> res = new RestResponse<>();
        
        // 1. Set statusCode: 400
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        
        // 2. Set error
        res.setError(ex.getMessage());
        
        // 3. Set message
        // res.setMessage(ex.getClass().getSimpleName());
        res.setMessage("Call api failed");
        
        // 4. Set data: null
        res.setData(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
