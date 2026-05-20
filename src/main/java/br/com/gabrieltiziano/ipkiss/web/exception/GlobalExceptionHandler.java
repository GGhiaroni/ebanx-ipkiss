package br.com.gabrieltiziano.ipkiss.web.exception;

import br.com.gabrieltiziano.ipkiss.domain.exception.AccountNotFoundException;
import br.com.gabrieltiziano.ipkiss.domain.exception.InsufficientBalanceException;
import br.com.gabrieltiziano.ipkiss.web.dto.AccountResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException e) {
        return ResponseEntity.status(404)
                .contentType(MediaType.TEXT_PLAIN)
                .body("0");
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<AccountResponse> handleInsufficientBalance(InsufficientBalanceException e) {
        AccountResponse body = new AccountResponse(e.getAccountId(), e.getCurrentBalance());
        return ResponseEntity.status(422).body(body);
    }
}
