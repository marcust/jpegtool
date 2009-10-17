package org.thiesen.jpegtoolkit.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.thiesen.jpegtoolkit.common.JpegSegmentType;
import org.thiesen.jpegtoolkit.parser.JpegMarker;
import org.thiesen.jpegtoolkit.parser.JpegSegment;


public class JpegSegmentHandler {

    private final List<JpegSegment> _segments;
    
    public JpegSegmentHandler( final List<JpegSegment> segmentList ) {
        _segments = new ArrayList<JpegSegment>( segmentList );
    }

    public List<JpegSegment> getImageSegments() {
        return getSegmentsByType( JpegSegmentType.IMAGE_DATA );
    }
    
    public List<JpegSegment> getHeaderSegments() {
        return getSegmentsByType( JpegSegmentType.HEADER );
    }

    public List<JpegSegment> getApplicationSegments() {
        return getSegmentsByType( JpegSegmentType.APPLICATION_DATA );
    }


    private List<JpegSegment> getSegmentsByType( final JpegSegmentType type ) {
        final List<JpegSegment> retval = new ArrayList<JpegSegment>( _segments.size() );
        
        for ( final JpegSegment segment : _segments ) {
            if ( segment.hasType( type ) ) {
                retval.add( segment );
            }
        }
        
        return retval;
    }

    public void setImageSegments( final List<JpegSegment> imageSegments ) throws JpegStructureException {
        removeImageSegments();
        removeHeaderSegments();
        _segments.addAll( imageSegments );
        addHeader();
        checkSane();
    }

    private void addHeader() {
        _segments.add( 0, new JpegSegment( JpegMarker.SOI) );
        _segments.add( new JpegSegment( JpegMarker.EOI ) );
    }

    public void write( final String filename ) throws FileNotFoundException, IOException, JpegStructureException {
        write( new File( filename ) );
    }
    
    public void write( final File target ) throws FileNotFoundException, IOException, JpegStructureException {
        checkSane();
        write( new FileOutputStream( target ) );
    }
    
   public void write( final FileOutputStream fileOutputStream ) throws IOException, JpegStructureException {
       checkSane();
        for ( final JpegSegment s : _segments ) {
            //System.out.println("Writing " + s );
            fileOutputStream.write( s.toByteArray() );
        }
        
        fileOutputStream.flush();
        fileOutputStream.close();
    }
    
    public void removeImageSegments() {
        final List<JpegSegment> imageData = getImageSegments();
        _segments.removeAll( imageData );
    }

    public void removeHeaderSegments() {
        final List<JpegSegment> imageData = getHeaderSegments();
        _segments.removeAll( imageData );
    }

    
    public List<JpegSegment> getSegmentsByContent( final JpegSegmentContentIdentifier contentId ) {
        final List<JpegSegment> retval = new ArrayList<JpegSegment>();
        for ( final JpegSegment s : getApplicationSegments() ) {
            if ( s.startsWith( contentId.getBytes() ) ) {
                retval.add( s );
            }
        }
        return retval;
    }
    

    public List<JpegSegment> getExifSegments() {
        return getSegmentsByContent( JpegSegmentContentIdentifier.EXIF );
    }
    
    public List<JpegSegment> getIptcSegments() {
        return getSegmentsByContent( JpegSegmentContentIdentifier.IPTC );
    }
    
    public List<JpegSegment> getXmpSegments() {
        return getSegmentsByContent( JpegSegmentContentIdentifier.XMP );
    }
    
    public List<JpegSegment> getICCProfileSegements() {
        return getSegmentsByContent( JpegSegmentContentIdentifier.ICC_PROILE );
    }
    
    public List<JpegSegment> getSegmentsByMarker( final JpegMarker marker ) {
        final List<JpegSegment> retval = new ArrayList<JpegSegment>();
        for ( final JpegSegment s : _segments ) {
            if ( s.getMarker() == marker ) retval.add( s );
        }
        return retval;
        
    }
    
    
    private void checkSane() throws JpegStructureException {
        if ( _segments.size() >  0 ) {
            final JpegSegment segment = _segments.get( 0 );
            if ( segment.getMarker() != JpegMarker.SOI ) {
                throw new JpegStructureException("Current Stream does not start with SOI: " + _segments );
            }
        
            final JpegSegment eoi = _segments.get( _segments.size() - 1 );
            if ( eoi.getMarker() != JpegMarker.EOI ) {
                throw new JpegStructureException("Current Stream does not end with EOI: " + _segments );
            }
            
        }
    }

    public void removeSegments( final List<JpegSegment> segments ) {
        _segments.removeAll( segments );
    }
    

    public void replaceSegment( final JpegSegment original, final JpegSegment replacement ) {
        final int pos = _segments.indexOf( original );
        _segments.set( pos, replacement );
    }
    
    
}
