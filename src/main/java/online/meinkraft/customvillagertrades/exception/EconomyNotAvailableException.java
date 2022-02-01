package online.meinkraft.customvillagertrades.exception;

public class EconomyNotAvailableException extends Exception {

    public EconomyNotAvailableException() {
        super("No Vault compatible economy plugin available");
    }

}
