package org.thiesen.jpegtoolkit.parser;

import java.util.EnumSet;
import java.util.Set;

import org.thiesen.jpegtoolkit.common.HexStringUtils;
import org.thiesen.jpegtoolkit.common.JpegSegmentType;

public enum JpegMarker {

    /*
     * #============================================================================#
# Constants for the grammar of a JPEG files. You can find here everything    #
# about segment markers as well as the JPEG puncutation mark.                #
#----------------------------------------------------------------------------#
our $JPEG_PUNCTUATION = 0xff; # constant prefixed to every JPEG marker       #
our %JPEG_MARKER =            # non-repetitive JPEG markers                  #
    (TEM => 0x01,  # for TEMporary private use in arithmetic coding          #
     DHT => 0xc4,  # Define Huffman Table(s)                                 #
     JPG => 0xc8,  # reserved for JPEG extensions                            #
     DAC => 0xcc,  # Define Arithmetic Coding Conditioning(s)                #
     SOI => 0xd8,  # Start Of Image                                          #
     EOI => 0xd9,  # End Of Image                                            #
     SOS => 0xda,  # Start Of Scan                                           #
     DQT => 0xdb,  # Define Quantization Table(s)                            #
     DNL => 0xdc,  # Define Number of Lines                                  #
     DRI => 0xdd,  # Define Restart Interval                                 #
     DHP => 0xde,  # Define Hierarchical Progression                         #
     EXP => 0xdf,  # EXPand reference component(s)                           #
     COM => 0xfe); # COMment block                                           #
#----------------------------------------------------------------------------#
# markers 0x02 --> 0xbf are REServed for future uses                         #
for (0x02..0xbf) { $JPEG_MARKER{sprintf "res%02x", $_} = $_; }               #
# some markers in 0xc0 --> 0xcf correspond to Start-Of-Frame typologies      #
for (0xc0..0xc3, 0xc5..0xc7, 0xc9..0xcb, 0xcd..0xcf) { $JPEG_MARKER{sprintf "SOF_%d", $_ - 0xc0} = $_; }         #
# markers 0xd0 --> 0xd7 correspond to ReSTart with module 8 count            #
for (0xd0..0xd7) { $JPEG_MARKER{sprintf "RST%d", $_ - 0xd0} = $_; }          #
# markers 0xe0 --> 0xef are the APPlication markers                          #
for (0xe0..0xef) { $JPEG_MARKER{sprintf "APP%d", $_ - 0xe0} = $_; }          #
# markers 0xf0 --> 0xfd are reserved for JPEG extensions                     #
for (0xf0..0xfd) { $JPEG_MARKER{sprintf "JPG%d", $_ - 0xf0} = $_; }          #
     */
        
    
    TEM( (byte)0x01, JpegSegmentType.UNDEFINED ), // for TEMporary use;
    DHT( (byte)0xC4, JpegSegmentType.IMAGE_DATA ), // Define Huffman Table
    JPG( (byte)0xC8, JpegSegmentType.UNDEFINED ), // reserved for JPeG extensions
    DAC( (byte)0xCC, JpegSegmentType.IMAGE_DATA ), // Define Arithmethic Coding Conditioning(s)
    SOI( (byte)0xD8, JpegSegmentType.HEADER ), // Start Of Image 
    EOI( (byte)0xD9, JpegSegmentType.HEADER ), // End Of Image
    SOS( (byte)0xDA, JpegSegmentType.IMAGE_DATA ), // Start of Scan
    DQT( (byte)0xDB, JpegSegmentType.IMAGE_DATA ), // Define Quantizaion Table
    DNL( (byte)0xDC, JpegSegmentType.IMAGE_DATA ), // Define Number Of Lines
    DRI( (byte)0xDD, JpegSegmentType.IMAGE_DATA ), // Define Restart Interval
    DHP( (byte)0xDE, JpegSegmentType.IMAGE_DATA ), // Define Hirarchical Progression
    EXP( (byte)0xDF, JpegSegmentType.IMAGE_DATA ), // EXPand reference component
    COM( (byte)0xFE, JpegSegmentType.UNDEFINED ), // COMment Blcok
    
    INVALID1( (byte)0x00, JpegSegmentType.UNDEFINED ),
    INVALID2( (byte)0xFF, JpegSegmentType.UNDEFINED ),

