package br.com.senac.exception;

/**
 * Classe de exception genérica da aplicação.
 * @author gabriel.firmino
 * @since 13/05/2025
 */
public class SenacException extends Exception{
    public SenacException() {
        super ();
    }
    public SenacException (String message) {
        super (message);
    }
    public SenacException (Throwable e ) {
        super(e);
    }
}
