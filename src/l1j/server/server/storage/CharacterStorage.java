package l1j.server.server.storage;

import l1j.server.server.model.Instance.L1FakePcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public interface CharacterStorage {
	public void createCharacter(L1PcInstance pc) throws Exception;
	public void deleteCharacter(String accountName, String charName) throws Exception;
	public void storeCharacter(L1PcInstance pc) throws Exception;
	public L1FakePcInstance loadFakeCharacter(String charName) throws Exception;
	public void updateAccountName(L1PcInstance pc) throws Exception;
	public L1PcInstance loadCharacter(String charName) throws Exception;
}

