package l1j.server.server.command;

import java.util.LinkedList;

public class CommandTree {
	private String						_operation;
	private String 						_description;
	private LinkedList<CommandTree> 	_command_tree;
	private String[]					_args_operations;
	public CommandTree(String operation, String description, String[] args_operations){
		_operation = operation;
		_description = description;
		_command_tree = new LinkedList<CommandTree>();
		_args_operations = args_operations;
	}
	
	public final void execute(CommandArgs args, StringBuilder command_path) {
		try {
			if(args.isRange()){
				String cmd = args.nextString();
				CommandTree next = find_command(cmd);
				if(next == null){
					to_handle_command(args.undo());
				}else{
					next.execute(args, command_path.append(" ").append(next.to_operation()));
				}
			}else{
				to_handle_command(args);
			}
		} catch (Exception e) {
			args.notify(to_description());
			args.notify(command_path.toString());
			args.notify(to_valid_operations());
			args.dispose();
		}
	}
	
	public final CommandTree find_command(String cmd){
		for(CommandTree command : _command_tree){
			if(command.to_operation().equalsIgnoreCase(cmd))
				return command;
		}
		return null;
	}
	
	public final CommandTree add_command(CommandTree command){
		_command_tree.add(command);
		return this;
	}
	
	public final String to_description(){
		return String.format("(%s)%s", to_operation(), _description);
	}
	public final String to_valid_operations(){
		StringBuilder sb = new StringBuilder(256);
		int i = 0;
		if(_args_operations == null){
			for(CommandTree command : _command_tree){
				if(i++ % 4 == 0)
					//sb.append("\r\n선택>");
					sb.append("\r\nSelect>");
				sb.append("[").append(command.to_operation()).append("]");
			}
		}else{
			for(String s : _args_operations){
				if(i++ % 4 == 0)
					//sb.append("\r\n선택>");
					sb.append("\r\nSelect>");
				sb.append("[").append(s).append("]");
			}
		}
		return sb.toString();
	}
	
	public final String to_operation(){
		return _operation;
	}
	
	protected void to_handle_command(CommandArgs args) throws Exception{
		throw new Exception();
	}
}

