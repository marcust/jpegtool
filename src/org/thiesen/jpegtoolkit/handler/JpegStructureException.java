package org.thiesen.jpegtoolkit.handler;

import org.thiesen.jpegtoolkit.common.JpegException;

public class JpegStructureException extends JpegException {

    private static final long serialVersionUID = -7207830802909520954L;

    public JpegStructureException() {
        super();
    }

    public JpegStructureException( final String message, final Throwable cause ) {
        super( message, cause );
    }

    public JpegStructureException( final String message ) {
        super( message );
    }

    public JpegStructureException( final Throwable cause ) {
        super( cause );
    }
    
}
