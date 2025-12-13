package com.nghiemdd.vantagecareer.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nghiemdd.vantagecareer.domain.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper; // Dùng để convert thủ công

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

    @Override
    public Object beforeBodyWrite(Object body,
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request,
                                ServerHttpResponse response) {

        // 1. Bỏ qua Swagger ... (Giữ nguyên)
        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }

        // 2. Nếu đã là RestResponse thì trả về luôn (Giữ nguyên)
        if (body instanceof RestResponse) {
            return body;
        }

        // 3. Tạo object RestResponse chuẩn
        RestResponse<Object> res = new RestResponse<>();
        
        // --- ĐOẠN SỬA ĐỔI: Lấy status code động ---
        if (response instanceof ServletServerHttpResponse servletResponse) {
            // Lấy status code thật mà Controller set (ví dụ 201, 200)
            res.setStatusCode(servletResponse.getServletResponse().getStatus());
        } else {
            res.setStatusCode(200); // Default
        }

        res.setMessage("CALL API SUCCESS");
        res.setData(body);

        // 4. Xử lý String (Giữ nguyên logic của bạn)
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(res);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Không thể convert response sang JSON", e);
            }
        }
        
        return res;
    }
}
