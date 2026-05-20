package br.com.gabrieltiziano.ipkiss.web.dto;

public class DepositRequest extends EventRequest{
    private String destination;
    private int amount;

    public String getDestination(){
        return destination;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public int getAmount(){
        return amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }
}
