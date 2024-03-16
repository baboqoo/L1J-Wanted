package l1j.server.common.data;

public enum Result{
	Result_sucess(1),
	Result_fail(2),
	Result_wrong_clientip(3),
	;
	private int value;
	Result(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(Result v){
		return value == v.value;
	}
	public static Result fromInt(int i){
		switch(i){
		case 1:
			return Result_sucess;
		case 2:
			return Result_fail;
		case 3:
			return Result_wrong_clientip;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments Result, %d", i));
		}
	}
}

