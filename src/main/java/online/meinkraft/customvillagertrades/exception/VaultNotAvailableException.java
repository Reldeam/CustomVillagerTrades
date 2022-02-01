package online.meinkraft.customvillagertrades.exception;

public class VaultNotAvailableException extends Exception {

    public VaultNotAvailableException() {
        super("Vault dependency not available");
    }

}
