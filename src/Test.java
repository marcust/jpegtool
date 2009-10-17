import java.io.IOException;
import java.util.List;

import org.thiesen.jpegtoolkit.common.HexStringUtils;
import org.thiesen.jpegtoolkit.handler.JpegSegmentHandler;
import org.thiesen.jpegtoolkit.handler.JpegStructureException;
import org.thiesen.jpegtoolkit.parser.JpegSegment;
import org.thiesen.jpegtoolkit.parser.JpegSegmentParser;
import org.thiesen.jpegtoolkit.parser.JpegSegmentParserException;
import org.thiesen.jpegtoolkit.util.icc.ICCProfile;
import org.thiesen.jpegtoolkit.util.icc.ICCTag;

public class Test {
    
    public static void main( final String[] args ) throws IOException, JpegSegmentParserException, JpegStructureException {
        final String filename = args[0];
        
        final JpegSegmentParser reader = new JpegSegmentParser( filename );
        
        reader.parse();
        
        final JpegSegmentHandler handler = new JpegSegmentHandler( reader.getSegmentList() );
        
        final List<JpegSegment> icc = handler.getICCProfileSegements();
        final int count  = 0;
        if ( icc != null ) {
            for ( final JpegSegment s : icc ) {
        
                
                final ICCProfile profile = new ICCProfile();
                
                profile.parse( s.getData() );
                /*           
                for ( final ICCTag tag : profile.getTags() ) {
                    
                    System.out.println( tag.toString() );
                    
                    
                    
                }
                
                
                
                
            }
            
      */
               final List<ICCTag> tags = profile.getTags();
               
               profile.clearTags();
               
               for ( final ICCTag tag : tags ) {
                   if ( "cprt".equals( tag.getSignature() ) ) continue;
                   if ( "desc".equals( tag.getSignature() ) ) continue;
                   if ( "wtpt".equals( tag.getSignature() ) ) continue;
                   //if ( "rXYZ".equals( tag.getSignature() ) ) continue;
                   //if ( "gXYZ".equals( tag.getSignature() ) ) continue;
                   //if ( "bXYZ".equals( tag.getSignature() ) ) continue;
                   //if ( "rTRC".equals( tag.getSignature() ) ) continue;
                   //if ( "gTRC".equals( tag.getSignature() ) ) continue;
                   //if ( "bTRC".equals( tag.getSignature() ) ) continue;
                   profile.addTag( tag );
                   
                   System.out.println( tag );
               }
               
                
                
                
                final byte[] newData = profile.toByteArray();
                s.setData( newData );
                s.setDataSize( newData.length );
                
                System.out.println( HexStringUtils.toString( newData, 8 ) );
                
                
                
                handler.write( "/tmp/copy.jpg");
                
                
                
            }
        }
    }
}
