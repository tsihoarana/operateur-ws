package ws.exception;

import java.math.BigDecimal;

public class NotEnoughMvolaException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public NotEnoughMvolaException(BigDecimal current_amount, String message) {
        super("Mvola insuffisant comme " + current_amount + " est inferieur. " + message);
    }
    public NotEnoughMvolaException(BigDecimal current_amount) {
        super("Mvola insuffisant comme " + current_amount + " est inferieur.");
    }
}