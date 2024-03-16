package l1j.server.server.model.item.collection.time.construct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 실렉티스 전시회 버프 타입
 * @author LinOffice
 */
public enum L1TimeCollectionBuffType {
	SHORT(1,	"SHORT"),
	LONG(2,		"LONG"),
	MAGIC(3,	"MAGIC");
	private int _value;
	private String _name;
	L1TimeCollectionBuffType(int type, String name) {
		_value	= type;
		_name	= name;
	}
	public int toInt(){
		return _value;
	}
	public String toName(){
		return _name;
	}
	
	private static final ConcurrentHashMap<Integer, L1TimeCollectionBuffType> TYPE_DATA;
	private static final ConcurrentHashMap<String, L1TimeCollectionBuffType> NAME_DATA;
	static {
		TYPE_DATA = new ConcurrentHashMap<>();
		NAME_DATA = new ConcurrentHashMap<>();
		for(L1TimeCollectionBuffType type : L1TimeCollectionBuffType.values()){
			TYPE_DATA.put(type._value, type);
			NAME_DATA.put(type._name, type);
		}
	}
	
	public static L1TimeCollectionBuffType fromInt(int type){
		return TYPE_DATA.get(type);
	}
	
	public static L1TimeCollectionBuffType fromString(String name){
		return NAME_DATA.get(name);
	}
	
}

