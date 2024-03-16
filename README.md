# ROADMAP (ToDo)

## Launcher
- [x] Get connect to latest server
- [x] Get information from the server
- [x] Integrated create account option
- [x] Integrated app center navigation
- [x] Install the client from the launcher
- [x] Integrate a patch system to update the client with automatic call to patcher command
- [x] Complete uninstall, including the client
- [x] Add alternative download links in case the cloud place will not work
- [x] Include an optio to auto create a directory exception for Windows Defender
- [ ] Implement the option to get the download links from the server (encrypted) as the connector info
- [ ] Auto update the launcher replacing the launcher executable for a new one
- [x] Build a setup program to install the launcher

## Patches
- [ ] ~~Make a GitHub repo with all unpacked pak content. With every oficial NC update we can extract the pak contents and check with Github what files are new or changed and pake a custom Patch.zip file to update the client.~~
- [x] Implement a new option in PackViewer to automatically generate a patch file ussing as source the NC log file L1Updater.log 

## Client 
- [ ] Translate all tbl files
- [ ] Translate all xml files
- [ ] Translate all uml files
- [ ] The big magic tooltip is not done for all the magic/skills
- [ ] A lot of text is so big and need to be shorter
- [ ] Translate the new desc-server.tbl to english
- [ ] Rename all the UML files, according with server dialog tables and source code to avoid that someone get the translated UML files from the client.

  
## Server 
- [x] Auto ServerMessages. 
- [ ] Translate messages from Files
- [ ] Translate messages from DB
- [ ] Translate the Server Manager (only to know what it makes before move the functionallity to a web interface)
- [x] Translate the messages that jumps into the client when you use for first time a new account
- [ ] When you make the quest to join a pledge, you get a Title, in korean. Check where is this done
- [x] Load the translations into the 0_Translation table
- [ ] Check what fixed text in database or server code needs to be added to desc-server.tbl
- [ ] Process the source code to change the translations for a reference (like $123)
	
## DB 
- [ ] Translations in all the tables needed, without deleting the original korean text
- [ ] Move the translation to the desc.tbl

## Web
- [ ] App center translation
- [ ] Replicate App center for a external webpage
- [ ] Add a monster database view (maybe is already there, seems that app store has a powerbook)
- [ ] Add an items database view (maybe is already there, seems that app store has a powerbook)
- [ ] Add an areas database view (maybe is already there, seems that app store has a powerbook)
- [ ] Add an internal server maintenance (use the Mythic db tools as a base)

## Improvements
- [x] Add english gm commands
- [ ] Implement tipical user commands like like -buff or -warp
- [x] The items name in shops is not the reference $123, is directly a text, in korean. Change them for the reference.
- [ ] Maybe add special NPCs with special buffs like the snake tower (Einhasad?) in Giran from other servers 
- [x] Limit the PSS. Maybe 2 hour / week or something like that. Will be great to have an icon like buff icons (haste for example) with the PSS name and the time remaining.
- [ ] Encrypt passwords with SHA (SHA3 for example)
- [ ] Locate all direct comparisons with an item id to change them all to unit constants. For example "if (itemId == 3200015)"
	
## Test
- [ ] Sieges
- [ ] Quests
- [ ] Exhibition
- [ ] Doll Alchemy
- [ ] Smelting Stones
- [ ] Attendance check
- [ ] Dungeons Instances
- [ ] Favor System
- [ ] Halpas items
- [ ] Bots
- [ ] PVP
- [ ] App center options
- [ ] App center store
- [ ] Balance between different chars
- [ ] Items received during quest

## Guides 
- [ ] Basic guides
- [ ] New content guides
	
## Discord channel
- [ ] Create a discord chanel
- [ ] Create the robots to make automatic announces in discord chanels

## Investigation
- [ ] How it works right now the exp. Are we getting always EXP or is necesary to buy items to get exp?  Einhasad protection?
- [ ] For what are TAM and BERRY?
- [ ] Are the mob always droping adena or is necesary to consume something to get it?
- [x] How it works the silver adena (Templar coin): Used in Talking Island for leveling. No use afer that.
- [ ] How to change the polymorph list in client/server for events. Seems that the game use the file "PolymorphList.xml". Easy to test it.
- [ ] How it works the special poly to rank system (for 1st one in the rank). Is implemented already? how? 
- [ ] How to translate the small Magic/Skill hint
- [ ] How exactly is working alchemy.dat, smelting.dat, craft.bin files
- [ ] How are quest working. Where is decided how many XP you get and what items you get
- [ ] How it works the server list. We dont need 30 servers, only one. Seems that the game use the file "ServerName.xml". Easy to test it.
- [ ] In old times, the client had a lot of commands to activate whisper, disable whisper, get location, party, etc. For sure in this version are in korean. Check it (after modify the source code, update the file Shortcut.xml)

### [Readme.me] Style Guide 
https://docs.github.com/es/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax
