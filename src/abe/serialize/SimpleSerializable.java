package abe.serialize;

public interface SimpleSerializable {
	static final byte ArrayMark			= '[';
	static final byte ElementMark		= 'E';
	static final byte StringMark		= 'S';
	static final byte PolicyMark		= 'P';
	static final byte IntMark			= 'I';
	static final byte TimeKeyMark		= 'T';
	static final byte LocationKeyMark	= 'L';
}
