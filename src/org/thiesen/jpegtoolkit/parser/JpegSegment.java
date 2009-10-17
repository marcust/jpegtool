package org.thiesen.jpegtoolkit.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.thiesen.jpegtoolkit.common.HexStringUtils;
import org.thiesen.jpegtoolkit.common.JpegSegmentType;

public class JpegSegment {
    
    private JpegMarker _marker;
    private int _dataSize;
    private byte[] _data;
    private final byte[] _rawMarker;
    
    public JpegSegment( final byte[] marker ) {
        _rawMarker = marker;
        
        setMarker( JpegMarker.lookup( marker[0], marker[1] ) );
    }
    
    
    public JpegSegment( final JpegMarker marker ) {
        _rawMarker = marker.toByteArray();
        
        setMarker( marker );
    }


    public JpegMarker getMarker() {
        return _marker;
    }
    public void setMarker( final JpegMarker marker ) {
        _marker = marker;
    }
    public int getDataSize() {
        return _dataSize;
    }
    public void setDataSize( final int size ) {
        _dataSize = size;
    }
    public byte[] getData() {
        return _data;
    }
    public void setData( final byte[] data ) {
        _data = data;
    }
    
    @Override
    public String toString() {
        return ( getMarker() != null ? getMarker() : HexStringUtils.toString( _rawMarker ) ) + " (size " + getDataSize() + " byte)";
    }
    
    public int getTotalSize() {
        return 2 + JpegSegmentParser.NUM_SIZE_BYTES + getDataSize();
    }
    
    public byte[] toByteArray() {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream( getTotalSize() );
        try {
            bos.write( getMarker() != null ? getMarker().toByteArray() : _rawMarker );
            
            if ( getDataSize() != 0 && getMarker() != JpegMarker.SOS ) {
                bos.write( getSizeAsByteArray() );
            }
            
            if ( _data != null ) {
                bos.write( _data );
            }
            
            bos.flush();
            bos.close();
        } catch (final IOException e) {
            // TODO: handle exception
        }
        return bos.toByteArray();
    }
    
    public InputStream toInputStream() {
        return new ByteArrayInputStream( toByteArray() );
    }


    private byte[] getSizeAsByteArray() {
        final int size = getDataSize() + JpegSegmentParser.NUM_SIZE_BYTES;
        
        final byte firstByte = (byte)( size >> 8 );
        final byte secondByte = (byte)( size );
        
        return new byte[] { firstByte, secondByte };
    }


    public boolean hasType( final JpegSegmentType type ) {
        return getMarker() != null && getMarker().getType() == type;
        
    }


    public boolean isMarkedAs( final JpegMarker marker ) {
        return getMarker() != null && getMarker() == marker;
        
    }


    public boolean startsWith( final byte[] bytes ) {
        if ( _data == null || _data.length == 0 ) return false;
        if ( _data.length < bytes.length ) return false;
        
        for ( int i = 0; i < bytes.length; i++ ) {
            if ( _data[i] != bytes[i] ) return false;
        }
        
        return true;
    }


    public String toDisplayString() {
        final StringBuilder retval = new StringBuilder( toString() ).append( "\n");
        
        retval.append( HexStringUtils.toString( _data, 10 ) );
        
        return retval.toString();
    }
    
    public String toAsciiString() {
        final StringBuilder retval = new StringBuilder( toString() ).append( "\n");
        
        retval.append( HexStringUtils.toAsciiString( _data, 40 ) );
        
        return retval.toString();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode( _data );
        result = prime * result + Arrays.hashCode( _rawMarker );
        return result;
    }


    @Override
    public boolean equals( final Object obj ) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( !( obj instanceof JpegSegment ) ) return false;
        final JpegSegment other = (JpegSegment) obj;
        if ( !Arrays.equals( _data, other._data ) ) return false;
        if ( !Arrays.equals( _rawMarker, other._rawMarker ) ) return false;
        return true;
    }
    
    
    

}
