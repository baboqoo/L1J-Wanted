//******************************************************************************
// File Name	: L1Node.java
// Description	: 노드 클래스
// Create		: 2003/04/01 JongHa Woo
// Update		: 2008/03/17 SiraSoni
//******************************************************************************
package l1j.server.server.model;

public class L1Node {
	public int f;				// f = g+h
	public int h;				// 휴리스틱 값
	public int g;				// 현재까지의 거리
	public int x, y;			// 노드의 위치
	public L1Node prev;			// 이전 노드
	public L1Node	direct[];	// 인접한 노드
	public L1Node	next;		// 다음 노드
	
	//*************************************************************************
	// Name : L1Node()
	// Desc : 생성자
	//*************************************************************************
	public L1Node() {
		direct = new L1Node[8];
		
		for (int i = 0; i < 8; i++) {
			direct[i] = null;
		}
	}
}