    APP0( (byte)0xE0, JpegSegmentType.APPLICATION_DATA ),
    APP1( (byte)0xE1, JpegSegmentType.APPLICATION_DATA ),
    APP2( (byte)0xE2, JpegSegmentType.APPLICATION_DATA ),
    APP3( (byte)0xE3, JpegSegmentType.APPLICATION_DATA ),
    APP4( (byte)0xE4, JpegSegmentType.APPLICATION_DATA ),
    APP5( (byte)0xE5, JpegSegmentType.APPLICATION_DATA ),
    APP6( (byte)0xE6, JpegSegmentType.APPLICATION_DATA ),
    APP7( (byte)0xE7, JpegSegmentType.APPLICATION_DATA ),
    APP8( (byte)0xE8, JpegSegmentType.APPLICATION_DATA ),
    APP9( (byte)0xE9, JpegSegmentType.APPLICATION_DATA ),
    APP10( (byte)0xEA, JpegSegmentType.APPLICATION_DATA ),
    APP11( (byte)0xEB, JpegSegmentType.APPLICATION_DATA ),
    APP12( (byte)0xEC, JpegSegmentType.APPLICATION_DATA ),
    APP13( (byte)0xED, JpegSegmentType.APPLICATION_DATA ),
    APP14( (byte)0xEE, JpegSegmentType.APPLICATION_DATA ),
    APP15( (byte)0xEF, JpegSegmentType.APPLICATION_DATA ),

    RST0( (byte)0xD0, JpegSegmentType.UNDEFINED ),
    RST1( (byte)0xD1, JpegSegmentType.UNDEFINED ),
    RST2( (byte)0xD2, JpegSegmentType.UNDEFINED),
    RST3( (byte)0xD3, JpegSegmentType.UNDEFINED ),
    RST4( (byte)0xD4, JpegSegmentType.UNDEFINED ),
    RST5( (byte)0xD5, JpegSegmentType.UNDEFINED ),
    RST6( (byte)0xD6, JpegSegmentType.UNDEFINED ),
    RST7( (byte)0xD7, JpegSegmentType.UNDEFINED ),
    
    SOF_0( (byte)0xC0, JpegSegmentType.IMAGE_DATA ),
    SOF_1( (byte)0xC1, JpegSegmentType.IMAGE_DATA ),
    SOF_2( (byte)0xC2, JpegSegmentType.IMAGE_DATA ),
    SOF_3( (byte)0xC3, JpegSegmentType.IMAGE_DATA ),
    SOF_5( (byte)0xC5, JpegSegmentType.IMAGE_DATA ),
    SOF_6( (byte)0xC6, JpegSegmentType.IMAGE_DATA ),
    SOF_7( (byte)0xC7, JpegSegmentType.IMAGE_DATA ),
    SOF_9( (byte)0xC9, JpegSegmentType.IMAGE_DATA ),
    SOF_10( (byte)0xCA, JpegSegmentType.IMAGE_DATA ),
    SOF_11( (byte)0xCB, JpegSegmentType.IMAGE_DATA ),
    SOF_13( (byte)0xCD, JpegSegmentType.IMAGE_DATA ),
    SOF_14( (byte)0xCE, JpegSegmentType.IMAGE_DATA ),
    SOF_15( (byte)0xCF, JpegSegmentType.IMAGE_DATA );

    
    public final static byte JPEG_PUNCTATION = (byte)0xFF;
    
    private final byte _firstByte;
    private final byte _secondByte;
    private final JpegSegmentType _type;
    
    private JpegMarker( final byte firstByte, final byte secondByte, final JpegSegmentType type ) {
        _firstByte = firstByte;
        _secondByte = secondByte;
        _type = type;

    }
    

    private JpegMarker( final byte secondByte, final JpegSegmentType type ) {
        this( JPEG_PUNCTATION, secondByte, type );
    }
    
    
    public byte getSecondByte() {
        return _secondByte;
    }


    private byte getFirstByte() {
        return _firstByte;
    }



    public static JpegMarker lookup( final byte first, final byte second ) {
        for ( final JpegMarker marker : values() ) {
            if ( marker.getFirstByte() == first && marker.getSecondByte() == second ) 
                return marker;
        }
        
        return null;
    
    }
    
    public boolean equals( final byte... bytes ) {
        if ( bytes == null || bytes.length != 2 ) return false;
        
        return bytes[0] == getFirstByte() && bytes[1] == getSecondByte();
    }
    
    public String getDisplayString() {
        return HexStringUtils.toString( getFirstByte(), getSecondByte() );
    }
    
    public static Set<JpegMarker> getEcsIgnoreMarker() {
        final Set<JpegMarker> retval = EnumSet.noneOf( JpegMarker.class );
        
        for ( final JpegMarker marker : values() ) {
            if ( marker.toString().startsWith( "INVALID" ) || marker.toString().startsWith( "RST" ) || marker.getType() == JpegSegmentType.IMAGE_DATA  ) {
                retval.add( marker );
            }
        }
        
        return retval;
        
    }


    public byte[] toByteArray() {
        return new byte[] { getFirstByte(), getSecondByte() };
        
    }


    public JpegSegmentType getType() {
        return _type;
        
    }

    
}
