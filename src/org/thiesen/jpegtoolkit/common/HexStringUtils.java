package org.thiesen.jpegtoolkit.common;



public class HexStringUtils {
  
    
    public static String toString(final byte bs) {
        final String hex = Integer.toHexString(0x0100 + (bs  & 0x00FF)).substring(1);
        
        return "0x" + (hex.length() < 2 ? "0" : "") + hex;
    }    
    
    public static String toString( final byte... bytes ) {
        final StringBuilder builder = new StringBuilder();
        for ( final byte b : bytes ) {
            builder.append( toString( b ) ).append(" ");
        }
        return builder.toString().trim();
    }

    public static String toString( final byte[] data, final int bytesPerLine ) {
        final StringBuilder builder = new StringBuilder();
        
        int currentLineByteCount = 0;
        for ( int i = 0; i < data.length; i++ ) {
            builder.append( toString( data[i] ) ).append( " " );
            currentLineByteCount++;
            
            if ( currentLineByteCount == bytesPerLine ) {
                builder.append( "\n" );
                currentLineByteCount = 0;
            }
         }
        
        
        
        return builder.toString();
    }
    
    public static String toAsciiString( final byte[] data, final int bytesPerLine ) {
        final StringBuilder builder = new StringBuilder();
        
        int currentLineByteCount = 0;
        for ( int i = 0; i < data.length; i++ ) {
            final char c = Character.valueOf( (char)data[i] ).charValue();
            if ( Character.isLetter( c ) || Character.isDigit( c ) ) {
                builder.append( c );
            } else {
                builder.append( '.' );
            }
            currentLineByteCount++;
            
            if ( currentLineByteCount == bytesPerLine ) {
                builder.append( "\n" );
                currentLineByteCount = 0;
            }
         }
        
        
        
        return builder.toString();
    }


}
