package l1j.server.IndunSystem.ruun;

public enum RuunRound {
	DOLL_MASTER(1),		// 인형 술사
	SHADOW_SCOUTS(2),	// 정찰 단장
	DR_UMA(3),			// 우마 박사
	MULTIPLE_FACE(4),	// 다중 가면
	PHANTOM_SWORD(5),	// 검귀
	RUUN_III(6),		// 루운 3세
	;
	protected int _type;
	RuunRound(int type) {
		_type = type;
	}
}

