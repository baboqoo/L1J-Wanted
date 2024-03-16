package l1j.server.common.bin;

public class WireFormat {
	public static final int WRITE_EXTENDED_SIZE = 5;
	public static final int READ_EXTENDED_SIZE = 5;
	
	public static final int WIRETYPE_VARINT           = 0;
	public static final int WIRETYPE_FIXED64          = 1;
	public static final int WIRETYPE_LENGTH_DELIMITED = 2;
	public static final int WIRETYPE_START_GROUP      = 3;
	public static final int WIRETYPE_END_GROUP        = 4;
	public static final int WIRETYPE_FIXED32          = 5;

	private static final int TAG_TYPE_BITS = 3;
	private static final int TAG_TYPE_MASK = (1 << TAG_TYPE_BITS) - 1;
	
	public static int getTagWireType(final int tag){
		return tag & TAG_TYPE_MASK;
	}
	
	public static int getTagFieldNumber(final int tag){
		return tag >>> TAG_TYPE_BITS;
	}
	
	public static int makeTag(final int fieldNumber, final int wireType){
		return (fieldNumber << TAG_TYPE_BITS) | wireType;
	}
}

