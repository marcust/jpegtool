package org.thiesen.jpegtoolkit.util.icc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.thiesen.jpegtoolkit.common.HexStringUtils;

public class ICCTagTableEntry extends ICCBase {

    private final String _signature;
    private final int _offset;
    private final int _length;
    
    public ICCTagTableEntry( final String signature, final int offset, final int length ) {
        _signature = signature;
        _offset = offset;
        _length = length;
        
    }
    

    
    public byte[] toByteArray( final String signature ) {

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        try {
            stream.write( signature.getBytes( "ASCII") );
            stream.write( fromInt( _offset ) );
            stream.write( fromInt( _length ) );
            
        } catch ( final IOException e ) {
            
        }
        
        final byte[] retval = stream.toByteArray();

        if ( retval.length != 12 ) {
            System.out.println( HexStringUtils.toString( retval ));
            System.exit( 1 );
        }

        
        return retval;

        
    }
    
    public byte[] toByteArray( ) {
        return toByteArray( _signature );
    }
    
    @Override
    public String toString() {
        return "ICCTagTableEntry: signature: " + _signature + ", offset " + _offset + ", length: " + _length;
        
        
    }
}
