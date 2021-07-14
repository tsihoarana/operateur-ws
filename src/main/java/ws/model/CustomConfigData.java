package ws.model;

import java.math.BigDecimal;
import java.sql.Connection;

import core.afQueryable;

/**
 * Cette classe represente la table de configuration de la BDD
 * Mappe une cle vers une valeur numerique
 */
public class CustomConfigData implements afQueryable {
    String key;
    BigDecimal value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

	@Override
	public int save(Connection connection) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove(Connection connection) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

    @Override
    public String toString() {
        return "CustomConfigData [key=" + key + ", value=" + value + "]";
    }

}