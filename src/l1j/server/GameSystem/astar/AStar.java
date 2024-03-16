package l1j.server.GameSystem.astar;

import java.util.List;

import javolution.util.FastTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DollInstance;

public class AStar {
	Node OpenNode, ClosedNode;// 열린 노드, 닫힌 노드 리스트
	private L1Character _cha;

	public void setCha(L1Character cha) {
		_cha = cha;
	}

	private static final int LIMIT_LOOP = 200;// 최대 루핑 회수
	private FastTable<Node> pool;
	private FastTable<Node> sabu;

	private Node getPool() {
		Node node;
		if (pool.size() > 0) {
			node = pool.get(0);
			pool.remove(0);
		} else {
			node = new Node();
		}
		return node;
	}

	private void setPool(Node node) {
		if (node != null) {
			node.close();
			if (isPoolAppend(pool, node)) {
				pool.add(node);
			}
		}
	}

	// *************************************************************************
	// Name : AStar()
	// Desc : 생성자
	// *************************************************************************
	public AStar() {
		sabu = new FastTable<Node>();
		OpenNode = null;
		ClosedNode = null;
		pool = new FastTable<Node>();
	}

	public void clear() {
		for (Node s : sabu) {
			try {
				s.close();
			} catch (Exception e) {
			}
			s.clear();
		}
		for (Node s2 : pool) {
			try {
				s2.close();
			} catch (Exception e) {
			}
			s2.clear();
		}
		OpenNode = null;
		ClosedNode = null;
		sabu.clear();
		pool.clear();
		sabu = null;
		pool = null;
	}

	// *************************************************************************
	// Name : ResetPath()
	// Desc : 이전에 생성된 경로를 제거
	// *************************************************************************
	public void cleanTail() {
		Node tmp;
		int cnt = 0;
		while (OpenNode != null) {
			cnt++;
			if (_cha != null) {
				if (_cha.isDead() || cnt > 10000) {
					return;
				}
			}
			tmp = OpenNode.next;
			setPool(OpenNode);
			OpenNode = tmp;
		}
		cnt = 0;
		while (ClosedNode != null) {
			cnt++;
			if (_cha != null) {
				if (_cha.isDead()) {
					ClosedNode = null;
					return;
				}
				if (cnt > 10000) {
					return;
				}
			}
			tmp = ClosedNode.next;
			setPool(ClosedNode);
			ClosedNode = tmp;
		}
	}

	// *************************************************************************
	// Name : FindPath()
	// Desc : 시작위치와 목표위치를 입력 받아 경로노드 리스트를 반환
	// *************************************************************************
	// 몬스터좌표 sx, xy
	// 이동할좌표 tx, ty
	public Node searchTail(L1Object o, int tx, int ty, int m, boolean obj) {
		int calcx = o.getX() - tx;
		int calcy = o.getY() - ty;
		if (o.getMapId() != m || Math.abs(calcx) > 30 || Math.abs(calcy) > 30) {
			return null;
		}
		Node src, best = null;
		int count = 0;
		int sx = o.getX();
		int sy = o.getY();

		// 처음 시작노드 생성
		src = getPool();
		src.g = 0;
		src.h = (tx - sx) * (tx - sx) + (ty - sy) * (ty - sy);
		src.f = src.h;
		src.x = sx;
		src.y = sy;

		// 시작노드를 열린노드 리스트에 추가
		OpenNode = src;

		// 길찾기 메인 루프
		// 최대 반복 회수가 넘으면 길찾기 중지
		while(count < LIMIT_LOOP){
			if (_cha != null && _cha.isDead()) {
				return null;
			}
			// 열린노드가 없다면 모든 노드를 검색했으므로 길찾기 중지
			if (OpenNode == null) {
				return null;
			}

			// 열린노드의 첫번째 노드를 가져오고 열린노드에서 제거
			best = OpenNode;
			OpenNode = best.next;

			// 가져온 노드를 닫힌노드에 추가
			best.next = ClosedNode;
			ClosedNode = best;

			// 현재 가져온 노드가 목표노드라면 길찾기 성공
			if (best.x == tx && best.y == ty) {
				return best;
			}

			// 현재 노드와 인접한 노드들로 확장하여 열린노드로 추가
			if (MakeChild(o, best, tx, ty, obj) == 0 && count == 0) {
				return null;
			}
			count++;
		}
		return null;
	}

