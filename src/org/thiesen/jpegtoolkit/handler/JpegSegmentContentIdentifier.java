package org.thiesen.jpegtoolkit.handler;

public enum JpegSegmentContentIdentifier {

    EXIF( new byte[] { (byte)0x45, (byte)0x78, (byte)0x69, (byte)0x66, (byte)0x00, (byte)0x00 } ),
    
    IPTC( new byte[] { (byte)0x50, (byte)0x68, (byte)0x6F, (byte)0x74, 
                       (byte)0x6F, (byte)0x73, (byte)0x68, (byte)0x6F, 
                       (byte)0x70, (byte)0x20 } ),
    
    XMP( new byte[]  { (byte)0x68, (byte)0x74, (byte)0x74, (byte)0x70, 
                       (byte)0x3A, (byte)0x2F, (byte)0x2F, (byte)0x6E,
                       (byte)0x73, (byte)0x2E, (byte)0x61, (byte)0x64, 
                       (byte)0x6F, (byte)0x62, (byte)0x65, (byte)0x2E, 
                       (byte)0x63, (byte)0x6F, (byte)0x6D, (byte)0x2F, 
                       (byte)0x78, (byte)0x61, (byte)0x70, (byte)0x2F,
                       (byte)0x31, (byte)0x2E, (byte)0x30, (byte)0x2F, 
                       (byte)0x00 } ),
                       
    ICC_PROILE ( new byte[] {
            (byte)0x49, (byte)0x43, (byte)0x43, (byte)0x5F, (byte)0x50, (byte)0x52, (byte)0x4F, (byte)0x46,
            (byte)0x49, (byte)0x4C, (byte)0x45, (byte)0x00, (byte)0x01, (byte)0x01
    });
                       
                       ;
    
    private final byte[] _id;
    
    private JpegSegmentContentIdentifier( final byte[] id ) {
        _id = id;
    }

    public byte[] getBytes() {
        return _id;
        
    }
    
    
    
}
