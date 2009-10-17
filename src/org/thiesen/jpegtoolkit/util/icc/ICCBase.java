package org.thiesen.jpegtoolkit.util.icc;

public abstract class ICCBase {
    
    protected byte[] fromInt( final int size ) {
        final byte[] retval = new byte[4];
        
        retval[0] = (byte)(size >> 24);
        retval[1] = (byte)(size >> 16);
        retval[2] = (byte)(size >> 8);
        retval[3] = (byte)(size);
        
        return retval;
    }
    

    protected int toInt16( final byte[] tagPart ) {
        int retval = 0;
        retval += (((tagPart[0]) << 8) &  0xFF00);
        retval += ((tagPart[1])& 0xFF);
        return retval;
    }

    protected float toKpF15d16ToDouble( final byte[] tagPart ) {
        float retval = 0;
        retval += (((tagPart[0]) << 24) & 0xFF000000);
        retval += (((tagPart[1]) << 16) & 0xFF0000);
        retval += (((tagPart[2]) << 8) &  0xFF00);
        retval += ((tagPart[3])& 0xFF);
        return retval / 65536.0f;
        
        
    }
      
    protected int toInt( final byte[] tagPart ) {
        int retval = 0;
        retval += (((tagPart[0]) << 24) & 0xFF000000);
        retval += (((tagPart[1]) << 16) & 0xFF0000);
        retval += (((tagPart[2]) << 8) &  0xFF00);
        retval += ((tagPart[3])& 0xFF);
        
        return retval;
    }
    
    protected byte[] subarray( final byte[] data, final int start, final int length ) {
        final byte[] retval = new byte[ length ];
        System.arraycopy( data, start, retval, 0, length );
        return retval;
    }
    

    
}
