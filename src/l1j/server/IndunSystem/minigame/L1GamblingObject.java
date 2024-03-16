package l1j.server.IndunSystem.minigame;

public class L1GamblingObject {
    // 주사위
    private boolean _isGambling;
    public boolean isGambling() {
    	return _isGambling;
    }
    public void setGambling(boolean flag) {
    	_isGambling = flag;
    }

    private int _gamblingmoney;
    public int getGamblingMoney() {
    	return _gamblingmoney;
    }
    public void setGamblingMoney(int i) {
    	_gamblingmoney = i;
    }

    // 소막
    private boolean _isGambling3;
    public boolean isGambling3() {
    	return _isGambling3;
    }
    public void setGambling3(boolean flag) {
    	_isGambling3 = flag;
    }

    private int _gamblingmoney3;
    public int getGamblingMoney3() {
    	return _gamblingmoney3;
    }
    public void setGamblingMoney3(int i) {
    	_gamblingmoney3 = i;
    }
}

