/**
 * Serviços de domínio que orquestram operações sobre as contas.
 *
 * <p>Cada serviço representa um caso de uso do sistema
 * (depósito, saque, transferência, consulta de saldo, reset).
 * Os serviços são stateless e coordenam o repositório com as
 * entidades de domínio, mantendo as entidades focadas no seu
 * próprio estado.
 */
package br.com.gabrieltiziano.ipkiss.domain.service;