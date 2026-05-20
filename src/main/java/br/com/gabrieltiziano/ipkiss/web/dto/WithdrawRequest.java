package br.com.gabrieltiziano.ipkiss.web.dto;

public class WithdrawRequest extends EventRequest{
    private String origin;
    private int amount;

    public String getOrigin(){
        return origin;
    }

    public void setOrigin(String origin){
        this.origin = origin;
    }

    public int getAmount(){
        return amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }
}
