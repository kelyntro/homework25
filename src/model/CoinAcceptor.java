package model;

public class CoinAcceptor implements MoneyAcceptor {
    private int amount;

    public CoinAcceptor() {
        this.amount = 0;
    }

    public void setAmount(int newAmount) {
        this.amount = newAmount;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void addAmount(int amount) {
        this.amount += amount;
    }

    @Override
    public void subtractAmount(int amount) {
        this.amount -= amount;
    }
}