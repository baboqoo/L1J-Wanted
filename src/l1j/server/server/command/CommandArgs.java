package l1j.server.server.command;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ChatPacket;

public class CommandArgs {
	private L1PcInstance 	_owner;
	private String[]		_args;
	private int				_idx;
	public CommandArgs setOwner(L1PcInstance pc){
		_owner = pc;
		return this;
	}
	
	public CommandArgs setParam(String param){
		_args 	= param.split(" ");
		_idx	= 0;
		return this;
	}
	
	public L1PcInstance getOwner(){
		return _owner;
	}
	
	public int nextInt() throws Exception{
		isValidRange();
		return Integer.parseInt(_args[_idx++]);
	}
	
	public String nextString() throws Exception{
		isValidRange();
		return _args[_idx++];
	}
	
	private void isValidRange() throws Exception{
		if (_args.length <= _idx)
			throw new Exception(_args + " " + _idx);
	}
	
	public CommandArgs undo(){
		--_idx;
		return this;
	}
	
	public boolean isRange(){
		return _idx < _args.length;
	}
	
	public void notify(String message){
		if (_owner != null) {
			_owner.sendPackets(new S_ChatPacket(_owner, message));
		}
	}
	
	public void dispose(){
		_args 	= null;
		_owner 	= null;
	}
}

