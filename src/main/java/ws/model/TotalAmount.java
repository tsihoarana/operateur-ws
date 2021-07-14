package ws.model;

import java.math.BigDecimal;
/**
 * Structure utilisee uniquement pour des requetes de sommation
 */
public class TotalAmount {
    BigDecimal amount = null;
    public BigDecimal getAmount () { 
        return this.amount; 
    }
    public void setAmount (BigDecimal amount) { 
        this.amount = amount; 
    }
}