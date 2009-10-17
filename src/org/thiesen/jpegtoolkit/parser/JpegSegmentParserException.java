package org.thiesen.jpegtoolkit.parser;

import org.thiesen.jpegtoolkit.common.JpegException;

public class JpegSegmentParserException extends JpegException {

    private static final long serialVersionUID = 1110403875117112220L;

    public JpegSegmentParserException() {
        super();
    }

    public JpegSegmentParserException( final String message, final Throwable cause ) {
        super( message, cause );
    }

    public JpegSegmentParserException( final String message ) {
        super( message );
    }

    public JpegSegmentParserException( final Throwable cause ) {
        super( cause );
    }

    
}
