package org.thiesen.jpegtoolkit.util;

import java.io.File;
import java.io.IOException;

import org.thiesen.jpegtoolkit.handler.JpegSegmentContentIdentifier;
import org.thiesen.jpegtoolkit.handler.JpegSegmentHandler;
import org.thiesen.jpegtoolkit.handler.JpegStructureException;
import org.thiesen.jpegtoolkit.parser.JpegSegment;
import org.thiesen.jpegtoolkit.parser.JpegSegmentParser;
import org.thiesen.jpegtoolkit.parser.JpegSegmentParserException;


public class RawMetadataHandler {

    public static void copyMetadata( final String fromFile, final String toFile ) throws IOException, JpegSegmentParserException, JpegStructureException {
        RawMetadataHandler.copyMetadata( new File( fromFile), new File( toFile ) );
    }

    private static void copyMetadata( final File source, final File target ) throws IOException, JpegSegmentParserException, JpegStructureException {
        final JpegSegmentHandler firstHandler = getHandler( source );
        final JpegSegmentHandler secondHandler = getHandler( target );
        
        firstHandler.setImageSegments( secondHandler.getImageSegments()  );
        
        
        firstHandler.write( target );
        
    }
    
    private static JpegSegmentHandler getHandler( final File forFile ) throws IOException, JpegSegmentParserException {
        final JpegSegmentParser parser = new JpegSegmentParser( forFile );
        
        parser.parse();
        
        return new JpegSegmentHandler( parser.getSegmentList() );
    }
    
    
    public static byte[] getXmpXmlDataFromSegment( final JpegSegment segment ) {
        final int idLength = JpegSegmentContentIdentifier.XMP.getBytes().length;
        final int xmlLength = segment.getDataSize() - idLength;

        final byte[] xmlData = new byte[ xmlLength  ];
        
        System.arraycopy( segment.getData(), idLength, xmlData, 0, xmlLength );
        
        return xmlData;
    }
    
}
