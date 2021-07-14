package ws.model;

import java.math.BigDecimal;

public class Count {
    BigDecimal count;

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public Count(BigDecimal count) {
        this.count = count;
    }

    public Count() {}
    
}