	// *************************************************************************
	// Name : MakeChild()
	// Desc : 입력받은 노드의 인접한 노드들로 확장
	// *************************************************************************
	// 리니지 환경에 맞게 재수정 by sabu
	private char MakeChild2(L1Object o, Node node, int tx, int ty, boolean obj) {
		int x, y;
		char flag = 0;

		x = node.x;
		y = node.y;
		boolean ckckck = false;
		// 인접한 노드로 이동가능한지 검사
		for (int i = 0; i < 8; ++i) {
			if (ckckck || World.isThroughObject(x, y, o.getMapId(), i)) {
				int nx = x + getXY(i, true);
				int ny = y + getXY(i, false);
				boolean ck = true;
				// 골인지점의 좌표는 검색할필요 없음.
				if ((tx != nx || ty != ny) && obj) {
					if (o instanceof L1DollInstance) {
						ck = true;
					} else if (World.isDoorMove(x, y, o.getMapId(), i) == true) {
						ck = false;
					} else {
						ck = World.isMapdynamic(nx, ny, o.getMapId()) == false;
					}
				}
				if (ck) {
					MakeChildSub(node, nx, ny, o.getMapId(), tx, ty);
					flag = 1;
				} else if (tx != nx || ty != ny) {
					sabu.add(node);
				}
			}
		}
		return flag;
	}

	// *************************************************************************
	// Name : FindPath()
	// Desc : 근접한 위치 찾기.. 씨발 될려나
	// *************************************************************************
	// 몬스터좌표 sx, xy
	// 이동할좌표 tx, ty
	public Node FindPath2(L1Object o, int tx, int ty, int m, boolean obj) {
		int calcx = o.getX() - tx;
		int calcy = o.getY() - ty;
		if (o.getMapId() != m || Math.abs(calcx) > 30 || Math.abs(calcy) > 30) {
			return null;
		}
		Node src, best = null;
		int count = 0;
		int sx = o.getX();
		int sy = o.getY();

		// 처음 시작노드 생성
		src = getPool();
		src.g = 0;
		src.h = (tx - sx) * (tx - sx) + (ty - sy) * (ty - sy);
		src.f = src.h;
		src.x = sx;
		src.y = sy;

		// 시작노드를 열린노드 리스트에 추가
		OpenNode = src;

		// 길찾기 메인 루프
		// 최대 반복 회수가 넘으면 길찾기 중지
		while (count < LIMIT_LOOP) {
			if (_cha != null && _cha.isDead()) {
				return null;
			}
			// 열린노드가 없다면 모든 노드를 검색했으므로 길찾기 중지
			if (OpenNode == null) {
				return null;
			}

			// 열린노드의 첫번째 노드를 가져오고 열린노드에서 제거
			best = OpenNode;
			OpenNode = best.next;

			// 가져온 노드를 닫힌노드에 추가
			best.next = ClosedNode;
			ClosedNode = best;

			// 현재 가져온 노드가 목표노드라면 길찾기 성공
			if (best.x == tx && best.y == ty) {
				return best;
			}

			// 현재 노드와 인접한 노드들로 확장하여 열린노드로 추가
			if (MakeChild2(o, best, tx, ty, obj) == 0 && count == 0) {
				return null;
			}
			count++;
		}
		int tmpdis = 0;
		for (Node saNode : sabu) {
			int x = saNode.x;
			int y = saNode.y;
			saNode.h = (tx - x) * (tx - x) + (ty - y) * (ty - y);
			if (tmpdis == 0) {
				best = saNode;
				tmpdis = saNode.h;
			}
			if (tmpdis > saNode.h) {
				best = saNode;
				tmpdis = saNode.h;
			}
		}

		if (best == null || best.h >= (tx - sx) * (tx - sx) + (ty - sy) * (ty - sy)) {
			return null;
		}
		if (sabu.size() > 0) {
			sabu.clear();
		}
		return best;
	}

	// *************************************************************************
	// Name : MakeChild()
	// Desc : 입력받은 노드의 인접한 노드들로 확장
	// *************************************************************************
	// 리니지 환경에 맞게 재수정 by sabu

	private char MakeChild(L1Object o, Node node, int tx, int ty, boolean obj) {
		int x, y;
		char flag = 0;

		x = node.x;
		y = node.y;
		boolean ckckck = false;// 인접한 노드로 이동가능한지 검사

		for (int i = 0; i < 8; ++i) {
			if (ckckck || World.isThroughObject(x, y, o.getMapId(), i)) {
				int nx = x + getXY(i, true);
				int ny = y + getXY(i, false);
				boolean ck = true;
				// 골인지점의 좌표는 검색할필요 없음.
				if ((tx != nx || ty != ny) && obj) {
					if (o instanceof L1DollInstance) {
						ck = true;
					} else if (World.isDoorMove(x, y, o.getMapId(), i) == true) {
						ck = false;
					} else {
						ck = World.isMapdynamic(nx, ny, o.getMapId()) == false;
					}
				}
				if (ck) {
					MakeChildSub(node, nx, ny, o.getMapId(), tx, ty);
					flag = 1;
				}
			}
		}
		return flag;
	}

