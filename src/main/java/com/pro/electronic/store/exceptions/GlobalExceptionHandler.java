package com.pro.electronic.store.exceptions;

import com.pro.electronic.store.dtos.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> ResponseNotFoundHandler(ResourceNotFoundException exception)
{
    ApiResponseMessage message = ApiResponseMessage.builder().message(exception.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
return  new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
}

//Message argument not found exception in postman
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<Map<String,Object>> HandlerMessageArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        Map <String,Object> response = new HashMap<>();
        allErrors.stream().forEach(objectError ->
        {
            String defaultMessage = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();

            response.put(field,defaultMessage);

        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> BadRequestApiHandler(BadApiRequest exception)
    {
        ApiResponseMessage message = ApiResponseMessage.builder().message(exception.getMessage()).status(HttpStatus.BAD_REQUEST).success(true).build();
        return  new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }


}
