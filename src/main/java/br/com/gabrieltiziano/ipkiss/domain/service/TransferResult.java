package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;

public record TransferResult(
        Account origin,
        Account destination
) {
}
