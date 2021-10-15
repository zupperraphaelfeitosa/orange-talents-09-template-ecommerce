package br.com.zup.raphaelfeitosa.ecommerce.validacao.handler;

import br.com.zup.raphaelfeitosa.ecommerce.validacao.handler.exception.ExcecaoEstoqueIndisponivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorDeValidacaoDto> handlerError(MethodArgumentNotValidException exception) {
        List<ErrorDeValidacaoDto> errorDeValidacaoDtoList = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(erros -> {
            String mensagem = messageSource.getMessage(erros, LocaleContextHolder.getLocale());
            ErrorDeValidacaoDto error = new ErrorDeValidacaoDto(erros.getField(), mensagem);
            errorDeValidacaoDtoList.add(error);
        });
        //retorna erro do objeto quando criamos nossa anotação e anotamos a classe Request/DTO
        List<ObjectError> objectErrors = exception.getBindingResult().getGlobalErrors();
        objectErrors.forEach(erros -> {
            String mensagem = messageSource.getMessage(erros, LocaleContextHolder.getLocale());
            ErrorDeValidacaoDto error = new ErrorDeValidacaoDto(erros.getObjectName(), mensagem);
            errorDeValidacaoDtoList.add(error);
        });
        return errorDeValidacaoDtoList;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public List<ErrorDeValidacaoDto> bindException(BindException exception) {
        List<ErrorDeValidacaoDto> errorDeValidacaoDtoList = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(erros -> {
            String mensagem = messageSource.getMessage(erros, LocaleContextHolder.getLocale());
            ErrorDeValidacaoDto error = new ErrorDeValidacaoDto(erros.getField(), mensagem);
            errorDeValidacaoDtoList.add(error);
        });
        return errorDeValidacaoDtoList;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExcecaoEstoqueIndisponivel.class)
    public Map<String, String> handlerExcecaoEstoqueIndisponivel(ExcecaoEstoqueIndisponivel exception) {
        return Map.of("message:", exception.getMessage());

    }

}
