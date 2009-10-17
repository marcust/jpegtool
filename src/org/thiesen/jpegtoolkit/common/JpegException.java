package org.thiesen.jpegtoolkit.common;

public class JpegException extends Exception {

    private static final long serialVersionUID = -5720560652030786128L;

    public JpegException() {
        super();
    }

    public JpegException( final String message, final Throwable cause ) {
        super( message, cause );
    }

    public JpegException( final String message ) {
        super( message );
    }

    public JpegException( final Throwable cause ) {
        super( cause );
    }

}
