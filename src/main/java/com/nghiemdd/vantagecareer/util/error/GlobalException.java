package com.nghiemdd.vantagecareer.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.nghiemdd.vantagecareer.domain.RestResponse;

import io.micrometer.core.instrument.Meter.Id;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();

        // 1. Set statusCode: 400
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());

        // 2. Set error
        res.setError(ex.getMessage());

        // 3. Set message
        // res.setMessage(ex.getClass().getSimpleName());
        res.setMessage("Exception occurred...");

        // 4. Set data: null
        res.setData(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        // Lấy danh sách các lỗi từ exception
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        // Biến danh sách lỗi thành List String (ví dụ: ["Email không đúng định dạng",
        // "Password quá ngắn"])
        List<String> errors = fieldErrors.stream()
                .map(f -> f.getDefaultMessage())
                .collect(Collectors.toList());

        // Tạo RestResponse
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail()); // Lấy tiêu đề lỗi chung

        // Mẹo: Vì message của bạn là kiểu Object, bạn có thể truyền cả List lỗi vào đây
        res.setMessage(errors.size() > 1 ? errors : errors.get(0)); // Nếu chỉ 1 lỗi thì lấy thẳng chuỗi

        res.setData(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            NoResourceFoundException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError(ex.getMessage());
        res.setMessage("404 Not Found. URL may not exist...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
