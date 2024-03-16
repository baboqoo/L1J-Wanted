package l1j.server.common.bin.quest;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.CharacterClass;
import l1j.server.common.data.OptionalRewardT;
import l1j.server.common.data.RegionT;
import l1j.server.common.data.RewardListT;

public class QuestT implements ProtoMessage{
	public static QuestT newInstance(){
		return new QuestT();
	}
	private int _ID;// 퀘스트 번호
	private boolean _AutoActive;// 자동 활성화
	private QuestT.DescriptionT _Description;// 묘사
	private QuestT.PrerequisiteT _Prerequisite;// 전재조건
	private QuestT.ObjectiveListT _ObjectiveList;
	private RewardListT _AdvanceRewardList;// 사전 보상
	private RewardListT _RewardList;// 보상
	private OptionalRewardT _OptionalRewardList;// 선택적 보상
	private QuestT.TeleportT _Teleport;// 텔레포트
	private boolean _Obsolete;// 사용하지 않음
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private QuestT(){
	}
	public int get_ID(){
		return _ID;
	}
	public void set_ID(int val){
		_bit |= 0x1;
		_ID = val;
	}
	public boolean has_ID(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_AutoActive(){
		return _AutoActive;
	}
	public void set_AutoActive(boolean val){
		_bit |= 0x2;
		_AutoActive = val;
	}
	public boolean has_AutoActive(){
		return (_bit & 0x2) == 0x2;
	}
	public QuestT.DescriptionT get_Description(){
		return _Description;
	}
	public void set_Description(QuestT.DescriptionT val){
		_bit |= 0x4;
		_Description = val;
	}
	public boolean has_Description(){
		return (_bit & 0x4) == 0x4;
	}
	public QuestT.PrerequisiteT get_Prerequisite(){
		return _Prerequisite;
	}
	public void set_Prerequisite(QuestT.PrerequisiteT val){
		_bit |= 0x8;
		_Prerequisite = val;
	}
	public boolean has_Prerequisite(){
		return (_bit & 0x8) == 0x8;
	}
	public QuestT.ObjectiveListT get_ObjectiveList(){
		return _ObjectiveList;
	}
	public void set_ObjectiveList(QuestT.ObjectiveListT val){
		_bit |= 0x10;
		_ObjectiveList = val;
	}
	public boolean has_ObjectiveList(){
		return (_bit & 0x10) == 0x10;
	}
	public RewardListT get_AdvanceRewardList(){
		return _AdvanceRewardList;
	}
	public void set_AdvanceRewardList(RewardListT val){
		_bit |= 0x20;
		_AdvanceRewardList = val;
	}
	public boolean has_AdvanceRewardList(){
		return (_bit & 0x20) == 0x20;
	}
	public RewardListT get_RewardList(){
		return _RewardList;
	}
	public void set_RewardList(RewardListT val){
		_bit |= 0x40;
		_RewardList = val;
	}
	public boolean has_RewardList(){
		return (_bit & 0x40) == 0x40;
	}
	public OptionalRewardT get_OptionalRewardList(){
		return _OptionalRewardList;
	}
	public void set_OptionalRewardList(OptionalRewardT val){
		_bit |= 0x80;
		_OptionalRewardList = val;
	}
	public boolean has_OptionalRewardList(){
		return (_bit & 0x80) == 0x80;
	}
	public QuestT.TeleportT get_Teleport(){
		return _Teleport;
	}
	public void set_Teleport(QuestT.TeleportT val){
		_bit |= 0x100;
		_Teleport = val;
	}
	public boolean has_Teleport(){
		return (_bit & 0x100) == 0x100;
	}
	public boolean get_Obsolete(){
		return _Obsolete;
	}
	public void set_Obsolete(boolean val){
		_bit |= 0x200;
		_Obsolete = val;
	}
	public boolean has_Obsolete(){
		return (_bit & 0x200) == 0x200;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_ID()){
			size += ProtoOutputStream.computeUInt32Size(1, _ID);
		}
		if (has_AutoActive()){
			size += ProtoOutputStream.computeBoolSize(2, _AutoActive);
		}
		if (has_Description()){
			size += ProtoOutputStream.computeMessageSize(3, _Description);
		}
		if (has_Prerequisite()){
			size += ProtoOutputStream.computeMessageSize(4, _Prerequisite);
		}
		if (has_ObjectiveList()){
			size += ProtoOutputStream.computeMessageSize(5, _ObjectiveList);
		}
		if (has_AdvanceRewardList()){
			size += ProtoOutputStream.computeMessageSize(6, _AdvanceRewardList);
		}
		if (has_RewardList()){
			size += ProtoOutputStream.computeMessageSize(7, _RewardList);
		}
		if (has_OptionalRewardList()){
			size += ProtoOutputStream.computeMessageSize(8, _OptionalRewardList);
		}
		if (has_Teleport()){
			size += ProtoOutputStream.computeMessageSize(9, _Teleport);
		}
		if (has_Obsolete()){
			size += ProtoOutputStream.computeBoolSize(10, _Obsolete);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_ID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_ID()){
			output.writeUInt32(1, _ID);
		}
		if (has_AutoActive()){
			output.writeBool(2, _AutoActive);
		}
		if (has_Description()){
			output.writeMessage(3, _Description);
		}
		if (has_Prerequisite()){
			output.writeMessage(4, _Prerequisite);
		}
		if (has_ObjectiveList()){
			output.writeMessage(5, _ObjectiveList);
		}
		if (has_AdvanceRewardList()){
			output.writeMessage(6, _AdvanceRewardList);
		}
		if (has_RewardList()){
			output.writeMessage(7, _RewardList);
		}
		if (has_OptionalRewardList()){
			output.writeMessage(8, _OptionalRewardList);
		}
		if (has_Teleport()){
			output.writeMessage(9, _Teleport);
		}
		if (has_Obsolete()){
			output.writeBool(10, _Obsolete);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_ID(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_AutoActive(input.readBool());
					break;
				}
				case 0x0000001A:{
					set_Description((QuestT.DescriptionT)input.readMessage(QuestT.DescriptionT.newInstance()));
					break;
				}
				case 0x00000022:{
					set_Prerequisite((QuestT.PrerequisiteT)input.readMessage(QuestT.PrerequisiteT.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_ObjectiveList((QuestT.ObjectiveListT)input.readMessage(QuestT.ObjectiveListT.newInstance()));
					break;
				}
				case 0x00000032:{
					set_AdvanceRewardList((RewardListT)input.readMessage(RewardListT.newInstance()));
					break;
				}
				case 0x0000003A:{
					set_RewardList((RewardListT)input.readMessage(RewardListT.newInstance()));
					break;
				}
				case 0x00000042:{
					set_OptionalRewardList((OptionalRewardT)input.readMessage(OptionalRewardT.newInstance()));
					break;
				}
				case 0x0000004A:{
					set_Teleport((QuestT.TeleportT)input.readMessage(QuestT.TeleportT.newInstance()));
					break;
				}
				case 0x00000050:{
					set_Obsolete(input.readBool());
					break;
				}
				default:{
					System.out.println(String.format("[QuestT] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class DescriptionT implements ProtoMessage{
		public static DescriptionT newInstance(){
			return new DescriptionT();
		}
		private int _Title;
		private int _BriefText;
		private int _FullText;
		private QuestT.DescriptionT.DialogueListT _StartDialogueList;
		private QuestT.DescriptionT.DialogueListT _CompleteDialogueList;
		private QuestT.DescriptionT.DialogueListT _FinishDialogueList;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private DescriptionT(){
		}
		public int get_Title(){
			return _Title;
		}
		public void set_Title(int val){
			_bit |= 0x1;
			_Title = val;
		}
		public boolean has_Title(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_BriefText(){
			return _BriefText;
		}
		public void set_BriefText(int val){
			_bit |= 0x2;
			_BriefText = val;
		}
		public boolean has_BriefText(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_FullText(){
			return _FullText;
		}
		public void set_FullText(int val){
			_bit |= 0x4;
			_FullText = val;
		}
		public boolean has_FullText(){
			return (_bit & 0x4) == 0x4;
		}
		public QuestT.DescriptionT.DialogueListT get_StartDialogueList(){
			return _StartDialogueList;
		}
		public void set_StartDialogueList(QuestT.DescriptionT.DialogueListT val){
			_bit |= 0x8;
			_StartDialogueList = val;
		}
		public boolean has_StartDialogueList(){
			return (_bit & 0x8) == 0x8;
		}
		public QuestT.DescriptionT.DialogueListT get_CompleteDialogueList(){
			return _CompleteDialogueList;
		}
		public void set_CompleteDialogueList(QuestT.DescriptionT.DialogueListT val){
			_bit |= 0x10;
			_CompleteDialogueList = val;
		}
		public boolean has_CompleteDialogueList(){
			return (_bit & 0x10) == 0x10;
		}
		public QuestT.DescriptionT.DialogueListT get_FinishDialogueList(){
			return _FinishDialogueList;
		}
		public void set_FinishDialogueList(QuestT.DescriptionT.DialogueListT val){
			_bit |= 0x20;
			_FinishDialogueList = val;
		}
		public boolean has_FinishDialogueList(){
			return (_bit & 0x20) == 0x20;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Title()){
				size += ProtoOutputStream.computeUInt32Size(1, _Title);
			}
			if (has_BriefText()){
				size += ProtoOutputStream.computeUInt32Size(2, _BriefText);
			}
			if (has_FullText()){
				size += ProtoOutputStream.computeUInt32Size(3, _FullText);
			}
			if (has_StartDialogueList()){
				size += ProtoOutputStream.computeMessageSize(4, _StartDialogueList);
			}
			if (has_CompleteDialogueList()){
				size += ProtoOutputStream.computeMessageSize(5, _CompleteDialogueList);
			}
			if (has_FinishDialogueList()){
				size += ProtoOutputStream.computeMessageSize(6, _FinishDialogueList);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Title()){
				output.writeUInt32(1, _Title);
			}
			if (has_BriefText()){
				output.writeUInt32(2, _BriefText);
			}
			if (has_FullText()){
				output.writeUInt32(3, _FullText);
			}
			if (has_StartDialogueList()){
				output.writeMessage(4, _StartDialogueList);
			}
			if (has_CompleteDialogueList()){
				output.writeMessage(5, _CompleteDialogueList);
			}
			if (has_FinishDialogueList()){
				output.writeMessage(6, _FinishDialogueList);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_Title(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_BriefText(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_FullText(input.readUInt32());
						break;
					}
					case 0x00000022:{
						set_StartDialogueList((QuestT.DescriptionT.DialogueListT)input.readMessage(QuestT.DescriptionT.DialogueListT.newInstance()));
						break;
					}
					case 0x0000002A:{
						set_CompleteDialogueList((QuestT.DescriptionT.DialogueListT)input.readMessage(QuestT.DescriptionT.DialogueListT.newInstance()));
						break;
					}
					case 0x00000032:{
						set_FinishDialogueList((QuestT.DescriptionT.DialogueListT)input.readMessage(QuestT.DescriptionT.DialogueListT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[QuestT.DescriptionT] NEW_TAG : TAG(%d)", tag));
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		
		public static class DialogueListT implements ProtoMessage{
			public static DialogueListT newInstance(){
				return new DialogueListT();
			}
			private java.util.LinkedList<QuestT.DescriptionT.DialogueT> _Dialogue;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private DialogueListT(){
			}
			public java.util.LinkedList<QuestT.DescriptionT.DialogueT> get_Dialogue(){
				return _Dialogue;
			}
			public void add_Dialogue(QuestT.DescriptionT.DialogueT val){
				if(!has_Dialogue()){
					_Dialogue = new java.util.LinkedList<QuestT.DescriptionT.DialogueT>();
					_bit |= 0x1;
				}
				_Dialogue.add(val);
			}
			public boolean has_Dialogue(){
				return (_bit & 0x1) == 0x1;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;
			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_Dialogue()){
					for(QuestT.DescriptionT.DialogueT val : _Dialogue){
						size += ProtoOutputStream.computeMessageSize(1, val);
					}
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (has_Dialogue()){
					for(QuestT.DescriptionT.DialogueT val : _Dialogue){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Dialogue()){
					for (QuestT.DescriptionT.DialogueT val : _Dialogue){
						output.writeMessage(1, val);
					}
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x0000000A:{
							add_Dialogue((QuestT.DescriptionT.DialogueT)input.readMessage(QuestT.DescriptionT.DialogueT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[QuestT.DialogueListT] NEW_TAG : TAG(%d)", tag));
							return this;
						}
					}
				}
				return this;
			}

			@Override
			public void dispose(){
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		
		public static class DialogueT implements ProtoMessage{
			public static DialogueT newInstance(){
				return new DialogueT();
			}
			private int _Text;
			private byte[] _Sound;
			private int _PortraitAssetID;
			private boolean _ShowQuestWindowAndHighlightCloseButton;
			private boolean _ShowQuestWindowAndHighlightRewardButton;
			private boolean _HighlightProgressWindow;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private DialogueT(){
			}
			public int get_Text(){
				return _Text;
			}
			public void set_Text(int val){
				_bit |= 0x1;
				_Text = val;
			}
			public boolean has_Text(){
				return (_bit & 0x1) == 0x1;
			}
			public byte[] get_Sound(){
				return _Sound;
			}
			public void set_Sound(byte[] val){
				_bit |= 0x2;
				_Sound = val;
			}
			public boolean has_Sound(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_PortraitAssetID(){
				return _PortraitAssetID;
			}
			public void set_PortraitAssetID(int val){
				_bit |= 0x4;
				_PortraitAssetID = val;
			}
			public boolean has_PortraitAssetID(){
				return (_bit & 0x4) == 0x4;
			}
			public boolean get_ShowQuestWindowAndHighlightCloseButton(){
				return _ShowQuestWindowAndHighlightCloseButton;
			}
			public void set_ShowQuestWindowAndHighlightCloseButton(boolean val){
				_bit |= 0x8;
				_ShowQuestWindowAndHighlightCloseButton = val;
			}
			public boolean has_ShowQuestWindowAndHighlightCloseButton(){
				return (_bit & 0x8) == 0x8;
			}
			public boolean get_ShowQuestWindowAndHighlightRewardButton(){
				return _ShowQuestWindowAndHighlightRewardButton;
			}
			public void set_ShowQuestWindowAndHighlightRewardButton(boolean val){
				_bit |= 0x10;
				_ShowQuestWindowAndHighlightRewardButton = val;
			}
			public boolean has_ShowQuestWindowAndHighlightRewardButton(){
				return (_bit & 0x10) == 0x10;
			}
			public boolean get_HighlightProgressWindow(){
				return _HighlightProgressWindow;
			}
			public void set_HighlightProgressWindow(boolean val){
				_bit |= 0x20;
				_HighlightProgressWindow = val;
			}
			public boolean has_HighlightProgressWindow(){
				return (_bit & 0x20) == 0x20;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;
			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_Text()){
					size += ProtoOutputStream.computeUInt32Size(1, _Text);
				}
				if (has_Sound()){
					size += ProtoOutputStream.computeBytesSize(2, _Sound);
				}
				if (has_PortraitAssetID()){
					size += ProtoOutputStream.computeUInt32Size(3, _PortraitAssetID);
				}
				if (has_ShowQuestWindowAndHighlightCloseButton()){
					size += ProtoOutputStream.computeBoolSize(4, _ShowQuestWindowAndHighlightCloseButton);
				}
				if (has_ShowQuestWindowAndHighlightRewardButton()){
					size += ProtoOutputStream.computeBoolSize(5, _ShowQuestWindowAndHighlightRewardButton);
				}
				if (has_HighlightProgressWindow()){
					size += ProtoOutputStream.computeBoolSize(6, _HighlightProgressWindow);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Text()){
					output.writeUInt32(1, _Text);
				}
				if (has_Sound()){
					output.writeBytes(2, _Sound);
				}
				if (has_PortraitAssetID()){
					output.writeUInt32(3, _PortraitAssetID);
				}
				if (has_ShowQuestWindowAndHighlightCloseButton()){
					output.writeBool(4, _ShowQuestWindowAndHighlightCloseButton);
				}
				if (has_ShowQuestWindowAndHighlightRewardButton()){
					output.writeBool(5, _ShowQuestWindowAndHighlightRewardButton);
				}
				if (has_HighlightProgressWindow()){
					output.writeBool(6, _HighlightProgressWindow);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_Text(input.readUInt32());
							break;
						}
						case 0x00000012:{
							set_Sound(input.readBytes());
							break;
						}
						case 0x00000018:{
							set_PortraitAssetID(input.readUInt32());
							break;
						}
						case 0x00000020:{
							set_ShowQuestWindowAndHighlightCloseButton(input.readBool());
							break;
						}
						case 0x00000028:{
							set_ShowQuestWindowAndHighlightRewardButton(input.readBool());
							break;
						}
						case 0x00000030:{
							set_HighlightProgressWindow(input.readBool());
							break;
						}
						default:{
							System.out.println(String.format("[QuestT.DialogueT] NEW_TAG : TAG(%d)", tag));
							return this;
						}
					}
				}
				return this;
			}

			@Override
			public void dispose(){
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
	}
	
	public static class PrerequisiteT implements ProtoMessage{
		public static PrerequisiteT newInstance(){
			return new PrerequisiteT();
		}
		private QuestT.PrerequisiteT.LevelT _Level;
		private QuestT.PrerequisiteT.CharacterClassListT _CharacterClass;
		private QuestT.PrerequisiteT.RequiredQuestT _Quest;
		private RegionT _Region;
		private QuestT.PrerequisiteT.BloodPledgeT _BloodPledge;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PrerequisiteT(){
		}
		public QuestT.PrerequisiteT.LevelT get_Level(){
			return _Level;
		}
		public void set_Level(QuestT.PrerequisiteT.LevelT val){
			_bit |= 0x1;
			_Level = val;
		}
		public boolean has_Level(){
			return (_bit & 0x1) == 0x1;
		}
		public QuestT.PrerequisiteT.CharacterClassListT get_CharacterClass(){
			return _CharacterClass;
		}
		public void set_CharacterClass(QuestT.PrerequisiteT.CharacterClassListT val){
			_bit |= 0x2;
			_CharacterClass = val;
		}
		public boolean has_CharacterClass(){
			return (_bit & 0x2) == 0x2;
		}
		public QuestT.PrerequisiteT.RequiredQuestT get_Quest(){
			return _Quest;
		}
		public void set_Quest(QuestT.PrerequisiteT.RequiredQuestT val){
			_bit |= 0x4;
			_Quest = val;
		}
		public boolean has_Quest(){
			return (_bit & 0x4) == 0x4;
		}
		public RegionT get_Region(){
			return _Region;
		}
		public void set_Region(RegionT val){
			_bit |= 0x8;
			_Region = val;
		}
		public boolean has_Region(){
			return (_bit & 0x8) == 0x8;
		}
		public QuestT.PrerequisiteT.BloodPledgeT get_BloodPledge(){
			return _BloodPledge;
		}
		public void set_BloodPledge(QuestT.PrerequisiteT.BloodPledgeT val){
			_bit |= 0x10;
			_BloodPledge = val;
		}
		public boolean has_BloodPledge(){
			return (_bit & 0x10) == 0x10;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Level()){
				size += ProtoOutputStream.computeMessageSize(1, _Level);
			}
			if (has_CharacterClass()){
				size += ProtoOutputStream.computeMessageSize(2, _CharacterClass);
			}
			if (has_Quest()){
				size += ProtoOutputStream.computeMessageSize(3, _Quest);
			}
			if (has_Region()){
				size += ProtoOutputStream.computeMessageSize(4, _Region);
			}
			if (has_BloodPledge()){
				size += ProtoOutputStream.computeMessageSize(5, _BloodPledge);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Level()){
				output.writeMessage(1, _Level);
			}
			if (has_CharacterClass()){
				output.writeMessage(2, _CharacterClass);
			}
			if (has_Quest()){
				output.writeMessage(3, _Quest);
			}
			if (has_Region()){
				output.writeMessage(4, _Region);
			}
			if (has_BloodPledge()){
				output.writeMessage(5, _BloodPledge);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_Level((QuestT.PrerequisiteT.LevelT)input.readMessage(QuestT.PrerequisiteT.LevelT.newInstance()));
						break;
					}
					case 0x00000012:{
						set_CharacterClass((QuestT.PrerequisiteT.CharacterClassListT)input.readMessage(QuestT.PrerequisiteT.CharacterClassListT.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_Quest((QuestT.PrerequisiteT.RequiredQuestT)input.readMessage(QuestT.PrerequisiteT.RequiredQuestT.newInstance()));
						break;
					}
					case 0x00000022:{
						set_Region((RegionT)input.readMessage(RegionT.newInstance()));
						break;
					}
					case 0x0000002A:{
						set_BloodPledge((QuestT.PrerequisiteT.BloodPledgeT)input.readMessage(QuestT.PrerequisiteT.BloodPledgeT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[QuestT.PrerequisiteT] NEW_TAG : TAG(%d)", tag));
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		
		public static class LevelT implements ProtoMessage{
			public static LevelT newInstance(){
				return new LevelT();
			}
			private int _Minimum;
			private int _Maximum;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private LevelT(){
			}
			public int get_Minimum(){
				return _Minimum;
			}
			public void set_Minimum(int val){
				_bit |= 0x1;
				_Minimum = val;
			}
			public boolean has_Minimum(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_Maximum(){
				return _Maximum;
			}
			public void set_Maximum(int val){
				_bit |= 0x2;
				_Maximum = val;
			}
			public boolean has_Maximum(){
				return (_bit & 0x2) == 0x2;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;
			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_Minimum()){
					size += ProtoOutputStream.computeUInt32Size(1, _Minimum);
				}
				if (has_Maximum()){
					size += ProtoOutputStream.computeUInt32Size(2, _Maximum);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Minimum()){
					output.writeUInt32(1, _Minimum);
				}
				if (has_Maximum()){
					output.writeUInt32(2, _Maximum);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_Minimum(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_Maximum(input.readUInt32());
							break;
						}
						default:{
							System.out.println(String.format("[QuestT.LevelT] NEW_TAG : TAG(%d)", tag));
							return this;
						}
					}
				}
				return this;
			}

			@Override
			public void dispose(){
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		
		public static class CharacterClassListT implements ProtoMessage{
			public static CharacterClassListT newInstance(){
				return new CharacterClassListT();
			}
			private java.util.LinkedList<CharacterClass> _Classes;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CharacterClassListT(){
			}
			public java.util.LinkedList<CharacterClass> get_Classes(){
				return _Classes;
			}
			public void add_Classes(CharacterClass val){
				if(!has_Classes()){
					_Classes = new java.util.LinkedList<CharacterClass>();
					_bit |= 0x1;
				}
				_Classes.add(val);
			}
			public boolean has_Classes(){
				return (_bit & 0x1) == 0x1;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;
			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_Classes()){
					for(CharacterClass val : _Classes){
						size += ProtoOutputStream.computeEnumSize(1, val.toInt());
					}
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				/*if (has_Classes()){
					for(CharacterClass val : _Classes){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}*/
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Classes()){
					for (CharacterClass val : _Classes){
						output.writeEnum(1, val.toInt());
					}
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							add_Classes(CharacterClass.fromInt(input.readEnum()));
							break;
						}
						default:{
							System.out.println(String.format("[QuestT.CharacterClassListT] NEW_TAG : TAG(%d)", tag));
							return this;
						}
					}
				}
				return this;
			}

			@Override
			public void dispose(){
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		
		public static class RequiredQuestT implements ProtoMessage{
			public static RequiredQuestT newInstance(){
				return new RequiredQuestT();
			}
			private java.util.LinkedList<Integer> _IDs;
			private QuestT.PrerequisiteT.eConnective _Connective;
			private QuestT.PrerequisiteT.eQuestCondition _Condition;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private RequiredQuestT(){
				set_Connective(QuestT.PrerequisiteT.eConnective.And);
				set_Condition(QuestT.PrerequisiteT.eQuestCondition.FINISHED);
			}
			public java.util.LinkedList<Integer> get_IDs(){
				return _IDs;
			}
			public void add_IDs(int val){
				if(!has_IDs()){
					_IDs = new java.util.LinkedList<Integer>();
					_bit |= 0x1;
				}
				_IDs.add(val);
			}
			public boolean has_IDs(){
				return (_bit & 0x1) == 0x1;
			}
			public QuestT.PrerequisiteT.eConnective get_Connective(){
				return _Connective;
			}
			public void set_Connective(QuestT.PrerequisiteT.eConnective val){
				_bit |= 0x2;
				_Connective = val;
			}
			public boolean has_Connective(){
				return (_bit & 0x2) == 0x2;
			}
			public QuestT.PrerequisiteT.eQuestCondition get_Condition(){
				return _Condition;
			}
			public void set_Condition(QuestT.PrerequisiteT.eQuestCondition val){
				_bit |= 0x4;
				_Condition = val;
			}
			public boolean has_Condition(){
				return (_bit & 0x4) == 0x4;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;
			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_IDs()){
					for(int val : _IDs){
						size += ProtoOutputStream.computeUInt32Size(1, val);
					}
				}
				if (has_Connective()){
					size += ProtoOutputStream.computeEnumSize(2, _Connective.toInt());
				}
				if (has_Condition()){
					size += ProtoOutputStream.computeEnumSize(3, _Condition.toInt());
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				/*if (has_IDs()){
					for(int val : _IDs){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}*/
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_IDs()){
					for (int val : _IDs){
						output.writeUInt32(1, val);
					}
				}
				if (has_Connective()){
					output.writeEnum(2, _Connective.toInt());
				}
				if (has_Condition()){
					output.writeEnum(3, _Condition.toInt());
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							add_IDs(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_Connective(QuestT.PrerequisiteT.eConnective.fromInt(input.readEnum()));
							break;
						}
						case 0x00000018:{
							set_Condition(QuestT.PrerequisiteT.eQuestCondition.fromInt(input.readEnum()));
							break;
						}
						default:{
							System.out.println(String.format("[QuestT.RequiredQuestT] NEW_TAG : TAG(%d)", tag));
							return this;
						}
					}
				}
				return this;
			}

			@Override
			public void dispose(){
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		
		public static class BloodPledgeT implements ProtoMessage{
			public static BloodPledgeT newInstance(){
				return new BloodPledgeT();
			}
			private QuestT.PrerequisiteT.eBloodPledgeStatus _Status;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private BloodPledgeT(){
				set_Status(QuestT.PrerequisiteT.eBloodPledgeStatus.HAS_BLOOD_PLEDGE);
			}
			public QuestT.PrerequisiteT.eBloodPledgeStatus get_Status(){
				return _Status;
			}
			public void set_Status(QuestT.PrerequisiteT.eBloodPledgeStatus val){
				_bit |= 0x1;
				_Status = val;
			}
			public boolean has_Status(){
				return (_bit & 0x1) == 0x1;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;
			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_Status()){
					size += ProtoOutputStream.computeEnumSize(1, _Status.toInt());
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Status()){
					output.writeEnum(1, _Status.toInt());
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_Status(QuestT.PrerequisiteT.eBloodPledgeStatus.fromInt(input.readEnum()));
							break;
						}
						default:{
							System.out.println(String.format("[QuestT.BloodPledgeT] NEW_TAG : TAG(%d)", tag));
							return this;
						}
					}
				}
				return this;
			}

			@Override
			public void dispose(){
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		
		public enum eConnective{
			And(1),
			Or(2),
			;
			private int value;
			eConnective(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(eConnective v){
				return value == v.value;
			}
			public static eConnective fromInt(int i){
				switch(i){
				case 1:
					return And;
				case 2:
					return Or;
				default:
					throw new IllegalArgumentException(String.format("invalid arguments eConnective, %d", i));
				}
			}
		}
		
		public enum eQuestCondition{
			NOT_REVEALED(1),
			FINISHED(5),
			;
			private int value;
			eQuestCondition(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(eQuestCondition v){
				return value == v.value;
			}
			public static eQuestCondition fromInt(int i){
				switch(i){
				case 1:
					return NOT_REVEALED;
				case 5:
					return FINISHED;
				default:
					throw new IllegalArgumentException(String.format("invalid arguments eQuestCondition, %d", i));
				}
			}
		}
		
		public enum eBloodPledgeStatus{
			HAS_BLOOD_PLEDGE(1),
			NO_BLOOD_PLEDGE(2),
			;
			private int value;
			eBloodPledgeStatus(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(eBloodPledgeStatus v){
				return value == v.value;
			}
			public static eBloodPledgeStatus fromInt(int i){
				switch(i){
				case 1:
					return HAS_BLOOD_PLEDGE;
				case 2:
					return NO_BLOOD_PLEDGE;
				default:
					throw new IllegalArgumentException(String.format("invalid arguments eBloodPledgeStatus, %d", i));
				}
			}
		}
	}
	
	public static class ObjectiveListT implements ProtoMessage{
		public static ObjectiveListT newInstance(){
			return new ObjectiveListT();
		}
		private java.util.LinkedList<QuestT.ObjectiveT> _Objective;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ObjectiveListT(){
		}
		public java.util.LinkedList<QuestT.ObjectiveT> get_Objective(){
			return _Objective;
		}
		public void add_Objective(QuestT.ObjectiveT val){
			if(!has_Objective()){
				_Objective = new java.util.LinkedList<QuestT.ObjectiveT>();
				_bit |= 0x1;
			}
			_Objective.add(val);
		}
		public boolean has_Objective(){
			return (_bit & 0x1) == 0x1;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Objective()){
				for(QuestT.ObjectiveT val : _Objective){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_Objective()){
				for(QuestT.ObjectiveT val : _Objective){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Objective()){
				for (QuestT.ObjectiveT val : _Objective){
					output.writeMessage(1, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_Objective((QuestT.ObjectiveT)input.readMessage(QuestT.ObjectiveT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[QuestT.ObjectiveListT] NEW_TAG : TAG(%d)", tag));
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	
	public static class ObjectiveT implements ProtoMessage{
		public static ObjectiveT newInstance(){
			return new ObjectiveT();
		}
		private int _ID;
		private int _Desc;
		private QuestT.ObjectiveTypeT _Type;
		private int _AssetID;
		private int _RequiredQuantity;
		private QuestT.SubTypeT _SubType;
		private String _HyperText;
		private int _ExtraDesc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ObjectiveT(){
		}
		public int get_ID(){
			return _ID;
		}
		public void set_ID(int val){
			_bit |= 0x1;
			_ID = val;
		}
		public boolean has_ID(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Desc(){
			return _Desc;
		}
		public void set_Desc(int val){
			_bit |= 0x2;
			_Desc = val;
		}
		public boolean has_Desc(){
			return (_bit & 0x2) == 0x2;
		}
		public QuestT.ObjectiveTypeT get_Type(){
			return _Type;
		}
		public void set_Type(QuestT.ObjectiveTypeT val){
			_bit |= 0x4;
			_Type = val;
		}
		public boolean has_Type(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_AssetID(){
			return _AssetID;
		}
		public void set_AssetID(int val){
			_bit |= 0x8;
			_AssetID = val;
		}
		public boolean has_AssetID(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_RequiredQuantity(){
			return _RequiredQuantity;
		}
		public void set_RequiredQuantity(int val){
			_bit |= 0x10;
			_RequiredQuantity = val;
		}
		public boolean has_RequiredQuantity(){
			return (_bit & 0x10) == 0x10;
		}
		public QuestT.SubTypeT get_SubType(){
			return _SubType;
		}
		public void set_SubType(QuestT.SubTypeT val){
			_bit |= 0x20;
			_SubType = val;
		}
		public boolean has_SubType(){
			return (_bit & 0x20) == 0x20;
		}
		public String get_HyperText(){
			return _HyperText;
		}
		public void set_HyperText(String val){
			_bit |= 0x40;
			_HyperText = val;
		}
		public boolean has_HyperText(){
			return (_bit & 0x40) == 0x40;
		}
		public int get_ExtraDesc(){
			return _ExtraDesc;
		}
		public void set_ExtraDesc(int val){
			_bit |= 0x80;
			_ExtraDesc = val;
		}
		public boolean has_ExtraDesc(){
			return (_bit & 0x80) == 0x80;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_ID()){
				size += ProtoOutputStream.computeUInt32Size(1, _ID);
			}
			if (has_Desc()){
				size += ProtoOutputStream.computeUInt32Size(2, _Desc);
			}
			if (has_Type()){
				size += ProtoOutputStream.computeEnumSize(3, _Type.toInt());
			}
			if (has_AssetID()){
				size += ProtoOutputStream.computeUInt32Size(4, _AssetID);
			}
			if (has_RequiredQuantity()){
				size += ProtoOutputStream.computeUInt32Size(5, _RequiredQuantity);
			}
			if (has_SubType()){
				size += ProtoOutputStream.computeEnumSize(6, _SubType.toInt());
			}
			if (has_HyperText()){
				size += ProtoOutputStream.computeStringSize(7, _HyperText);
			}
			if (has_ExtraDesc()){
				size += ProtoOutputStream.computeUInt32Size(8, _ExtraDesc);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_ID()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Type()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_ID()){
				output.writeUInt32(1, _ID);
			}
			if (has_Desc()){
				output.writeUInt32(2, _Desc);
			}
			if (has_Type()){
				output.writeEnum(3, _Type.toInt());
			}
			if (has_AssetID()){
				output.writeUInt32(4, _AssetID);
			}
			if (has_RequiredQuantity()){
				output.writeUInt32(5, _RequiredQuantity);
			}
			if (has_SubType()){
				output.writeEnum(6, _SubType.toInt());
			}
			if (has_HyperText()){
				output.writeString(7, _HyperText);
			}
			if (has_ExtraDesc()){
				output.writeUInt32(8, _ExtraDesc);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_ID(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_Desc(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_Type(QuestT.ObjectiveTypeT.fromInt(input.readEnum()));
						break;
					}
					case 0x00000020:{
						set_AssetID(input.readUInt32());
						break;
					}
					case 0x00000028:{
						set_RequiredQuantity(input.readUInt32());
						break;
					}
					case 0x00000030:{
						set_SubType(QuestT.SubTypeT.fromInt(input.readEnum()));
						break;
					}
					case 0x0000003A:{
						set_HyperText(input.readString());
						break;
					}
					case 0x00000040:{
						set_ExtraDesc(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[QuestT.ObjectiveT] NEW_TAG : TAG(%d)", tag));
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	
	public static class TeleportT implements ProtoMessage{
		public static TeleportT newInstance(){
			return new TeleportT();
		}
		private int _Cost;
		private int _MapID;
		private int _X;
		private int _Y;
		private int _QuestionText;
		private boolean _NoTeleport;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TeleportT(){
		}
		public int get_Cost(){
			return _Cost;
		}
		public void set_Cost(int val){
			_bit |= 0x1;
			_Cost = val;
		}
		public boolean has_Cost(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_MapID(){
			return _MapID;
		}
		public void set_MapID(int val){
			_bit |= 0x2;
			_MapID = val;
		}
		public boolean has_MapID(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_X(){
			return _X;
		}
		public void set_X(int val){
			_bit |= 0x4;
			_X = val;
		}
		public boolean has_X(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_Y(){
			return _Y;
		}
		public void set_Y(int val){
			_bit |= 0x8;
			_Y = val;
		}
		public boolean has_Y(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_QuestionText(){
			return _QuestionText;
		}
		public void set_QuestionText(int val){
			_bit |= 0x10;
			_QuestionText = val;
		}
		public boolean has_QuestionText(){
			return (_bit & 0x10) == 0x10;
		}
		public boolean get_NoTeleport(){
			return _NoTeleport;
		}
		public void set_NoTeleport(boolean val){
			_bit |= 0x20;
			_NoTeleport = val;
		}
		public boolean has_NoTeleport(){
			return (_bit & 0x20) == 0x20;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Cost()){
				size += ProtoOutputStream.computeUInt32Size(1, _Cost);
			}
			if (has_MapID()){
				size += ProtoOutputStream.computeUInt32Size(2, _MapID);
			}
			if (has_X()){
				size += ProtoOutputStream.computeUInt32Size(3, _X);
			}
			if (has_Y()){
				size += ProtoOutputStream.computeUInt32Size(4, _Y);
			}
			if (has_QuestionText()){
				size += ProtoOutputStream.computeUInt32Size(5, _QuestionText);
			}
			if (has_NoTeleport()){
				size += ProtoOutputStream.computeBoolSize(6, _NoTeleport);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_MapID()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_X()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Y()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Cost()){
				output.writeUInt32(1, _Cost);
			}
			if (has_MapID()){
				output.writeUInt32(2, _MapID);
			}
			if (has_X()){
				output.writeUInt32(3, _X);
			}
			if (has_Y()){
				output.writeUInt32(4, _Y);
			}
			if (has_QuestionText()){
				output.writeUInt32(5, _QuestionText);
			}
			if (has_NoTeleport()){
				output.writeBool(6, _NoTeleport);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_Cost(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_MapID(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_X(input.readUInt32());
						break;
					}
					case 0x00000020:{
						set_Y(input.readUInt32());
						break;
					}
					case 0x00000028:{
						set_QuestionText(input.readUInt32());
						break;
					}
					case 0x00000030:{
						set_NoTeleport(input.readBool());
						break;
					}
					default:{
						System.out.println(String.format("[QuestT.TeleportT] NEW_TAG : TAG(%d)", tag));
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public enum ObjectiveTypeT{
		KILL_NPC(1),
		COLLECT_ITEM(2),
		REACH_LEVEL(3),
		TUTORIAL_BLOOD_PLEDGE_JOIN(4),
		TUTORIAL_USE_ITEM(5),
		DESTROY_NOVICE_SIEGE_DOOR(6),
		DESTROY_NOVICE_SIEGE_TOWER(7),
		TUTORIAL_ENCHANT_MAX(8),
		TUTORIAL_BLOOD_PLEDGE_CREATE(9),
		QUEST_REVEAL(10),
		VIEW_DIALOGUE(11),
		START_PSS(12),
		TUTORIAL_OPEN_UI(13),
		;
		private int value;
		ObjectiveTypeT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ObjectiveTypeT v){
			return value == v.value;
		}
		public static ObjectiveTypeT fromInt(int i){
			switch(i){
			case 1:
				return KILL_NPC;
			case 2:
				return COLLECT_ITEM;
			case 3:
				return REACH_LEVEL;
			case 4:
				return TUTORIAL_BLOOD_PLEDGE_JOIN;
			case 5:
				return TUTORIAL_USE_ITEM;
			case 6:
				return DESTROY_NOVICE_SIEGE_DOOR;
			case 7:
				return DESTROY_NOVICE_SIEGE_TOWER;
			case 8:
				return TUTORIAL_ENCHANT_MAX;
			case 9:
				return TUTORIAL_BLOOD_PLEDGE_CREATE;
			case 10:
				return QUEST_REVEAL;
			case 11:
				return VIEW_DIALOGUE;
			case 12:
				return START_PSS;
			case 13:
				return TUTORIAL_OPEN_UI;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ObjectiveTypeT, %d", i));
			}
		}
	}
	public enum SubTypeT{
		OPEN_HIDDEN_MAP_UI(1),
		OPEN_TOTAL_CRAFT_UI(2),
		OPEN_INDUN_CATEGORY_UI(3),
		OPEN_EINHASAD_POINT_UI(4),
		OPEN_EVEN_TOTAL_CRAFT_UI(5),
		OPEN_HUNTING_GUIDE_UI(6),
		OPEN_POTENTIAL_UI(7),
		OPEN_PURE_ELIXIR_UI(8),
		OPEN_MEDAL_OF_VALIANCY_UI(9),
		;
		private int value;
		SubTypeT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SubTypeT v){
			return value == v.value;
		}
		public static SubTypeT fromInt(int i){
			switch(i){
			case 1:
				return OPEN_HIDDEN_MAP_UI;
			case 2:
				return OPEN_TOTAL_CRAFT_UI;
			case 3:
				return OPEN_INDUN_CATEGORY_UI;
			case 4:
				return OPEN_EINHASAD_POINT_UI;
			case 5:
				return OPEN_EVEN_TOTAL_CRAFT_UI;
			case 6:
				return OPEN_HUNTING_GUIDE_UI;
			case 7:
				return OPEN_POTENTIAL_UI;
			case 8:
				return OPEN_PURE_ELIXIR_UI;
			case 9:
				return OPEN_MEDAL_OF_VALIANCY_UI;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SubTypeT, %d", i));
			}
		}
	}
}

