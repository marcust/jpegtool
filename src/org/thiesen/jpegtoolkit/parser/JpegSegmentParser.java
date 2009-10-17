package org.thiesen.jpegtoolkit.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.thiesen.jpegtoolkit.common.HexStringUtils;

public class JpegSegmentParser {

    public final static int NUM_SIZE_BYTES = 2;
    
    private final byte[] _fileData;
    
    private int _currentPos;
    
    private java.util.List<JpegSegment>  _segments;
    
    public JpegSegmentParser( final String filename ) throws IOException {
        this( new java.io.File( filename ) );
    }

    public JpegSegmentParser( final File file ) throws IOException {
        this( new FileInputStream( file ) );
        
    }

    public JpegSegmentParser( final InputStream inputStream ) throws IOException {
        int available = inputStream.available();
        
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int offset = 0;
        while ( available != 0 ) {
            final byte[] buffer = new byte[available];
            
            inputStream.read( buffer, offset, available );
            
            offset += available;
            available = inputStream.available();
            
            outputStream.write( buffer );
        }
        
        outputStream.flush();
        outputStream.close();
        
        outputStream.toByteArray();
        
        _fileData = outputStream.toByteArray();
        
    }
    
    public void parse() throws JpegSegmentParserException {
        _currentPos = 0;
        _segments = new ArrayList<JpegSegment>();
        
        expect( JpegMarker.SOI );
        _segments.add( new JpegSegment( JpegMarker.SOI ) );
        
        //System.out.println("Seen SOI");
        
        byte[] nextMarker = getNextMarker();
        
        while ( notEqual( nextMarker, JpegMarker.SOS ) ) {
            handleSegment( nextMarker );
            nextMarker = getNextMarker();
        }
        
        if ( isEqual( nextMarker, JpegMarker.SOS ) ) {
            handleEcs();

            nextMarker = getNextMarker();
            
            while ( notEqual( nextMarker, JpegMarker.EOI ) ) {
                handleSegment( nextMarker );
                nextMarker = getNextMarker();
            }
            
            
        }
  
        _segments.add( new JpegSegment( JpegMarker.EOI ) );
        
        //System.out.println("Seqments finished");
    
        //System.out.println( _segments );
        
    }


    private void handleEcs() throws JpegSegmentParserException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        final boolean run = true;
        
        while ( run ) {
            final byte b = getByte();
            
            if ( b != JpegMarker.JPEG_PUNCTATION ) {
                bos.write( readCurrentByte() );
            } else {
                final byte peekByte = getByte( 1 );
                
                final JpegMarker marker = JpegMarker.lookup( b, peekByte );
                
                if ( marker == null ) {
                    bos.write( readCurrentByte() );
                } else {
                    if ( JpegMarker.getEcsIgnoreMarker().contains( marker ) ) {
                        bos.write( readCurrentByte() );
                    } else {
                        //System.out.println("Finishing ECS because of " + marker );
                        final JpegSegment segment = new JpegSegment( JpegMarker.SOS );
                        
                        final byte[] value = bos.toByteArray();
                        segment.setDataSize( value.length );
                        segment.setData( value );
                        
                        _segments.add( segment );
                        
                        return;
                        
                    }
                }
            }
        }
    }

    private boolean notEqual( final byte[] nextMarker, final JpegMarker jpegMarker ) {
        return !jpegMarker.equals( nextMarker );
        
    }

    private boolean isEqual( final byte[] nextMarker, final JpegMarker jpegMarker ) {
        return jpegMarker.equals( nextMarker );
        
    }

    
    private byte[] getNextMarker() throws JpegSegmentParserException {
        final byte[] retval = new byte[2];
        retval[0] = readCurrentByte();
        retval[1] = readCurrentByte();
        
        return retval;
    }

    private byte getByte( final int offset ) throws JpegSegmentParserException {
        sizeCheck( offset );
        return _fileData[ _currentPos + offset ];
        
    }
    
    private void sizeCheck( final int offset ) throws JpegSegmentParserException {
        if ( !(_currentPos + offset < _fileData.length) ) {
            throw new JpegSegmentParserException("Invalid read attempt at " + (_currentPos + offset) + ",data size " + _fileData.length );
        }
        
    }

    private byte getByte( ) throws JpegSegmentParserException {
        return getByte(0);
    }

    private void handleSegment( final byte[] segment ) throws JpegSegmentParserException {
        
        final JpegSegment currentSegment = new JpegSegment( segment );
        
        final int size = getDataSize();
        
        currentSegment.setDataSize( size );
        
        if ( size >= NUM_SIZE_BYTES && _currentPos + size < _fileData.length ) {
            //System.out.println("Marker: " + HexStringUtils.toString( segment ) + ", currentPosition: " + _currentPos + ", size " + size ); 
            
            final byte[] currentValue = new byte[ size ];
            
            System.arraycopy( _fileData, _currentPos, currentValue, 0, size );
            
            currentSegment.setData( currentValue );
            
            _segments.add( currentSegment );
            
            _currentPos += size;
        } else {
            throw new JpegSegmentParserException("Invalid size in marker " + HexStringUtils.toString( segment )  + " value is  " + size );
        }
        
        
    }

    private int getDataSize() throws JpegSegmentParserException {
        final byte firstByte = readCurrentByte();
        final byte secondByte = readCurrentByte();
        
       return ((firstByte << 8) & 0xFF00) | (secondByte & 0xFF) - NUM_SIZE_BYTES;
    }

    private byte readCurrentByte() throws JpegSegmentParserException {
        sizeCheck( 0 );
        return _fileData[_currentPos++];
    }

    private void expect( final JpegMarker jpegMarker ) throws JpegSegmentParserException {
        final byte firstByte = readCurrentByte();
        final byte secondByte = readCurrentByte();
        
        if ( !jpegMarker.equals( firstByte, secondByte ) ) {
            throw new JpegSegmentParserException( "Expected marker " + jpegMarker.toString() + " (" + jpegMarker.getDisplayString() + ") but got " + HexStringUtils.toString( firstByte, secondByte  ) );
        }
    }

    public void saveToFile( final String namefilename ) throws IOException {
        saveToFile( new File( namefilename ) );
        
    }

    private void saveToFile( final File file ) throws IOException {
        saveToFile( new FileOutputStream( file ) );
        
    }

    private void saveToFile( final FileOutputStream fileOutputStream ) throws IOException {
        for ( final JpegSegment s : _segments ) {
            //System.out.println("Writing " + s );
            fileOutputStream.write( s.toByteArray() );
        }
        
        fileOutputStream.flush();
        fileOutputStream.close();
        
    }

    public List<JpegSegment> getSegmentList() {
        return Collections.unmodifiableList( _segments );
    }
      
  
    
}
