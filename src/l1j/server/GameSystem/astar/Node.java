package l1j.server.GameSystem.astar;

//******************************************************************************
// File Name	: Node.java
// Description	: 노드 클래스
// Create		: 2003/04/01 JongHa Woo
// Update		:
//******************************************************************************

public class Node {
	public int f; // f = g+h
	public int h; // 휴리스틱 값
	public int g; // 현재까지의 거리
	public int x, y; // 노드의 위치
	public Node prev; // 이전 노드
	public Node direct[]; // 인접한 노드
	public Node next; // 다음 노드

	// *************************************************************************
	// Name : Node()
	// Desc : 생성자
	// *************************************************************************
	Node() {
		direct = new Node[8];
		for (int i = 0; i < 8; i++) {
			direct[i] = null;
		}
	}

	public void close() {
		for (int i = 0; i < 8; i++) {
			direct[i] = null;
		}
		prev = next = null;
	}

	public void clear() {
		f = h = g = x = y = 0;
		prev = next = null;
		direct = null;
	}
}

