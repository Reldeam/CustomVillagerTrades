
package online.meinkraft.customvillagertrades.exception;

public class VillagerNotMerchantException extends Exception {

    public VillagerNotMerchantException() {
        super("Villager is not a merchant (trader)");
    }

}