	// *************************************************************************
	// Name : MakeChildSub()
	// Desc : 노드를 생성. 열린노드나 닫힌노드에 이미 있는 노드라면
	// 이전값과 비교하여 f가 더 작으면 정보 수정
	// 닫힌노드에 있다면 그에 연결된 모든 노드들의 정보도 같이 수정
	// *************************************************************************
	private void MakeChildSub(Node node, int x, int y, int m, int tx, int ty) {
		Node old = null, child = null;
		int g = node.g + 1;
		// 현재노드가 열린 노드에 있고 f가 더 작으면 정보 수정
		if ((old = IsOpen(x, y, m)) != null) {
			if (g < old.g) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
			}

			// 현재노드가 닫힌 노드에 있고 f가 더 작으면 정보 수정
		} else if ((old = IsClosed(x, y, m)) != null) {
			if (g < old.g) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
			}
			// 새로운 노드라면 노드정보 생성하고 열린노드에 추가
		} else {
			try {
				// 새로운 노드 생성
				child = getPool();
				child.prev = node;
				child.g = g;
				child.h = (x - tx) * (x - tx) + (y - ty) * (y - ty);
				child.f = child.h + child.g;
				child.x = x;
				child.y = y;

				// 새로운 노드를 열린노드에 추가
				InsertNode(child);
			} catch (Exception e) {
			}
		}
	}

	// *************************************************************************
	// Name : IsOpen()
	// Desc : 입력된 노드가 열린노드인지 검사
	// *************************************************************************
	private Node IsOpen(int x, int y, int mapid) {
		Node tmp = OpenNode;
		int cnt = 0;
		while (tmp != null) {
			cnt++;
			if (_cha != null) {
				if (_cha.isDead() || cnt > 10000) {
					return null;
				}
			}
			if (tmp.x == x && tmp.y == y) {
				return tmp;
			}
			tmp = tmp.next;
		}
		return null;
	}

	// *************************************************************************
	// Name : IsClosed()
	// Desc : 입력된 노드가 닫힌노드인지 검사
	// *************************************************************************
	private Node IsClosed(int x, int y, int mapid) {
		Node tmp = ClosedNode;
		int cnt = 0;
		while (tmp != null) {
			cnt++;
			if (_cha != null) {
				if(_cha.isDead() || cnt > 10000) {
					return null;
				}
			}
			if (tmp.x == x && tmp.y == y) {
				return tmp;
			}
			tmp = tmp.next;
		}
		return null;
	}

	// *************************************************************************
	// Name : InsertNode()
	// Desc : 입력된 노드를 열린노드에 f값에 따라 정렬하여 추가
	// f값이 높은것이 제일 위에 오도록 -> 최적의 노드
	// *************************************************************************
	private void InsertNode(Node src) {
		Node old = null, tmp = null;
		int cnt = 0;
		if (OpenNode == null) {
			OpenNode = src;
			return;
		}
		tmp = OpenNode;
		while (tmp != null && (tmp.f < src.f)) {
			cnt++;
			if (_cha != null) {
				if (_cha.isDead() || cnt > 10000) {
					return;
				}
			}
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

	/**
	 * 풀링에 추가해도되는지 확인해주는 함수. : 너무 많이 등록되면 문제가 되기대문에 적정선으로 카바.. :
	 * java.lang.OutOfMemoryError: Java heap space
	 * 
	 * @param c
	 * @return
	 */
	private boolean isPoolAppend(List<?> pool, Object c) {
		return pool.size() < 200;// 전체 갯수로 체크.
	}

	/**
	 * 방향과 타입에따라 적절하게 좌표값세팅 리턴
	 * 
	 * @param h
	 *            : 방향
	 * @param type
	 *            : true ? x : y
	 * @return
	 */
	public int getXY(final int h, final boolean type) {
		int loc = 0;
		switch (h) {
		case 0:
			if (!type) {
				loc -= 1;
			}
			break;
		case 1:
			if (type) {
				loc += 1;
			} else {
				loc -= 1;
			}
			break;
		case 2:
			if (type) {
				loc += 1;
			}
			break;
		case 3:
			loc += 1;
			break;
		case 4:
			if (!type) {
				loc += 1;
			}
			break;
		case 5:
			if (type) {
				loc -= 1;
			} else {
				loc += 1;
			}
			break;
		case 6:
			if (type) {
				loc -= 1;
			}
			break;
		case 7:
			loc -= 1;
			break;
		}
		return loc;
	}

	public int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy) {
			return 3;
		}
		if (tx < myx && ty < myy) {
			return 7;
		}
		if (tx > myx && ty == myy) {
			return 2;
		}
		if (tx < myx && ty == myy) {
			return 6;
		}
		if (tx == myx && ty < myy) {
			return 0;
		}
		if (tx == myx && ty > myy) {
			return 4;
		}
		if (tx < myx && ty > myy) {
			return 5;
		}
		return 1;
	}

}
