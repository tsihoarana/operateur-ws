package ws.model;

import java.math.BigDecimal;

public class BasicStats {
    BigDecimal average_age;
    BigDecimal total_credit;
    BigDecimal total_mvola;

    public BigDecimal getAverage_age() {
        return average_age;
    }

    public void setAverage_age(BigDecimal average_age) {
        this.average_age = average_age;
    }

    public BigDecimal getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(BigDecimal total_credit) {
        this.total_credit = total_credit;
    }

    public BigDecimal getTotal_mvola() {
        return total_mvola;
    }

    public void setTotal_mvola(BigDecimal total_mvola) {
        this.total_mvola = total_mvola;
    }

    @Override
    public String toString() {
        return "BasicStats [average_age=" + average_age + ", total_credit=" + total_credit + ", total_mvola="
                + total_mvola + "]";
    }
}