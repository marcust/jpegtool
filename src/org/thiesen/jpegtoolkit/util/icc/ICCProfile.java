package org.thiesen.jpegtoolkit.util.icc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thiesen.jpegtoolkit.handler.JpegSegmentContentIdentifier;


public class ICCProfile extends ICCBase {

    private byte[] _data;
    private byte[] _header;
    
    private final int TABLE_ENTRY_SIZE = 12;
    
    private List<ICCTag> _tags;
    
    public ICCProfile( final byte[] header ) {
        _header = header;
        _tags = new ArrayList<ICCTag>();
    }

    public ICCProfile() {}

    public void parse( final byte[] data ) {
        final int offset = JpegSegmentContentIdentifier.ICC_PROILE.getBytes().length;
        _data = new byte[ data.length - offset ];
        
        System.arraycopy( data, offset, _data, 0, data.length - offset );
        
        System.out.println("Here  " + getTagCount() );
        _tags = new ArrayList<ICCTag>( getTagCount() );
        
        _header = new byte[128];
        System.arraycopy( data, offset, _header, 0, 128 );
        
        for ( int i = 0; i < getTagCount(); i ++ ) {
            _tags.add( new ICCTag( getTagSignature( i ), i, getTagDataPart( i ) ) );
        }
        
    }

    public byte[] toByteArray() {
        final ByteArrayOutputStream retval = new ByteArrayOutputStream();
        
        try {
        
            
            final ByteArrayOutputStream tagTable = new ByteArrayOutputStream();
            tagTable.write( fromInt( _tags.size() ) );
            
            final ByteArrayOutputStream dataPartOutput = new ByteArrayOutputStream();
            int offset = (_tags.size() * TABLE_ENTRY_SIZE) + 128 + 4;
            System.err.println("Computed tag data offset " + offset );
            final Map<ICCTag, ICCTagTableEntry> entries = new HashMap<ICCTag, ICCTagTableEntry>();
            // do resuse already written bytes, this is common in most ICC headers i've seen
            for ( final ICCTag tag : _tags ) {

                boolean written = false;
                for ( final ICCTag writtenTag : entries.keySet() ) {
                    if ( writtenTag.dataEquals( tag ) ) {
                        final ICCTagTableEntry entry = entries.get( writtenTag );
                        
                        tagTable.write( entry.toByteArray( tag.getSignature() ) );
                        written = true;
                        System.out.println("Rewrote " + entry + " with sig " + tag.getSignature() );
                        
                        break;
                    }
                    
                    
                }
                
                if ( !written ) {
                    
                    final ICCTagTableEntry entry = tag.getTableEntry( offset ); 
                    
                    tagTable.write( entry.toByteArray()  );
                    
                    final byte[] dataPart = tag.getDataPart();
                    
                    dataPartOutput.write( dataPart );
                    
                    
                    System.out.println("Wrote " + entry );
                    
                    offset += dataPart.length;
                    
                    // pad to 4 byte border, as speciefied in the specs
                    while ( dataPartOutput.size() % 4 != 0 ) {
                        dataPartOutput.write( 0x00 );
                        offset++;
                    }
                    
                    
                    entries.put( tag, entry );
                }
                
                
                
                
            }
        
            System.out.println("Header size: " + retval.size() );
            System.out.println("Data size " + dataPartOutput.size() );
            
            retval.write( JpegSegmentContentIdentifier.ICC_PROILE.getBytes() );
            retval.write( fromInt( 128 + tagTable.size() + dataPartOutput.size() )   );
            retval.write( subarray( _header, 4, 124 ) );

            retval.write( tagTable.toByteArray() );
            retval.write( dataPartOutput.toByteArray() );
            
        } catch ( final IOException e ) {
            
        }
        return retval.toByteArray();
    }
    
    
    public byte[] getHeaderPart() {
        return getSubarray( 0, 127 );
    }
    
    public byte[] getTagPart() {
        return getSubarray( 128, _data.length - 1);
    }
    
    public String getDeviceClassField() {
        return asString( getSubarray( 12, 15 ) ) ;
    }
    
    public String getDataColourSpaceField() {
        return asString( getSubarray( 16, 19 ) ) ;
    }
    
    public String getProfileFileSignatureField() {
        return asString( getSubarray( 36, 39 ) );
    }

    public String getPrimaryPlatformField() {
        return asString( getSubarray( 40, 43 ) );
    }
    
    private String asString( final byte[] string ) {
        return new String( string ).replaceAll( "\0.*", "" );
    }
    
    public int getTagCount( ) {
        final byte[] tagCount = getTagPart( 0, 3 );
        return toInt( tagCount );
    }
    
    private int getTagOffset( final int number ) {
        final int retval = 4 + ( number * 12 ); 
        return  retval;
    }
    
    public String getTagSignature( final int number ) {
        return new String( getTagPart( getTagOffset( number ) , getTagOffset( number ) + 3 ) );
    }

    public int getTagDataElementOffset( final int number ) {
        return toInt( getTagPart( getTagOffset( number ) + 4, getTagOffset( number ) + 7 ) );
    }

    public int getTagDataElementSize( final int number ) {
        return toInt( getTagPart( getTagOffset( number ) + 8 , getTagOffset( number ) + 11 ) );
    }

    private byte[] getSubarray( final int start, final int end ) {
        final int length = end - start + 1;
        final byte[] retval = new byte[ length ];
        System.arraycopy( _data, start, retval, 0, length );
        return retval;
    }

    private byte[] getTagPart( final int startIncl, final int endIncl ) {
        return  getSubarray( 128 + startIncl, 128 + endIncl );
    }

    public byte[] getTagDataPart( final int tagNo ) {
        //final int tagDataOffset = getTagOffset( getTagCount() );  
        final int tagDataElementOffset = getTagDataElementOffset( tagNo );
        final int length = getTagDataElementSize( tagNo );
        
      //  System.out.println("Parsing " + tagNo + " at offset " + tagDataElementOffset + " with length " + length );
        
        final byte[] retval = new byte[ length ];
        
         System.err.println("Read data part at offest " + tagDataElementOffset + " with length " + length );
        
        System.arraycopy( _data,  tagDataElementOffset, retval,0, length );
        
        return retval;
    }
    
    public String getTagDataAsString( final int tagNo) {
        return new String( getTagDataPart( tagNo  ) );
    }
    
    public List<ICCTag> getTags() {
        return new ArrayList<ICCTag>( _tags );
    }
    
    public void addTag( final ICCTag tag ) {
        _tags.add( tag );
    }
    
    public void clearTags() {
        _tags.clear();
    }


    
}

