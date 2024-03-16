package l1j.server.server.utils;

public class ArrangeHelper {
	public static void cleanup(Object[] arr){
		if(arr != null){
			for(int i = arr.length - 1; i>=0; --i) 
				arr[i] = null;
			arr = null;
		}
	}
	
	public static <T> void setArrayValues(T[] arr, int start, int end, T val){
		for(int i=start; i<=end; ++i)
			arr[i] = val;
	}
	
	public static <T> int indexOf(T[] array, T val) {
		return indexOf(array, val, 0);
	}
	
	public static <T> int indexOf(T[] array, T val, int startIndex) {
		int length = array.length;
		for(int i=startIndex; i<length; ++i) {
			if(array[i] == val) {
				return i;
			}
		}
		return -1;
	}
	
	public static int indexOf(byte[] array, byte val) {
		return indexOf(array, val, 0);
	}
	
	public static int indexOf(byte[] array, byte val, int startIndex) {
		int length = array.length;
		for(int i=startIndex; i<length; ++i) {
			if(array[i] == val) {
				return i;
			}
		}
		return -1;
	}
}

