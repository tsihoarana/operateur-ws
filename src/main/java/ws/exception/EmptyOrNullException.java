package ws.exception;

public class EmptyOrNullException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EmptyOrNullException() {
        super("Valeur vide ou nulle");
    }
    public EmptyOrNullException (String msg) {
        super(msg);
    }
}