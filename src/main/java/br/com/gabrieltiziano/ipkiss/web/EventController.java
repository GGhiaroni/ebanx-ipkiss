package br.com.gabrieltiziano.ipkiss.web;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.domain.service.DepositService;
import br.com.gabrieltiziano.ipkiss.web.dto.AccountResponse;
import br.com.gabrieltiziano.ipkiss.web.dto.DepositRequest;
import br.com.gabrieltiziano.ipkiss.web.dto.DepositResponse;
import br.com.gabrieltiziano.ipkiss.web.dto.EventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    private final DepositService depositService;

    public EventController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> handleEvent(@RequestBody EventRequest event){
        if(event instanceof DepositRequest depositRequest){
            Account result = depositService.deposit(depositRequest.getDestination(), depositRequest.getAmount());
            AccountResponse accountResponse = new AccountResponse(result.getId(), result.getBalance());
            return ResponseEntity.status(HttpStatus.CREATED).body(new DepositResponse(accountResponse));
        }

        throw new IllegalArgumentException("Unsupported event type");
    }
}
