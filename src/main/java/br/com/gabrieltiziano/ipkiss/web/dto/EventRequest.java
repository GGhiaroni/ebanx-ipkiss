package br.com.gabrieltiziano.ipkiss.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DepositRequest.class,  name = "deposit"),
        @JsonSubTypes.Type(value = WithdrawRequest.class, name = "withdraw"),
        @JsonSubTypes.Type(value = TransferRequest.class, name = "transfer")
})
public abstract class EventRequest {
}
