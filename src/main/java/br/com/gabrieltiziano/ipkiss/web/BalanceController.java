package br.com.gabrieltiziano.ipkiss.web;

import br.com.gabrieltiziano.ipkiss.domain.service.BalanceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {
   private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(value = "/balance", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getBalance(@RequestParam("account_id") String accountId) {
        int balance = balanceService.getBalance(accountId);
        return ResponseEntity.ok(String.valueOf(balance));
    }
}
