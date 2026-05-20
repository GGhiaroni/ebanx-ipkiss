package br.com.gabrieltiziano.ipkiss.web;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.domain.service.DepositService;
import br.com.gabrieltiziano.ipkiss.domain.service.TransferResult;
import br.com.gabrieltiziano.ipkiss.domain.service.TransferService;
import br.com.gabrieltiziano.ipkiss.domain.service.WithdrawService;
import br.com.gabrieltiziano.ipkiss.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    private final DepositService depositService;
    private final WithdrawService withdrawService;
    private final TransferService transferService;

    public EventController(DepositService depositService, WithdrawService withdrawService, TransferService transferService) {
        this.depositService = depositService;
        this.withdrawService = withdrawService;
        this.transferService = transferService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> handleEvent(@RequestBody EventRequest event){
        if(event instanceof DepositRequest depositRequest){
            Account result = depositService.deposit(depositRequest.getDestination(), depositRequest.getAmount());
            AccountResponse accountResponse = new AccountResponse(result.getId(), result.getBalance());
            return ResponseEntity.status(HttpStatus.CREATED).body(new DepositResponse(accountResponse));
        }

        if (event instanceof WithdrawRequest withdrawRequest){
            Account result = withdrawService.withdraw(withdrawRequest.getOrigin(), withdrawRequest.getAmount());
            AccountResponse accountResponse = new AccountResponse(result.getId(), result.getBalance());
            return ResponseEntity.status(HttpStatus.CREATED).body(new WithdrawResponse(accountResponse));
        }

        if (event instanceof  TransferRequest transferRequest){
            TransferResult result = transferService.transfer(transferRequest.getOrigin(), transferRequest.getAmount(), transferRequest.getDestination());
            AccountResponse accountResponseOrigin = new AccountResponse(result.origin().getId(), result.origin().getBalance());
            AccountResponse accountResponseDestination = new AccountResponse(result.destination().getId(), result.destination().getBalance());
            return ResponseEntity.status(HttpStatus.CREATED).body(new TransferResponse(accountResponseOrigin, accountResponseDestination));
        }

        throw new IllegalArgumentException("Unsupported event type");
    }
}
