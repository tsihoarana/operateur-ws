package ws.model;

/**
 * Cette classe capture l'idee de l'implementation d'un Map : String -> List<String>.
 * Entite representant une ligne de donnee de la table 'CustomMapListData'
 */
public class CustomMapListData {
    String idcustommaplistdata;
    String key;
    String value;

    public String getIdcustommaplistdata() {
        return idcustommaplistdata;
    }

    public void setIdcustommaplistdata(String idcustommaplistdata) {
        this.idcustommaplistdata = idcustommaplistdata;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CustomMapListData [idcustommaplistdata=" + idcustommaplistdata + ", key=" + key + ", value=" + value
                + "]";
    }
}