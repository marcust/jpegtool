package org.thiesen.jpegtoolkit.util.icc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.thiesen.jpegtoolkit.common.HexStringUtils;

public class ICCTag extends ICCBase {

    private final int _pos;
    private final String _type;
    private final byte[] _data;
    private final String _signature;
    
    private static int COUNTER = 0;
    
    public ICCTag( final String signature, final int pos, final byte[] data ) {
        COUNTER += data.length;
        System.out.println("Tag " + signature + " has a data part of length " + data.length );
        System.out.println("All data length: " + COUNTER );
        _signature = signature;
        _pos = pos;
        final byte[] type = new byte[4];
        
        System.arraycopy( data, 0, type, 0, 4 );
        
        _type = new String( type );
        
        
        _data = new byte[ data.length - 8 ];
        
        System.arraycopy( data, 8, _data, 0, data.length - 8 );
    }
    
    public int getPos() {
        return _pos;
    }
    
    public String getType() {
        return _type;
    }
    
    public byte[] getData() {
        
        return _data;
    }
    
    public String toHexString() {
        return HexStringUtils.toString( getData(), 8 );
    }
    
    public String toAsciiString() {
        return new String( getData() );
    }
    
    private byte[] getBytes( final int start, final int length ) {
        return subarray( _data, start, length );
    }
 

    
    @Override
    public String toString() {
        final StringBuilder retval = new StringBuilder();
        if ( _type.equals( "XYZ " ) ) {
            int offset = 0;
            
            while ( offset < getData().length ) {

                _data[offset] = 0x00;
                _data[offset+1] = 0x00;
                _data[offset+2] = 0x01;
                _data[offset+3] = 0x00;
                
                retval.append("CIE X:\t").append( toKpF15d16ToDouble( getBytes( offset, 4 ) )).append("\n");
                offset += 4;
                
                
                _data[offset] = 0x00;
                _data[offset+1] = 0x00;
                _data[offset+2] = 0x01;
                _data[offset+3] = 0x00;

                retval.append("CIE Y:\t").append( toKpF15d16ToDouble( getBytes( offset, 4 ) )).append("\n");
                offset += 4;
                
                _data[offset] = 0x00;
                _data[offset+1] = 0x00;
                _data[offset+2] = 0x01;
                _data[offset+3] = 0x00;

                retval.append("CIE Z:\t").append( toKpF15d16ToDouble( getBytes( offset, 4 ) )).append("\n");
                offset += 4;

            }
        } else 
        if ( _type.equals( "para" ) ) {
            retval.append("Function Type: " + HexStringUtils.toString( getBytes( 0, 2 ) ) ).append( "\n" );

            final int functionType = toInt16( getBytes( 0, 2 ) );
            int paramCount = -1;
            
            switch ( functionType ) {
                case 0: paramCount = 1; break;
                case 1: paramCount = 3; break;
                case 2: paramCount = 4; break;
                case 3: paramCount = 5; break;
                case 4: paramCount = 7; break; 
            }
            
            retval.append( "Expected ").append( paramCount ).append( " params\n");
            
            if ( paramCount == -1 ) {
                retval.append( "Can not determine param count for function " + functionType + " \n");
            } else {
                final int restLength = _data.length - 4;
                final int bytesExpected = paramCount * 4;
                if ( bytesExpected != restLength ) {
                    retval.append("FATAL: expected " + bytesExpected + " param bytes, but length is only " + restLength+ " bytes\n" );
                }
            }
            
            final int offset = 4;
            retval.append( handleS15Fixed16ArrayType(getBytes( offset, getData().length - offset ) ) );
            
            
        } else 
        if ( _type.equals( "mluc" ) ) {
            final int numberOfNames = toInt( getBytes( 0, 4 ) );
            
            retval.append( "Number of names " +  numberOfNames ).append( "\n");
            retval.append( "Name record size " + toInt( getBytes( 4, 4 ) ) ).append( "\n");
            retval.append( "ISO-639 Lang code " + toInt16( getBytes( 8, 2 ) ) ).append( "\n");
            retval.append( "ISO-3166 Country code " + toInt16( getBytes( 10, 2 ) ) ).append( "\n");
            
            final int length = toInt( getBytes( 12, 4 ) );
            final int offset = toInt( getBytes( 16, 4 ) );
            
            retval.append( "Offset: " + offset ).append( "\n");
            retval.append( "Length: " + length ).append( "\n");
            
            final Charset c = Charset.forName( "UTF-16" );
            
            retval.append( "Value:\n").append( c.decode( ByteBuffer.wrap(  getBytes( offset - 8  , length  ) ) ) );
            
            
            
            
            
            
            
        } else {
            retval.append( toHexString() );
        }
        
        
        return retval.toString();
    }

    private String handleS15Fixed16ArrayType( final byte[] bytes ) {
        final StringBuilder retval = new StringBuilder();
        int offset = 0;
        int count= 0;
        while ( offset < bytes.length ) {
            for ( int i = offset; i < offset + 4; i++ ) {
                _data[i] = 0x00;
            }
            
            retval.append( "[").append( count++ ).append("]: ").append( toKpF15d16ToDouble( getBytes( offset, 4 ) )).append("\n");
            offset += 4;
            
            
        }
        
        
        return retval.toString();
    }

    public String getDisplayName() {
        return _signature + " (Pos " + getPos() + ")";
        
    }

    public ICCTagTableEntry getTableEntry( final int offset ) {
            return new ICCTagTableEntry( _signature, offset, getDataPart().length );
                
        
    }
    

    public byte[] getDataPart() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        try {
            if ( _type.getBytes( "ASCII" ).length > 4 ) {
                System.out.println( _type );
                System.exit( 1 );
            }
            
            stream.write( _type.getBytes( "ASCII" ));
            stream.write( new byte[] { 0x00, 0x00, 0x00, 0x00 }  );
            stream.write( _data );

        } catch ( final IOException e ) {
        }
        
       final byte[] retval = stream.toByteArray();
        System.out.println("Created data part for tag " + _signature + " is " + retval.length );
        
        
        return retval;
        
    }
    
    public boolean dataEquals( final ICCTag anotherTag ) {
        if ( anotherTag == null ) return false;
        return Arrays.equals( getData(), anotherTag.getData() );
    }

    public String getSignature() {
        return _signature;
        
    }
    
    
    
}
