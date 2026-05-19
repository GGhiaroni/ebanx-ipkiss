/**
 * Abstrações de persistência e implementações em memória.
 *
 * <p>A interface {@code AccountRepository} reside aqui como
 * contrato voltado ao domínio, e a implementação em memória
 * é o único adaptador disponível no momento. Trocar o
 * armazenamento em memória por um banco de dados exigiria
 * apenas uma nova implementação dessa interface.
 */
package br.com.gabrieltiziano.ipkiss.repository;