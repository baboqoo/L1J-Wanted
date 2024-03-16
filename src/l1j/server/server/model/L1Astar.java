//******************************************************************************
// File Name	: L1AStar.java
// Description	: A* 알고리즘을 사용한 길찾기 클래스
// Create		: 2003/04/01 JongHa Woo
// Update		: 2008/03/17 SiraSoni
//******************************************************************************
package l1j.server.server.model;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.Instance.L1NpcInstance;

public class L1Astar {
	// 열린 노드, 닫힌 노드 리스트
	private L1Node	OpenNode, ClosedNode;
	// 최대 루핑 회수
	private static final int LIMIT_LOOP = 1000;
	//*************************************************************************
	// Name : L1AStar()
	// Desc : 생성자
	//*************************************************************************
	public L1Astar() {
		OpenNode = null;
		ClosedNode = null;
	}
	//*************************************************************************
	// Name : ResetPath()
	// Desc : 이전에 생성된 경로를 제거
	//*************************************************************************
	public void ResetPath() {
		L1Node tmp;
		while ( OpenNode != null ) {
			tmp = OpenNode.next;
			OpenNode = null;
			OpenNode = tmp;
		}
		while ( ClosedNode != null ) {
			tmp = ClosedNode.next;
			ClosedNode = null;
			ClosedNode = tmp;
		}
	}
	//*************************************************************************
	// Name : FindPath()
	// Desc : 시작위치와 목표위치를 입력 받아 경로노드 리스트를 반환
	//*************************************************************************
	public L1Node FindPath(L1NpcInstance npc, L1Character target) {
		return FindPath(npc, target.getX(), target.getY(), target.getMapId(),target);
	}
	
