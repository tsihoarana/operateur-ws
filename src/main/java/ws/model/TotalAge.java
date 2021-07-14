package ws.model;

import java.math.BigDecimal;
/**
 * Structure utilisee uniquement pour des requetes de sommation
 */
public class TotalAge {
    BigDecimal age = null;
    public BigDecimal getAge () { 
        return this.age; 
    }
    public void setAge (BigDecimal age) { 
        this.age = age; 
    }
}