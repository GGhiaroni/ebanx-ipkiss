package br.com.gabrieltiziano.ipkiss.web.dto;

public record TransferResponse(
        AccountResponse origin,
        AccountResponse destination
) {
}