	public L1Node FindPath(L1Object npc, int tx, int ty, int mapId, L1Character target) {
		L1Node	src, best = null;
		int	count = 0;
		
		src = new L1Node();
		src.g = 0;
		src.h = (tx - npc.getX()) * (tx - npc.getX()) + (ty - npc.getY()) * (ty - npc.getY());
		src.f = src.h;
		src.x = npc.getX();
		src.y = npc.getY();
		OpenNode = src;
		
		while (count < LIMIT_LOOP) {
			if (OpenNode == null)
				return best;
			best = OpenNode;
			if (best == null)
				return null;
			OpenNode = best.next;
			best.next = ClosedNode;
			ClosedNode = best;

			if (Math.max(Math.abs(tx - best.x), Math.abs(ty - best.y)) == 1)
				return best;
			if (MakeChild(best, tx, ty, npc.getMapId()) == 0 && count == 0)
				return null;
			count++;
		}
		return best;
	}
	//*************************************************************************
	// Name : MakeChild()
	// Desc : 입력받은 노드의 인접한 노드들로 확장
	//*************************************************************************
	public char MakeChild(L1Node node, int tx, int ty, short m) {
		int		x, y;
		char	flag = 0;
		char	cc[] = {0, 0, 0, 0, 0, 0, 0, 0};
		x = node.x;
		y = node.y;
		// 인접한 노드로 이동가능한지 검사
		cc[0] = IsMove(x  , y+1, m);
		cc[1] = IsMove(x-1, y+1, m);
		cc[2] = IsMove(x-1, y  , m);
		cc[3] = IsMove(x-1, y-1, m);
		cc[4] = IsMove(x  , y-1, m);
		cc[5] = IsMove(x+1, y-1, m);
		cc[6] = IsMove(x+1, y  , m);
		cc[7] = IsMove(x+1, y+1, m);
		// 이동가능한 방향이라면 노드를 생성하고 평가값 계산
		if (cc[2] == 1) {
			MakeChildSub(node, x-1, y, tx, ty);
			flag = 1;
		}
		if (cc[6] == 1) {
			MakeChildSub(node, x+1, y, tx, ty);
			flag = 1;
		}
		if (cc[4] == 1) {
			MakeChildSub(node, x, y-1, tx, ty);
			flag = 1;
		}
		if (cc[0] == 1) {
			MakeChildSub(node, x, y+1, tx, ty);
			flag = 1;
		}
		if (cc[7] == 1 && cc[6] == 1 && cc[0] == 1) {
			MakeChildSub(node, x+1, y+1, tx, ty);
			flag = 1;
		}
		if (cc[3] == 1 && cc[2] == 1 && cc[4] == 1) {
			MakeChildSub(node, x-1, y-1, tx, ty);
			flag = 1;
		}
		if (cc[5] == 1 && cc[4] == 1 && cc[6] == 1) {
			MakeChildSub(node, x+1, y-1, tx, ty);
			flag = 1;
		}
		if (cc[1] == 1 && cc[0] == 1 && cc[2] == 1) {
			MakeChildSub(node, x-1, y+1, tx, ty);
			flag = 1;
		}
		return flag;
	}
	//*************************************************************************
	// Name : IsMove()
	// Desc : 이동가능한 위치인지 검사
	//*************************************************************************
	public char IsMove(int x, int y, short mapid) {
		L1Map map = L1WorldMap.getInstance().getMap(mapid);
		if (map.isPassable(x, y) == false)
			return 0;
		return 1;
	}
	//*************************************************************************
	// Name : MakeChildSub()
	// Desc : 노드를 생성. 열린노드나 닫힌노드에 이미 있는 노드라면 
	//        이전값과 비교하여 f가 더 작으면 정보 수정
	//        닫힌노드에 있다면 그에 연결된 모든 노드들의 정보도 같이 수정
	//*************************************************************************
	public void MakeChildSub(L1Node node, int x, int y, int tx, int ty) {
		L1Node	old = null, child = null;
		int		i;
		int		g = node.g + 1;
		// 현재노드가 열린 노드에 있고 f가 더 작으면 정보 수정
		if ((old = IsOpen(x, y)) != null) {
			for (i = 0; i < 8; i++) {
				if (node.direct[i] == null) {
					node.direct[i] = old;
					break;
				}
			}
			if (g < old.g) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
			}
		}
		// 현재노드가 닫힌 노드에 있고 f가 더 작으면 정보 수정
		else if ((old = IsClosed(x, y)) != null) {
			for (i = 0; i < 8; i++) {
				if (node.direct[i] == null) {
					node.direct[i] = old;
					break;
				}
			}
			if (g < old.g) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
				// 현재노드가 닫힌노드에 있다면 그에 연결된 모든 노드들의 정보도 수정
				//MakeDown(old);
			}
		}
		// 새로운 노드라면 노드정보 생성하고 열린노드에 추가
		else {
			// 새로운 노드 생성
			child = new L1Node();
			child.prev = node;
			child.g = g;
			child.h = (x-tx)*(x-tx) + (y-ty)*(y-ty);
			child.f = child.h + child.g;
			child.x = x;
			child.y = y;
			
			// 새로운 노드를 열린노드에 추가
			InsertNode(child);

			for (i = 0; i < 8; i++) {
				if (node.direct[i] == null) {
					node.direct[i] = child;
					break;
				}
			}
		}
	}
	//*************************************************************************
	// Name : IsOpen()
	// Desc : 입력된 노드가 열린노드인지 검사
	//*************************************************************************
	public L1Node IsOpen(int x, int y) {
		L1Node tmp = OpenNode;
		while (tmp != null) {
			if (tmp.x == x && tmp.y == y)
				return tmp;
			tmp = tmp.next;
		}
		return null;
	}
	//*************************************************************************
	// Name : IsClosed()
	// Desc : 입력된 노드가 닫힌노드인지 검사
	//*************************************************************************
	public L1Node IsClosed(int x, int y) {
		L1Node tmp = ClosedNode;
		while (tmp != null) {
			if (tmp.x == x && tmp.y == y)
				return tmp;
			tmp = tmp.next;
		}
		return null;
	}
	//*************************************************************************
	// Name : InsertNode()
	// Desc : 입력된 노드를 열린노드에 f값에 따라 정렬하여 추가
	//        f값이 높은것이 제일 위에 오도록 -> 최적의 노드
	//*************************************************************************
	public void InsertNode(L1Node src) {
		L1Node old = null, tmp = null;
		if (OpenNode == null) {
			OpenNode = src;
			return;
		}
		tmp = OpenNode;
		while (tmp != null && (tmp.f < src.f)) {
			old = tmp;
			tmp = tmp.next;
		}
		if (old != null) {
			src.next = tmp;
			old.next = src;
		} else {
			src.next = tmp;
			OpenNode = src;
		}
	}
}

