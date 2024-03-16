# Lineage Development Wiki

In this wiki you can find all the relevant information to develop and maintain an NC Lineage emulator.

## Index
- [[Connector]](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#connector)
- [Client]
- [Pak files](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#pak-files)
- [File types](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#file-types)
- [Localization](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#localization)
- [English patch](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#english-patch)
- [Server]
- [Functionalities](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#functionalities)
- [Translate](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#translate)
- [Client Connection](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#client-connection)
- [Speak with one NPC](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#speak-with-one-npc)
- [Items with dialogs](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#items-with-dialogs)
- [Maps](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#maps)
- [Detailed Login](https://github.com/lechtung/L1J-REMASTERED/blob/main/wiki.md#detailed-login-process)
- [Dungeons limited by time](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#dungeons-limited-by-time)
- [Stores](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#stores)
- [Reward system](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#reward-system)
- [Desc_kr](https://github.com/lechtung/L1J-REMASTERED/edit/main/wiki.md#desc_kr)
- [[Web](https://github.com/lechtung/L1J-REMASTERED/blob/main/wiki.md#web--app-store)]

## Connector (Launcher)

The connector is the intermediate part between the client and the server, which makes it possible to play lineage using an emulation server and not the official one. The connector just loads a DLL in memory which is in charge of intercepting the communication between the Lineage executable (Lin.bin) and the server, modifying the IP address and redirecting the data packets to the emulation server. 

The lineage executable "lin.bin" is not modified and is the original NC executable but as NC changes the opcodes and passwords from time to time, the connectors are always accompanied by a specific version of the "lin.bin" to avoid them to stop working after an NC update. The same applies to the "libcocs2d.dll" library, which is associated with a "lin.bin".

Some connectors also come with a "boxer.dll" library, which is actually the official NC library that is responsible for making updates to the client's PAK files.

The DLL needs a data structure where all the operating parameters are indicated, which are as follows:

- **OP_GAMESERVER_ADDRESS:** The ip address of the server.
- **OP_GAMESERVER_PORT:** The port of the server
- **OP_USE_APPCENTER:** Whether to use the APPCENTER or not
- **OP_APPCENTER_ADDRESS:** The AppCenter ip address
- **OP_APPCENTER_PORT:** The AppCenter port
- **OP_USE_SECONDS_PASSWORD:** Indicates if the second numeric password will be used to access the game (OTA)
- **OP_LINBIN_NAME:** The name of the "lin.bin" file to load
- **OP_C_LOGOUT:** (byte). Opcode of the lineage. For example 176
- **OP_C_LOGIN:** (byte). Opcode of the lineage. For example 233
- **OP_C_CHANNEL:** (byte). Opcode of the lineage. For example 99
- **OP_S_CUSTOM_MESSAGE_BOX:** (byte). Opcode of the lineage. For example 3
- **OP_LINBIN_VERSION:** The version of the "lin.bin" binary. For example 1901120701
- **OP_CLIENT_SIDE_KEY:** The key of the "lin.bin" file. For example 22443638
- **OP_S_KEY:** (byte). Opcode of the lineage. For example 235
- **OP_S_EXTENDED_PROTO_BUF:** (byte). Opcode of the lineage. For example 180
- **OP_C_EXTENDED_PROTO_BUF:** (byte). Opcode of the lineage. For example 81
- **OP_DLL_PASSWORD:** The password of the DLL, e.g. 2052201973

Likewise, when the "lin.bin" file is executed, the connector passes it several parameters in a command line, which are the following

- **AuthnToken:** The authorization token. In the current connector it is the session token that is sent by the server. In earlier versions, the account plus the password
- **SessKey:**  Same as the previous parameter
- **ServiceRegion:**  "NCS"
- **AuthProviderCode:** "np"
- **ServiceNetwork:** "Live"
- **NPServerAddr:** "https://api.ncsoft.com:443"
- **UidHash:** "798363913"
- **PresenceId:** "L1_KOR:3C164CEB-D15F-E011-9A06-E61F135E992F"
- **UserAge:** 10

### Installation and update
The connector has an integrated option to download the files corresponding to the client from the internet if necessary (when the "install.dat" file exists). The links are currently codified within the connector, but in future versions they will be provided by the server.

The connector is also capable of updating the client if there are patches available. The operation is very simple: together with the connector there is a file called "LWLauncher.vfo" inside which there is a number that indicates the version of the client. When the Launcher connects with the server, it finds out the current version and if the number obtained is higher than the current one, it will download and apply the necessary patches.

To store the files we can use Google Drive or One Drive (from microsoft), but is necesary to convert the shared links generated in the platform for direct download links. There are 2 online webs for that:
- Google: https://sites.google.com/site/gdocs2direct/
- One Drive: https://joelgrayson.wixsite.com/joelgrayson/onedrive-download-link-generator

## Client

In this section we will see everything related to the client, how it is structured and how to modify it

### Pak files
The client has different PAK files where all the files that the client needs to function are stored, both image files and text files as well as binaries. The PAK files are the following:

- **data:** contains most of the files that will need to be located to run the client in English, such as tbl, xml or uml files.
- **icon:** contains a multitude of images in png format corresponding to different icons used in the client, such as objects or skills.
- **image:** again contains a multitude of images in png format corresponding to the maps, monster book images, polymorph list images, etc.
- **sprite:** contains files in its own .spx format that correspond to animations of the characters or npcs.
- **tile:** contains image files in its own .til format corresponding to the different types of terrain and objects that are shown in the client's mapping
- **ui:** contains both xml text files and png images that are used in the user interface.

### File Types

The most important types of files that we can find in PAK files are the following:

- **tbl:** text file by lines that contains text literals that are used in the client for the interface, messages, object names, npcs names, etc.
- **xml:** text file that contains different information such as the structure and text of the teleport screen, the magic tooltip, etc. They are encrypted.
- **uml:** text file that contains the dialogs displayed by the npcs
- **spx:** own NC format that contains the animations of the characters and the npcs. It is nothing more than a collection of images with a width and a height specified in the file.
- **csb:** Cocos2d format, a program used to create video game interfaces, capable of containing images and interaction with the user. They are compiled versions of the csd source files (which are unfortunately not inside the pak files)

#### UML special structure

- The files can have something like this: 
```<a action="askwartime">```
These are actions, and basically the name of the action will be sent to the server, which can reply with a specific text or the name of another uml file that the client will open.   
- You can also have buttons that when pressed will also launch an action, such as ```<panel type="Panel_Shop_Bt_Craft"><button action="request craft"/></panel>```
- You may have things that are not 100% HTML compatible, like ```<font fg="ffffaf">``` which is equivalent to ```<font color="ffffaf">```
- They have an <npc> section where they define the name of the npc, their photo if they have one, their title and some extra text:
```
<npc>
<portrait>PO_016</portrait>
<name>루우나</name>
<job>[이벤트 제작]</job>
<text></text>
</npc>
```

### Most important files

We can highlight the following files from the PAK files:

- **desc.tbl:** Contains names of objects, mobs, npcs, etc.
- **string.tbl:** Contains fixed strings that are used on the client, such as the interface or some messages.
- **itemdesc.tbl:** Contains the text to be displayed in chat when an item is identified.

### Localization

This game is Korean so it is designed to be run in a Korean environment. To be able to do this it is necessary to change the administrative language of Windows to Korean:

Settings > Time and language > Language and region > Administrative language settings > Administrative > Language for non-Unicode programs > Current language for non-Unicode programs > Change system locale > Current system locale > Korean.

One of the drawbacks of making this change is that Windows change automatically the character for directory separator, from the usual character "/" to the ugly "₩". If you want to restore the usual separator, you have to open a command console in administrative mode and type the following command (use the one that is appropriate depending on the region of the system):

```  
- Europe: reg add "HKLM\SYSTEM\CurrentControlSet\Control\Nls\CodePage" /v OEMCP /t reg_sz /d 850 /f   
- USA: reg add "HKLM\SYSTEM\CurrentControlSet\Control\Nls\CodePage" /v OEMCP /t reg_sz /d 437 /f   
- Korea: reg add "HKLM\SYSTEM\CurrentControlSet\Control\Nls\CodePage" /v OEMCP /t reg_sz /d 949 /f   
```

After that, you can then right click on the console title and select "Properties > Font" and change the font to "consoles"

Source: https://ss64.com/nt/chcp.html

### English patch

If you don't want to change your windows to Korean, don't worry, it's still possible to run Lineage on English Windows, since it's enough to introduce different files with the "-e" modifier for the client to adapt and work normally.
  
Specifically, it is necessary to introduce in the client a few of the .tbl files that have the -k modifier but changed to -e. You will also need to enter a few xml files and all uml files (otherwise the NPCs will stop talking).

## Server

On the server is where almost all the "magic" is to be able to emulate the Lineage. Its structure is complex and it is very easy to forget what each thing is for.

### Functionalities

Currently our server has several features integrated:

- **Lineage server:** This is the main functionality, providing lineage emulation and communication with the client.
- **AppCenter:** The server has integrated the execution of a web page server that serves the AppCenter website, which is a website very similar in structure to the official NC website, where users can see their characters, see their inventory, exchange objects with other users or use the integrated store.
- **Authentication:** The server communicates directly with the connector both to create new accounts and to provide the connector with the necessary identification token in case the user enters the correct account and password.
  
### Translate
  
The idea is to translate the server but keep Korean working for Korean users. For this, what you have to do is replace all the texts on the server with a $123 reference and transfer the previous text to the client, specifically to the desc-x.tbl file. To do this obviously we must add at the end of the file all the texts that have been extracted from the server, which can have several origins:

- **Announce Cycle:** The texts are located in the file ```$SERVIDOR\data\announcecycle.txt```
- **Fixed System Messages:** These can be found in the file ```message.properties.java```
- **Database:** In the database, specifically in the ```armor, etcitem, npc, weapon tables``` a few fixed korean texts have been replaced by references like $123. In order to locate them, all we have to do is look in the translations table ```0_translations``` for those records with ```source = db_server.tbl``` and cross the reference with the line number.  
- **Automatic references:** There are many messages on the server that send fixed text in Korean to the client. Almost all of them have been replaced by references and their text transferred to the client. Again, to be able to locate them, all we have to do is search the ```0_translations``` table for those references whose ```source = automatic_reference``` and select the one whose index directly matches the line_number field.
- **Various files:** On the server there are many places where fixed texts are used in Korean that end up being sent to the client. On this occasion it will be necessary to replace the texts with references by hand. The possible files are the following:
  - L1ItemInstance.java
- **Other database tables:** There are other tables in the DB that have text in Korean that should be translated and investigated if they can be multilanguage or not, such as:
  - item_click_message
  
A problem that we have found translating this game is with the help context window showed in the magic. It remains with korean language and we don't find any place (TBL or XML file) where we can translate this information.
NOTE: I have found UML files with help about magic. Maybe is the client extracting from these files the help context window text?. Try with this file: helpmgcde5-k.uml (Dark Elf Magic Level 4)  
  
## Processes

In this section we will have explained different processes that involve the client + server.
  
### Client Connection
  
When a client connects, the GameClient.start() class is called where the following code is executed:

```
# create a connection
_connection = new Connection(socketChannel, selector, this);
# Create an observer
_observer = new ClientThreadObserver(Config.SERVER.AUTOMATIC_KICK * 60000);
# Send the connection a key
_connection.send(Config.VERSION.FIRST_KEY);
# initialize the encryption keys
_cryption.initKeys(Config.VERSION.FIRST_KEY_SEED);
# Start receiving/sending packets with the connection
_connection.resumeRecv();
```
#### Create a connection
This uses a java class so i don't think it matters
#### Create an observer
I don't think it's important either
#### Send the connection a key
The key to send depends on the version of the client. The server is theoretically ready to work with different clients (it has different keys stored in the FirsKey table of the DB) but in the end the key used is the one corresponding to the client indicated in the configuration file version.properties.ClientVersion (2211231701). 
In this case the key is: e7, 43, 3f, 0f, 21, 1d, 9f, 2e, 23
#### Initialize the encryption keys
It is initialized with a seed that is calculated based on the previous key (554647363). That leaves the _cryption variable ready to be able to both encrypt (encrypt_s) and decrypt (decrypt_S)
#### Start receiving/sending packets with the connection
Now, in the onRecv event, when receiving a packet and calling _cryption.decrypt_S we decrypt what we receive

### Speak with one NPC

How does this work:

- When the server starts, the definition of the dialogs that each NPC initially launches is loaded into the ```NPCTalkDataTable``` class, using the ```npcAction``` table.
- Also, when starting the server, a class ```NpcActionTable``` will be created that will contain actions for specific NPCs. These actions are obtained from the xml files in the ```/data/xml/NPCActions``` directory.
- In the client, a player clicks on an NPC, let's say it's NPC Aanon (with ID 123)
- The client will communicate with the server and tell it that it has started a conversation with the NPC 123. For this, the client will use the opcode ```C_DIALOG```
- This opcode will be taked in the unit ```PacketHandler``` (like all the opcodes) and will create the class ```C_NPCTalk```.
- In this C_NPCTalk the server will search the object that match with the ID received from client. First, it will check if we have an action defined in the class ```NpcActionTable``` and if is found, then it will return to the client the action to make ussing the class ```S_NPCTalkReturn```.
- If a record in the class ```NpcActionTable``` is not found, then an ```L1Object``` will be created for the npc ID and the method ```onTalkAction``` will be called. Becouse this method is virtual, the correct method for the correct class for this object will be used. There is several classes that implement this method becouse they are inherited classes from ```L1Object```: ```L1PeopleInstance, L1CastleGuardInstance, L1HouseKeeperInstance, L1GuardInstance, L1QuestInstance```, etc.
- Almost all of this classes, in the method ```onTalkAction```, have modifiers for specific NPCs, where they define a specific UML file name that the client will be told to use. If the NPC we are talking to is not on that list, then the UML dialog specified in the class ```NPCTalkDataTable``` will be used, which will vary if the player is laufull or chaotic. After that, ussing the class ```S_NPCTalkReturn```, a response with a uml name will be sended to the client.
- Now, on the client side several things can happen. The player can click on a link that just opens another UML file. This does not reach the server, it is done completely independently on the client. It can also happen that the client clicks on an "action". This is slightly different. In this case the client will send the ```C_HACTION``` opcode to the server. This opcode will be taked in the unit PacketHandler (like all the opcodes) and will create the class ```C_NPCAction```.
- This java unit ```C_NPCAction``` has the actions divided into three large groups, each with its corresponding unit. Our unit will call each of them, passing them different parameters (it depends on the unit) to see if they have defined something specific. If they don't have it, the main unit will go to the next one. If none of the three have anything specific, then the main unit will check for specific actions, with literals like ```askwartime```, ```pay```, etc. to perform certain operations. The three units mentioned are the following:
     - NpcActionTeleportTable. This unit will receive the id of the npc and the specific action. Internally it has a list in memory initially loaded from the database table ```npcaction_teleport```. Here there are many records with the NPC id, the corresponding action and specific data about a teleport. As a result, the unit will return a ``L1NpcActionTeleport`` class.
     - L1NpcHtmlFactory. This unit will receive the specific action. Internally it has a table in memory in which actions are related to the instance of a specific class. There are many of them, for example, if this unit receives the action ```buy```, it will return a class ```L1NpcHtmlFactory``` that as an action contains the instance of the class ```BuyAction```.
     - L1NpcIdFactory. This unit will receive the id of the NPC. As with the previous unit, it has an in-memory table that associates NPCS ids with an instance of a specific class. We also have a multitude of them, for example, if this unit receives the id of the NPC ```170041```, it will return the class ```L1NpcIdAction``` that will contain the action corresponding to the class ```MaenoAction```.
  
- At the end, is possible that the dialog is done and close, or is also possible that the server ussing again the class ```S_NPCTalkReturn``` will send to the client a UML name to be opened.

revisar:

S_Inn
S_AuctionBoard
S_NPCTalkReturn  

INVESTIGAR. Esto creo que es para enviar diferentes parámetros al html. Igual se podria crear un html vacio con un parámetro y en codigo rellenar todo ese texto... investigar

pc.sendPackets(new S_NPCTalkReturn(objid, failure_htmlid, htmldata), true);
 
### Items with dialogs

There are some items that when double clicked on them are not consumed, or equipped, but show a new window with a dialog.

Getting this is simple, just create a record in the ```item_click_message``` table with the following values:

- **itemId (int):** id of the item
- **type (bool):** if TRUE, it will show the dialog whose name is indicated in the msg field. If it is FALSE, it will display a system message on the client with the text indicated in the msg field.
- **msg (string):** name of the dialog that we want to show (without the file extension) or the message that we want to show in the chat
- **delete (bool):** if TRUE, the object will be consumed after displaying the dialog or message.

### Maps
  
Clients have different subdirectories in the map folder, one per map. Inside each subdirectory there are several files with the structure of a map.
The client takes these maps into account to know if the player can move somewhere or not depending on whether there is a free wall or path and once allowed
the displacement sends the movement to the server.
  
On the server we have the maps folder where there are some text files, one for each map, which defines its structure, walls, etc. But these files are not
They are used during execution, since when the server starts up, it generates some cache maps on disk in the data/mapcache directory and these will be the ones that are used. If they don't exist, they are created from the text files, so if you update a text file, you have to delete the cached map for it to be regenerated.

In order to generate the server maps based on the client data, the following must be done:

- Copy the executables ExtractMap.exe and "FreeTime_worldmap_v0.2.exe" in the "map" folder of the client.
- Run the file "ExtractMap.exe", which will create an index of the maps in the file "worldmap.cpp"
- Execute the "FreeTime_worldmap_v0.2.exe" file, which using the file created in the previous point, will create the "maps" folder and inside the text files that we can use on the server.  
  
### Detailed LOGIN process

- The server starts
   - Server.Server();
   - GameServer.getInstance().initialize();
- The connector is executed
   - A request is made to the URI ```/outgame/info```. This call instantiates the ConnectorInfoDefine class and a json is returned with all the server info.
- Enter the login and password and click on the button that shows the web. A request is made to the URI ```/outgame/login``` with the following parameters:
  - account = test1111
  - password = test1111
  - process = svchost%2cCefSharp%2cAggregatorHost%2ccchrome%2cmsedge%2cCode%2cmsedgewebview2%2cfdlauncher
  - hdd_id = SCSI%5cDISK%26VEN_NVME%26PROD_ADATA_SX8200PNP%5c5%26D2A2058%260%26000000
  - board_id = NUC8BEB%5cGEBE02200VS7
  - mac_address = 1C%3a69%%3a27%3aAF&nic_id=PCI%5cVEN8086%26DEV%26SUBSYS%26REV_30%5c3%2611583659%260%26FE
  - mac_info = bPguYF7gjxucmXWFK2ICdjJqe1bQE35NVV%2fL6uv%2brEw%3d
- Click on the button to execute Lineage
  - GameClient.start
  - A_NpLogin.doWork. Here, using the encrypted login data sent by the client, it locates the corresponding session, since previously, the connector using the user data has been identified to recover its ncoin data and leave it identified on the web. The session data is located in the HttpLoginSession unit.
- The character selection screen is displayed
  - C_LoginToServer.login
  - GameClient.start
- We are already in Lineage
 
#### Web calls

This web request is answered from the server using the LoginDefine.java unit, but how did we get to this point? The server maintains a web request/java unit association table that is stored in the app_page_info DB table, a table that is processed in the DispatcherLoader.java unit, that is, in this unit web requests are received and decisions are made. which java unit will be used to respond to said web request. In this specific case, the record in the table is the following:   

```INSERT INTO `app_page_info` VALUES ('/outgame/login', null, 'LoginDefine', '0', '0', 'false', 'true', 'false', 'false', 'true', 'false ');```

In this table there are many different entry points that are mainly used in the AppCenter, which if not configured otherwise, can only be used from the connector or from the game. This is controlled thanks to the ```user-agent``` which is a header that is sent in the request and which must have the value ```nc launcher``` in the case of the connector and ```nc ingame``` when it is from the lineage.

#### LoginDefine

In this LoginDefine unit, a base64 text that identifies the session (an auth token) will be obtained, something like ```AAAAAAAAAABaVEVDCAIDCUdUQk0EAA===```. The socket then has to encrypt this text and send it as bytes (encrypted with AES), something like ```DFH3DXd/bI5bfaF/Q9RBaPZuSsk18xoGs71IMITxWROI0vZ74HMeQilUAmpZbFn3```. This encrypted text replaces the authorization text that we used to send as "account|password" in older connectors.

Also, LoginDefine receives a lot of data as we have seen before (the hdd_id, board_id, etc). But in order to continue, the first thing it does is encrypt the ```hdd_id + mac_address + uri information``` and it gets an encrypted text. But be careful, that it is not encrypted only as the concatenation of its values, other characters are used in between to make it difficult to figure out how it is done. Specifically, the following format string is used ```%s.%s@%s``` If it matches the mac_info encrypted text sent by the connector, then you can continue, if not...you can't. To encrypt this information, the HttpAccountManager.checkHmac function is used.The encryption is SHA256  
  
#### Different JAVA units involved in this process

EngineLogDefine
ConnectorInfoDefine
EngineLogDAO
HttpAccountManager
DispatcherLoader
DispatcherModel
LoginDefine
HttpRequestModel
WebClient
AccountVO
HttpAccountManager
HttpLoginSession  
  
#### Entry points to use from the connector

- **loginUri:** /outgame/login
  - LoginDefine: login to display the account data in the connector's browser
- **createUri:** outgame/create.
  - AccountCreateDefine: account creation
- **loginmergeUri:** /outgame/loginmerge.
  - LoginDefineMerge: no idea... we ignore it
- **engineUri:** /outgame/engine
  - EngineLogDefine: registers the "engine" which is a parameter sent by the connector (not ours) in the DB. Specifically in the "app_engine_log" table. Then it sends the GMs a warning message with the text:
An unauthorized engine usage account has been detected. Account[%s] Engine[%s]
- **processUri:** /outgame/process
  - ProcessMergeDefine: no idea, we can ignore it.
  
### Dungeons limited by time

For a dungeon to have a time limit, an entry for the dungeon is created in the dungeon_timer table and then each account, when using the dungeon, will create a record in dungeon_timer_account which sets the time limit.

### Stores

How the stores work. Is quit simple. When you press in a NPC that will have a shop, is necesary that this NPC has a UML dialog with the special text for shop:

    ```<panel type="Panel_Shop_Bt_Buy"><button action="buy"/></panel>```

### Stores Problem

Right now we have a problem that we already had in other versions of the server. When we open a shop, the text shown for the items for sale is in Korean even though the same items in the inventory show their name in English (taken from the desc.tbl file). It is evident that the reference of the objects is not being sent to the client, but rather a fixed text in Korean. Let's study the process.   
   
Currently, the DB table that contains the list of objects sold each time is the "shop" table. If we look for this table in the code we will see that it is used in the following units:

- ShopTable.java
- NpcShopTable.java
- AdenShopTable.java

If we use the ShopTable unit (for example), we see that the store configuration is stored in an object of the L1Shop class. In this class we have the ```getSellingItems()``` function that returns the list of items being sold (L1ShopItem), but there is still no Korean text. This class is used in units:

- S_ShopSellPledge.java
- S_PremiumShopSellList.java
- S_PersonalShop.java
- S_ShopSellAden.java
- S_ShopSellList.java

If we use the S_ShopSellList.java unit (for example), we see that the list of items is sent, and in this construct a text string is created at runtime that is the name of the item with modifications, such as the enchantment level ( +1, +2, etc) or if it is Blessed. This string is constructed using the text returned by the ```getDescKr()``` function which returns the Korean literal from the database. The solution is simple, change ```getDescKr()``` to ```getDesc()```, since this function will return the reference of the object, always using the version of the local Desc.tbl file.

### Chat drop text lines

Related with the previous point, when the game is showing a drop in the chat screen, is using the item name and also the NPC name that dropped the item. Like before, if we use the function ```getDescKr()``` in the units involved, then we will have a korean name insteed the reference of the item or npc. 

Here we have a list of units that are ussingg this function ```getDeskKr()``` where maybe is not necesary to change it:

- DeathPenaltyItemObject: seems to be a log text about lost items when dead and drop. We can't nont change it until we will have the desc_en field.
- TreasureIsland: load a treasure box from the bin files loaded, so we can't not change it until we will create the field desc_en and we will change all the bin loaders.
- L1RobotAI: used for a console message to say the item droped. We will update it with desc_en
- C_LoginToServer: again, a console message. We will update it with desc_en
- C_PickUpItem: used to send a message to the manager, so not necesary now, only when we will change to desc_en and if we want to have the manager.
- C_ShopAndWareHouse: to add a DB log, so we will change it for desc_en
- AdenShopTable: only to calculate a data lenght. We will need to check it in future.
- CharacterCompanionTable: to store the record in the DB.
- LogTable: DB Logs.
- NPCSpawnTable: to create new spawn in DB.
- NPCTable: used in the function ```findNPCIdByName``` that is used when the GM want to create an spawn of a monster by name.
- SkillsTable
- L1PcInventory: used in the function ```getNameEquipped``` to check something. Will be necesary to change it for desc_en
- L1Trade: used to send a message to the manager.
- L1PetInstance: used to change the name of the pet. Will be necesary to change it for desc_en
- L1FavorLoader: used to load the items of the favor system.
- Enchant: to send the enchant to the manager
- OmanRandomAmulet: to check if is a high amulet with a fixed text
- BuyAction: check something
- NonNpcBuyAction: check something
- S_Extended: here we have a function that makes a packet with WriteBits and use the descKR. Need to see how to solve this and if is necesary.
- S_AttendanceBonusGroupInfo: same as before
- S_AttendanceRandomBonusGroupInfo: same as before *** in s_shopSellList is used the reference in a packet operation with WriteBits without problems.
- GuideResponse: WEB unit, not necesary for now.
- MarketLoader: WEB, not necesary for now.
- L1InfoDAO: WEB, not necesary for now.
- DropEdit: Manager. 
- PlayerInvetory: Manager.
- ShopEdit: Manager.

Probably we will need to update these units:

- C_NPCAction: in a function houseBuyPayment. Need to check it

Was necesary to update the function name in these units: 

- EventPushManager: when a user receives something from the game.
- L1Gambling, L1Gambling3: a kind of trade, maybe the main trade window, but i think that is a result of a minigame. ATTENTION!!! THESE UNITS USE ```S_MessageYN```
- C_Door: i don't know for what is used this unit, but have a procedure "createItem" where is this function used.
- A_AttendanceReward: when you get a reward to get a message. 
- A_CompletedAchievementReward: reward unit
- A_EventPushItemListReceive: maybe for events, don't know, anyway it's changed here.
- A_HuntingQuestReward: maybe when you receive an item from the trainer becouse you have done a quest.
- A_PersonalShopOpen: maybe when you open a shop and you get the adena from the buyer.
- L1AllPresent: When someone send a present to other player.
- L1LevelPresent: Maybe presents when you reach a certain level.
- Fishing: When you get a fish.
- War: When you finish a war, sometimes you receive an item.
- ConnectRewardTable: unit to control all the rewards defined in database and with a systemmessage with the text that we search (5k adena reward). We have introduced english text, maybe in the future, after change all the korean text to references will be good to change the english text here in the same way.
- Beginner: maybe the beginner items, not sure.
- L1ItemInstance: in the function ```getLogName``` that is used in a lot of other units like drop units. We have created the new function ```getLogNameRef``` and replace in a lot of other units the gegtLogName for getLogNameRef. Inside these units we have the korean text ```(각인) ``` that will be necesary to introduce in the file ```desc-x.tbl ``` to use the reference insteed the fixed text.
- L1NPCInstance: here we have the function ```setting_template``` that use the korean function to ```setName()```. Don't know what will happen if i change that. Ok, the drops are fixed, but then a few parts of the code will not be correct, becouse use ```getName()``` to compare to fixed korean npc names. We need to find another solution, like look where is used ```getName()``` to change for ```getDesc()``` (this function have the correct reference). Another solution will be locate in code where are these comparations made (```getName().equalsIgnoreCase```) and replace with the reference function and the fixed reference text (```getDesc().equalsIgnoreCase = '$111'```). At the end we have done a mix, in some cases we have used the desc function and in others, we have replaces for a new function ```getKoreanName()```, that is defined in L1Character and of course returns the original korean name. In this unit we call the ```setKoreanname()``` function to set the korean name in the npc.
- L1PCInstance
- L1NPCMakeItemAction: maybe item creation
- FrancuAction
- ItExplorerAction
- JCrystalAction
- S_RetrievePackageList

After change all of them, the npc name was still a korean fixed text, that can be solved in this unit:

L1NPCInstance: Uses the function ```setting_template``` to set the name of the NPC with the korean fixed db text. If we change this for the reference, we will get the name of the NPC in english or korean correctly. But we need to check if we have not fucked another thing (becouse the korean name is used as CONST in other code places).

### Reward System

There is an automatic reward system implemented on the server. The operation is very simple.

- We have a ```connect_reward``` table that contains the information about the rewards that users get automatically. This table indicates the time interval for the reward, the amount and the item that is received. You can also specify a start and end time to limit rewards at certain time intervals (such as events).
- The content of this table is loaded into the unit ```ConnectRewardTable.java```
- There are two types of rewards. The ```NORMAL``` ones that are always given and the ```STANDBY_SERVER``` ones that are only delivered when the server is configured as ```STANDBY``` (parameter included in the file ```server.properties```).
- The unit that makes use of the information loaded in the previous class is ```ConnectReward.java```, which is a class that implements ```ControllerInterface```.
- In the game we have three timers that control all the classes that implement ```ControllerInterface``` and that are  ```ChangeHoursController.java, ChangeMinutesController.java and ChangeSecondsController.java```. As we can see, each one of them is a timer that is fired every hour/minute/second respectively and that in each event calls the ```execute()``` function of each class that implements ```ControllerInterface``` and that is included in one of the three "mother" classes. In this case, our ```ConnectReward``` unit is included in the ```ChangeMinutescontroller``` class, so the check will be done every minute.
- We have added a new field ```description_kr``` to have the original korean description, becouse the original field ```description``` is used to send the message to the players, so we have translated the korean text to english text. In the future we can change that for a reference and include it in ```desc_x.tbl```
 
### Desc_kr

In the database there are many tables that have the field ```desc_kr``` with Korean text. It is found in the following tables:

- bin_*
- armor
- weapon
- etitem
- npc
- polymorphs
- skills

Its value is loaded when reading the tables with setDescKr()
Its value is obviously read with getDescKr()

It is used from the following code points:

- **ItemDAO, MPSECore:** Direct database access (App Center)
DropEdit, DropMonsterFind, PlayerInvetory, ShopEdit: Database shortcut (LinManager)
- **L1Autoloot, L1Search:** Direct database access (GM Commands)
- **GMWand:** Direct database access (The GM's magic wand)
- **Bin Loader.** Direct database access. In this case, if we put the file "desc-e.tbl" on the server and force the loading of the BIN tables, the desc_kr field will have the text in English instead of Korean.
  
### Quest System

Actually, when we create a new char we start in a Begginer area and we have a lot of quest to make that shows how to play the game. They are very ussefull and the player will get different ones until lvl 90 (more or less, need to check it). But, how is working this system? How can we change it?

- Fixed texts. The file that has the fixed texts that are used in the different quest dialogs are located in the file ```QuestDesc-x.tbl```
- Quest Config file. We have a config file ```quest.properties``` where we can configure the general parameters for the quest system. This file is loaded in ```QuestConfigure.java```
- Table beginner_quest: We have this table in the database where all the quest are defined, with a id number and a few parameters like the level trigger. The server is using this table to load in memory the different quest, but also, is completing the info that needs to run the quest from the binary file ```quest-common.bin``` that is loaded in memory in the unit ```QuestCommonBinLoader```. Using the ID of the quest readed from database, it will get all the info about this quest in a class ```QuestT``` that has all the important information about it, like pre requisites, objetive list, reward list, teleport info, etc. The table is loaded in unit ```BeginnerQuestTable.java```.
- Table beginner_quest_drop: Like before, is a table with drops obtained in every quest. Used to load the info into memory.
- character_beginner_quest: In this table the server is saving the state of the quest for every character, that means that every time a character ends a quest, is writted here.

Step by Step:

- When the server is initialized (```GameServer.java```), the class ```BeginnerQuestTable``` will be loaded with the quest info taked from database and binary files.
- When a quest is finished, the client will call the unit ```A_QuestFinish``` to say to the server that a quest was done. En esta unidad, se hacen varias cosas:
  * Cargar el estado actual de la quest que indica el mensaje del cliente (```L1QuestProgress progress = _pc.getQuest().getQuestProgress(_id)```)
  * Realizar comprobaciones sobre la quest para ver si ya se terminó anteriormente o si realmente no está completa, etc.
  * Establecer su hora de finalización (```progress.setFinishTime(System.currentTimeMillis())```).
  * Obtener la lista de las recompensas que se obtienen por haber completado la quest (```RewardListT rewardList = progress.getBin().get_RewardList()```).
  * Dar la recompensa al personaje (```reward(rewardList, optionalReward)```)
  * Decirle al cliente que efectivamente, la quest se ha completado (```_pc.sendPackets(S_QuestFinish.getFinishPck(_id))```)
  * Actualizar el progreso de las quests del personaje (```BeginnerQuestUserTable.getInstance().update_progress(_pc)```)


L1Quest, L1QuestCollectItem, L1QuestDropItem, L1QuestKillNPC, L1QuestProgress, L1QuestTemp
HuntingQuestTable, HuntingQuestTeleportObject, HuntingQuestUser, HuntingQuestUserTemp
CompensatorElement, BuffCompensator, ExpCompensator, ItemCompensator, A_HuntingQuestReward

En algunos puntos del codigo hace estas llamadas: 
  ```if (pc.getQuest().getStep(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END```
Comprobar que es 

- Items. We have in DB the table ```item_box``` where the boxes obtained in the quest are defined. This table is loaded in the class ```ItemBoxTable``` and this class is used in the class ```ItemBoxOpen```

### Treasure Island

It has several steps:

1. The necessary instances for this to work are loaded on the game server
TreasureBoxCommonBinLoader.getInstance()
L1TreasureBox.load() (not sure if this class is used in treasure island)

2. With the command "treasureisland start" you can force the event to start, as it normally starts automatically every day, depending on the server configuration.
The first thing the command does once executed is instantiate the L1TreasureIslandCommand class, which in turn instantiates the TreaureIsland class.

3. TreasureIsland Class
From here the command is processed: command() -> commandStart() -> start()

A message is sent to everyone warning that the treasure island has begun
L1World.getInstance().broadcastPacketToAll(TreasureIslandUtil.TREASURE_OPEN)

From this moment on, the npc with id 18309 found in Heine is prepared to teleport those who want to the treasure island with the action "a"
This NPC has a record in the table `npcaction_teleport` with this info:

INSERT INTO `npcaction_teleport` VALUES ('18309', '달의 기사^질리언', 'a', '0', '0', '0', 'ITEMID:40308\r\nCOUNT:10000\r\nREMOVE:true\r\n===============', null, 'false', '0', '0', '0', '0', 'inter', null, '52', 'none', null, null, null, null);


In this same class we have the enter() procedure that is executed when you teleport to the island and that does the following:
Makes you a special polymorph
L1PolyMorph.doPolyTreasure(pc)
Adds the shovel to your inventory
L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.TREASURE_SHOVEL); //500231
There is a special item, sold by an NPC located next to the previous one, and which reduces the time needed to dig.
TREASURE_COMPASS: 500230

4. Once on the island, when you locate treasure and use the TreasureDetectShovel
The object id is obtained by extracting it from the read data packet. This object is actually an NPC that has no visible sprite.
packet.readD();
Now we try to locate which treasure corresponds to that item.
L1TreasureInstance treasure = (L1TreasureInstance)L1World.getInstance().findObject(objid);
As has been said, these treasures are actually NPCS with ids ranging from 18302 to 18305 and which of course have spawn in the "spawnlist" table, specifically those ranging from 18300 to 18311.
Once the treasure is located, a method from the TreasureIsland class is used to send the treasure found.
TreasureIsland.excavationTreasure(pc, treasure) (*see below in point 5)
Once the treasure is found, it is marked as already collected with
treasure.setExcavation();
and is removed
treasure.deleteMe();

5. TreasureIsland.excavationTreasure
The first thing to do is check the grade of the treasure found among the 4 possible ones:
normal, advanced, top grade, Eva's
If it is a legendary treasure (Eva's) a message is sent to the server saying that someone has found a legendary treasure.
Finally, you obtain the object corresponding to the level of the treasure, which will always be a chest, which when opened will provide the corresponding object(s) at random.
TreasureIslandUtil.excavateReward

(*) TreasureBoxCommonBinLoader.getInstance()

Since it is not yet instantiated, call create
new TreasureBoxCommonBinLoader()
In create, load the data
loadFile()
In the loadFile, two instances are created
bin = TreasureIslandBoxCommonBin.newInstance()
reward_bin = TreasureIslandBoxRewardCommonBin.newInstance()
And then fill in your details
bin.readFrom(ProtoInputStream.newInstance("./data/Contents/treasureBox-common.bin"))
reward_bin.readFrom(ProtoInputStream.newInstance("./data/Contents/treasureIslandRewardBox-common.bin"))
This data is used since
getBox()
getReward()

### Teleport to Event Message

En algunos eventos como el de "Tower of Domination", cuando el evento está activo, al hacer click sobre el evento aparece un mensaje que dice: "Would you like to teleport to Tower of Domination? 1000 Adena". Este es un mensaje de servidor (S_ServerMessage) con el índice 4664, es decir, que usa el fichero "string.tbl". Pero hay algunos eventos que NO lanzan este mensaje, así que la pregunta es: como se contrala qué eventos tienen este mensaje disponible y qué eventos no. Las unidades implicadas son: 

- NotificationTable. Esta es una unidad que se encargará de leer de la base de datos la información sobre los eventos que tienen lugar en lineage. Para ello accederá a la tabla "Notification" y leerá todos los registros que contenga, los cuales definen las características de cada evento. En concreto el campo de esta tabla que nos interesa es "teleport_loc". Por cada registro leido, se creará una instancia de la clase L1Notification. Todos estos objetos se insertarán en la lista interna que usará la case para devolver la información corcreta sobre un evento.
- L1Notification. Clase que se corresponde con un evento. El campo de la tabla antes mencionado es el que se usará para la función "getTeleportLoc()" que tiene esta clase para indicar si un evento posee la capacidad de provocar un teleport o no ya que esta función devolvera NULO o unas coordenadas.
- S_Notification. Esta clase es la encargada de enviar al cliente todos los eventos que discurren en el juego. Para ello recorre todos los eventos (objetos L1Notification) contenidos en la clase "NotificationTable" gracias a su función "getNotifications()". A partir de esta lista de objetos, crea las notificaciones y mensajes al cliente. Cuando está procesando dichas notificaciones comprueba si la notificación tiene definido alguna localización de teleport con "getTeleportLoc()" y si la tiene, entonces crea los bytes que contienen dicha información a través de la linea de código "os.write_teleport(TELEPORT_BYTES);". Esa variable "TELEPORT_BYTES" se rellena usando la función "getTeleportBytes()" que básicamente llama a la clase NotificationTeleportDataStream que devolverá los bytes necesarios. En el nuevo códgo de Paladin, esta unidad se llama "S_Alarm"
 - NotificationTeleportDataStream. Desde esta unidad se escriben los bytes necesarios para la configuración del mensaje, es decir, el índice usado en "string.tbl" (4664) y la cantidad de adena (1000) ambos valores son constantes de esta clase. 
- A_NotificationTeleport. Esta es la unidad que se ejecutará en el servidor si el usuario ha pulsado sobre el botón "Confirm" cuando el cliente le ha preguntado si desea teleportarse. Esta unidad primero comprobará si es posible hacer el teleport (por ejemplo comprobando que se tiene suficiente adena) y a continuación hará el teleport del personaje a las coordenadas indicadas que se obtienen gracias a la linea "L1Notification noti = NotificationTable.getNotification(readC());" que básicamente recuperará el objeto L1Notification correspondiente gracias a su identificador único incluido en el mensaje de entrada que llega al servidor.

### Character Slot Available Increase

En los ficheros de configuración del servidor se establece los slots máximos para personajes que tiene cada cuenta: Config.SERVER.CHARACTER_SLOT_MAX_COUNT
Actualmente tenemos configurado este valor maximo como 8 aunque el cliente soporta hasta 10 caracteres diferentes por cuenta. 
Para poder incrementar este valor, los usuarios pueden usar el objeto con id 210083 [태고의 옥쇄 (캐릭터 슬롯 확장) / Ancient Royal Seal (Character Slot Expansion)].

Unidades relacionadas: 

CharacterSlotExtend
A_ExtendCharSlot

server.properties
CHARACTER_SLOT_MAX_COUNT = 8

REVISAR


### Drop System

The game has configured the drop rates in the confi file ```rates.propierties``` with a few parameters like RATE_DROP_ITEM. These parameters are used in the function ```setDrop()``` located in the unit ```DropTable.java``` to calculate the final drop rate of the items in the game.

This unit has an internal list named ```_droplists``` where are stored all the drops from all monsters. 

### ServerMessages

In the ```L1ServerMessage``` unit there are a multitude of server messages defined that are then used in the different server units such as ```pc.sendPackets(L1ServerMessage.sm7552)``` which is internally translated into the call ```S_ServerMessage(7552)```. This in only a message to the server where the numerical index indicates the line that will be used from the ```string.tbl``` file, just as is done with the references ```$123``` and the ```desc.tbl``` file (that is, we will also have to add two to the index to know the line number).

But these messages have an advantage over those that use ```desc.tbl``` and that is that they allow parameters. For example, the call ```pc.sendPackets(L1ServerMessage.sm337_GEMSTONE)``` is translated internally into ```S_ServerMessage(337, "$5240")```, that is, it has a parameter, since the message with index 337 (i.e. line 339 in ```string.tbl```) is ```Not enough {0}%s.```. The parameter ```$5240``` is a reference and therefore will be resolved using the file ```desc.tbl``` in whose line 5242 we find the text ```crystal```, so the message that will receive the player will be ``Not enought crystal``.

The ```S_ServerMessage``` supports up to 5 parameters, that is, from ```{0}``` to ```{4}```

### Different ways to send information from Server to Client

Aquí tenemos que explicar los diferentes métodos, como el systemMessage, serverMessage, notice, greenMessage etc. Que implican, como se muestran, si admiten referencias, etc.

## Einhasad's Faith

Now in the game there is a yellow icon (almost at the end) that launch a new system Eiinhasad's Faith, where we can "buy" ussing the Einhasad points, different beneffits (in form of skill with due time) like +1 Hit, +1% EXP, +1% MR, etc.

How is loaded:

C_LoginToServer.login() -> pc.createEinhasadFaith();
L1PCInstance.createEinhasadFaith() -> new EinhasadFaithHandler(this);
EinhasadFaithHandler.create -> EinhasadFaithHandler.init()
EinhasadFaithHandler.init() -> EinhasadFaithLoader.load() -> (read data from DB)
                            -> EinhasadPointFaithCommonBinLoader.getGroupListT()
                            
When you buy one of more of them, they are stored in the table ```character_einhasadfaith``` that is loaded in the unit ```EinhasadFaithLoader```, that is used in the unit ```EinhasadFaithHandler```  where the skill is activated

## Relic

https://lineage.plaync.com/powerbook/wiki/%EC%84%B1%EB%AC%BC+%EC%8B%9C%EC%8A%A4%ED%85%9C

## Web / App Store
  
The web is generated automatically by the server, which integrates, in addition to the Lineage server, a web server. For this, it uses both html, js, css files etc. such as various settings and data that are saved in the DB (such as the cover slide show, newsletter or announcements). Let's see the different parts of the web and where your data or configuration is obtained from:

- **Main menu:** ```cnb.xml``` file that is loaded in the ```cnb.java``` file
- **Secondary menu with icons:** directly in ```index.html```
- **SlideShow:** DB Table ```app_promotion``` that is loaded in the file ```PromotionDao.java```
- **First 4 horizontal sections:** The titles of the 4 sections directly in ```index.html``` and their content in the DB table ```app_board_notice```
- **First line of content (updates):** DB table ```app_powerbook```
- **Second and third line of content:** DB table ```app_board_content```
- **Shop items:** DB table ```app_nshop```
- **Footer links:** ```index.html``` file  

## Como funciona el powerbook

Tenemos la unidad L1InfoDao donde seleccionará toda la información relevante y la agregará a un objeto en memoria "data". Para los NPC esta información la construye obteniendo los NPCS directamente
de la instancia NPCTable. Para los Items (armor, weapon, etc) sin embargo, usa una clase que es ItemDao que tiene una consulta personalizada para obtener los items. 

## Adaptar el código al Paladin Pack

Para poder adaptar el código al Paladin Pack hacen falta los siguientes cambios:

1. Nuevos opcodes. Ya tenemos una unidad con los opcodes del paladin usando los mismos nombres que los actuales. Solo hay que sustituir una unidad por la otra.
2. En la unidad ```VersionConfigure.java```, cuando se inicializa la variable FIRST_KEY, su primer valor debe ser el mismo que hasta ahora pero +1. Es decir, hay que cambiar ```FIRST_KEY[i] = (byte) ((array.length + 1) & 0xff)``` por ```FIRST_KEY[i] = (byte) ((array.length + 2) & 0xff)```.
3. El fichero de configuración ```version.properties``` tiene valores nuevos para: 
    + FIRST_KEY
    + CLIENT_VERSION
    + MSDL_VERSION
    + LIBCOCOS_VERSION
4. El fichero de configuración ```connector.properties``` tiene valores nuevos para: 
    + CONNECTOR_CLIENT_SIDE_KEY
    + CONNECTOR_DLL_PASSWORD
    + CONNECTOR_LINBIN_SIZE
    + CONNECTOR_MSDLL_SIZE
    + CONNECTOR_LIBCOCOS_SIZE

## Añadir el servidor a un Virtual Host.

Lo primero, es entrar en el VHost y habilitar los puertos en las iptables, o de lo contrario no estarán accesibles desde fuera. Eso se hace con el comando iptables, por ejemplo con los puertos 2000 y 2001:

[root@server]# iptables -I INPUT -p tcp -m tcp --dport 2000 -j ACCEPT
[root@server]# iptables -I INPUT -p tcp -m tcp --dport 2001 -j ACCEPT

Para redireccionar el dominio que crearemos, el lineagewarrior.com, como tiene que apuntar a la ip+puerto, será un problema, pero igual podemos con esto:

<VirtualHost *:80>
    ServerName dominio.com
    ServerAlias www.dominio.com

    ProxyPass / http://111.111.1.1:8082/
    ProxyPassReverse / http://111.111.1.1:8082/
</VirtualHost>

Pero creo recordar que estas entradas vittualhost que usa el apache, con el plesk se crean automaticamente, aunque creo que se puede decir explicitamente como. Investigar esto

Auto generar ficheros PFX para firma de ejecutables
https://www.advancedinstaller.com/what-is-pfx-certificate.html
Incluir firma en el Setup 
https://revolution.screenstepslive.com/s/revolution/m/10695/l/563371-signing-installers-you-create-with-inno-setup

## Cosas que igual se pueden borrar....

Tenemos la tabla beginner_box que tiene definidos objetos para cada clase. Dichos objetos se obtienen si se hace click en el item 1000010 (AddItem.java), pero ese item no es un drop de nadie ahora mismo ni hay unidad que lo cree.
Aunque también es verdad que pueden ser items que se crafteen... 

beginner_teleport: si hay aquí bookmarks creados, se crearán con cada nuevo personaje. Parece que ya no se usa por el Elephant book ese que se consigue que tiene muchos bookmarks.

## Smelting Stone
Los items se llaman: 
  - Refining Stone Lvl 1 : MR (itemId 31367)
  - Refining Stone Lvl 1 : Absolute HP Recovery
  - etc.

con itemId desde el 31367 al 31468 y con useType = 'SMELTING'

## Enchant an item with Scroll of Enchant Armor xxxxxxx

Cuando usamos un Scroll of Enchant Armor (los hay de muchos tipos diferentes), al hacer click sobre el scroll se instancia la clase ```EnchantArmor``` a través de su método ```clickItem()```. 
Dentro de este método primero se hacen multitud de comprobaciones para ver si realmente puede usarse el scroll o no. Una vez comprobado que si es posible, se hace uso de el pudiendo tener éxito o no. Si no se tiene éxito, se llamará al método ```failureEnchant``` pero si se tiene éxito se llamará al método ```successEnchant```, ambos definidos en la clase ```Enchant```. Dentro de este último procedimiento, se harán varias cosas:

  - Si el item está equipado se quitarán todos los beneficios que pueda proporcionar el objeto. Esto se hace porque hay muchos objetos (como los amuletos), que cuando suben de nivel de encantamiento, proporcionan unos mayores beneficios, por lo que es necesario eliminar los "antiguos beneficios" antes de aplicar los nuevos con ```inventory.removeItemAblity(item)```
  - Se actualiza el nivel de encantamiento del objeto.
  - Se actualiza el AC actual que tiene el personaje en función del nuevo AC que proporcione el objeto. Para los objetos normales se usa el método getAc() y para los amuletos el getAcSub(). El valor del AC se gestiona a través de una clase específica ```L1AC```. (Este punto concreto no se estaba haciendo. Arreglado el 23/02/2024). 
  - Se activan los beneficios que tiene el objeto con su nuevo nivel de encantamiento con ```inventory.setItemAblity(item)```
  - Una vez actualizado todo, se comunica al cliente los nuevos valores del estado del personaje, usando para ello el mensaje de servidor de la clase ```S_OwnCharAttrDef```
  - Hay también otro mensaje de servidor que actualiza otros valores del estado del personaje como sus stats o su nivel: ```S_OwnCharStatus```

  NOTA: Cada vez que un personaje equipa un objeto, se hace uso de la clase L1EquipmentSlot en sus métodos ```setWeapon```, ```setArmor``` y ```setArmorSet```. De igual forma, cuando se desequipa un objeto, se usan los metodos ```removeWeapon```, ```removeArmor``` y ```removeArmorSet```.

## Security Buff

Existe un buff de AC que da -1 extra y +50hp y que se llama Security Buff. Está representado por el icono ```ICON_SECURITY_SERVICES``` y se da al usuario cuando entra en el juego con el procedimiento ```C_LoginToServer.securityBuff()```.
Dentro de este procedimiento se comprueba si el usuario ha definido el número de teléfono movil en su cuenta, a través del comando de usuario ```.phone```

Si no se tiene definido el teléfono, entonces el servidor manda un ```L1GreenMessage``` del tipo ```SECURITY_BUFF_EXPLAN``` (esto sería conveniente programarlo para que se hiciera un minuto despues de entrar al servidor, ya que no se puede ver).

## Inventario

Si se tienen demasiados objetos en el inventario (más de 200) el cliente recibe el mensaje de error (sm82) ```Your belongings are very heavy and you cannot carry more.```, da igual si los items tienen un peso de 0. Más de 200 y obtenemos ese error. Este máximo de 200 está guardado en una variable L1PcInventory.MAX_SIZE y no parece que pueda configurarse en nigún sitio. Quizás sería buena idea añadir un parámetro de configuración.

## Forgotten Island

El funcionamiento de Forgotten Island es el siguiente.

- Existe una configuración para la isla en el fichero ```dungeon.properties```:

    + ISLAND_LOCAL_ACTIVE; Si la isla está activa o no
    + ISLAND_DAY_LOCAL: Los días en los cuales la isla está activa (0=Domingo)
    + ISLAND_OPEN_HOUR_LOCAL: La hora a la que se abre la isla.
    + ISLAND_CLOSE_HOUR_LOCAL: La hora a la que se cierra la isla.

- En paralelo a esta confiuración, existe una configuración en la tabla de la BD ```bin_ship_common```. Aquí se definen los barcos que viajan entre Heine y FI, las horas a las que salen y su recorrido.
- La unidad ```SpecialDungeon``` es la encargada de iniciar o parar la isla en función de la configuración a través de los procedimientos ```forgottenIslandLocalOpen()``` y ```forgottenIslandLocalEnd()```
- Hay que tener en cuenta que los barcos son mapas, como los de una dungeon. En concreto el barco de Heine a FI es el mapa 83 y el de FI a Heine el mapa 84.
- Además, existe una notificación global de la apertura y cierre de la isla, la cual se encuentra en la tabla ```notification```, la cual se usará en las unidades ```NotificationTable```, ```L1Notification``` y ```S_Notification```
- La apertura y cierre de la isla también puede hacerse de forma manual gracias al comando ```.forgottenisland start/stop``` que hará uso de la unidad ```L1ForgottenIsland```
- En la unidad ChangeMinuteController existe un control para comprobar si algún personaje está todavía en los mapas 83 o 70 (Forgotten Island) una vez que la isla está desactivada o bien si su level a bajado del 85, para teleportarlos automáticamente a Heine. Además, también en esta unidad, cada minuto se ejecutará la instancia de la clase ```ShipTime```.
- Las unidades directamente relacionadas con el funcionamiento de los barcos son:

  + ShipCommonBinLoader: Unidad encargada de interpretar los datos contendidos en el fichero del cliente ```ShipInfo-common.bin```
  + ShipInfo: Unidad encargada de interpretar los datos contendidos en el fichero del cliente ```ShipInfo-common.bin```
  + ShipCommonBin: Clase que contiene la información relacionada con cada barco, como su horario o su estado.
  + L1ShipStatus: aquí se definen los diferentes estados del barco: NONE, STAY, LEAVE, ARRIVE
  + ShipTime: Desde aquí se controla cuando debe partir o llegar un barco a su destino, controlando el tiempo actual con respecto a las horas de salida, llegada y duración del viaje. Desde aquí se consumirá el ticket cuando el barco comience su travesía y también teleportará a los jugagdores cuando el barco llegue a su destino.
  + L1ShipTime: Unidad que encapsula la información sobre un barco. Su salida, su estado, etc.
  + C_EnterShip: Unidad que será invocada cuando el jugador intente entrar en un barco, gracias a el protobuf que enviará el cliente. Aquí se controla si un jugador puede subir al barco, ya que comprueba el nivel, el ticket, la hora, etc.
  + C_LeaveShip: Esta unidad parece que ya NO se usa.
  + Dungeon: Antiguamente, las entradas a los barcos debían estar aquí definidas, pero ya no es necesario, ya que se controla directamente desde el cliente.

## Regalos automáticos 

Se puede configurar el servidor para que todos los jugadores reciban determinados items si están conectados en el servidor a determinadas horas. 

EventPush.properties
	
	HOT_TIME_ENABLE = true
	HOT_TIME_HOUR = 10, 14, 20
	HOT_TIME_ITEM_ID = 1000031, 3110123
	HOT_TIME_ITEM_COUNT = 1, 1

ChangeHourController

public void onHourChanged(l1j.server.server.model.gametime.BaseTime time)
			if (Config.PUSH.HOT_TIME_ENABLE && Config.PUSH.HOT_TIME_HOUR.contains(hours))
				EventPushManager.getInstance().hot_time_push();

EventPushManager
	
	Usa el getDesc para crear textos en diferentes sitios, pero al final ese texto se usa haciendo la llamada: 

	return push.create(target, item.getItemId(), getTile(item, count, enchant), getContent(item, count, enchant), count, enchant, false);
	
	(en el tercer y cuarto parametro, title y content)
	
y que es push?

EventPushLoader push = EventPushLoader.getInstance();	

así que ...

EventPushLoader.create()
	EventPushUser user = null;
	user = new EventPushUser();
	EventPushObject obj		= new EventPushObject(++_id, subject, content, null, item.getItemId(), item, item_amount, item_enchant, used_immediately, 0, enable_date, 0);


## Como obtener puntos account.einhasad

- Al participar en la IceRaid (2500000 / 500000 en función de si es la versión HARD o la normal)
- Como resultado de algunas Quest (Config.EIN.REST_EXP_DEFAULT_RATION * 150 / Config.EIN.REST_EXP_DEFAULT_RATION * 300)
- L1PCInstance.einGetExcute: 
	al recibir de forma automática el einhasad blessing del servidor (ver envios automáticos)
	al consumir algunos de los AttendanceReward
	al matar a algunos npc especiales
- DragonGemStone.useDragonGemstone
	al consumir DRAGON_DIAMOND, DRAGON_HIGH_DIAMOND, DRAGON_SAPPHIRE, DRAGON_RUBY, ETC: 
	1000004, 1000007, 1000009, 1000003, 1000002, 510139, 600342, 600343, 600344, 600348
- DragonGemStone.useEtcGemstone

Como se pierden puntos einhasad: 

- Al pescar 
- L1ExpPlayer.einhasadConsume
- Al cazar

## Cuando NO se obtiene un drop de un mob

DropTable.drop()

- Si el servidor está en standby (Config.SERVER.STANDBY_SERVER)
- Si no se tiene inventario (inventory == null)
- Si no se tiene configurado el autoloot (Config.ALT.AUTO_LOOT)
- Si no se está dentro del rango máximo para el autoloot (Config.ALT.LOOTING_RANGE)
- Si se tiene configurado la multa por no tener einhasad (REST_EXP_ITEM_PENALTY)
- Si el item dropeado es adena y se está en una zona de principiantes, entonces la adena no se dropea.
- Si el item es el knight coin y no se está en una zona de principiantes o el level no está por debajo de 80, el knight coin no se dropea.
- Si se tiene configurado la multa y no es un item de quest y no se tienen puntos de einhasad, entonces:  
	si no se está en un PcRoom (PcCafe) y no se tiene el einFavor (isEinFavor), no se dropea.
		** Si se tiene el efecto "pc.getSkill().hasSkillEffect(L1SkillId.EINHASAD_FAVOR" entonces isEinFavor = true, si no: false.
	si se está en un PcRoom o se tiene el einFavor entonces se calculará si se tiene drop de forma aleatoria en base al valor de einPenaltyProb
		** Si No se tiene configurada la multa o se tienen suficientes puntos Einhasad (en la cuenta), entonces einPenaltyProb = 150. En caso contrario einPenaltyProb = 100.

## Player Support System (PSS)

Este sistema se configura en el fichero playsupport.properties

- PLAY_SUPPORT_ACTIVE: Whether to use play support
- PLAY_SUPPORT_TIME_LIMITED: Wheter PlaySupport is Limited by time
- PLAY_SUPPORT_TIME_DAY_ADD: If PlaySupport is time Limited, how many free time account gets every login day (minutes)
- PLAY_SUPPORT_TIME_MAX: If PlaySupport is time Limited, maximal ammount of PSS time is possible to have (minutes)
- PLAY_SUPPORT_ALL_ACTIVE: Wheter full PlaySupport (all) is enabled

La funcionalidad del control de tiempo ha sido añadida por nosotros, no es algo oficial del Lineage. La configuración del PSS ALL también.

Unidades relacionadas:

- Account: En esta unidad tenemos el método addPSSTime() el cual, cada día, añadirá el tiempo indicado en la configuración del PSS (si el límite de tiempo está activo).
- C_LoginToServer: Aquí mandaremos al cliente la notificación para que muestre el icono del PSS limitado por tiempo en caso de ser necesario.
- A_PlaySupportStart: Unidad que es invocada cuando el PSS es activado en el cliente. Desde aquí se controla si está activado el PSS, si se está dentro del límite de tiempo, que modo se puede usar y si se tiene el nivel necesario. En caso de poder usarse, se llamarán a los métodos _pc.getConfig().setPlaySupport(true) y _pc.getConfig().setPlaySupportType(type) para establecer el modo seleccionado y su estado.
- S_StarPlaySupport: Unidad del servidor encargada de notificar al cliente si el PSS puede activarse, y si hay un error, qué error. 
- A_PlaySupportPoly: Unidad que es invocada cuando el PSS del cliente quiere usar un Polymorph en el jugador.
- A_PlaySupportFinish: Unidad que es invocada cada vez que el cliente detiene el uso del PSS. Se actualiza el estado del usuario y se para el contador de tiempo si está activado.
- L1PlaySupport: unidad para los GM. Desde aquí pueden iniciar o parar manualmente el sistema de PSS
- L1CharacterConfig: aquí está definida la función finishPlaySupport() invocada por A_PlaySupportFinish.
- S_FinishPlaySupport: unidad encargada de notificar al cliente que el PSS se ha detenido.
- L1PCInstance: desde aquí guardamos el estado del PSS (los minutos restantes) con el procedimiento save()
- S_GameGatePSSBucketName: unidad que manda un dato al cliente que no sé exactamente para qué sirve.


## Fruit of Growth 

La fruta del crecimiento es un item que al consumirlo hace que el usuario obtenga puntos TAM a intervalos regulares.
Se puede consumir la fruta con hasta 5 personajes diferentes. Cada vez que se toma una fruta con un personaje diferente, se sube de nivel TAM:

- Nivel 1. -1 AC
- Nivel 2. -2 AC
- Nivel 3. -3 AC
- Nivel 4. -4 AC,  +2 Damage Reduction
- Nivel 5. -4 AC,  +2 Damage Reduction, +5% EXP 

Name_en: Fruit of Growth (3d)
Name_kr: 탐나는 성장의 열매 3일
item_id: 600226
desc_id: $18724

No used

Name_en: Fruit of Growth
Name_kr: 탐나는 성장의 열매
item_id: 810005
desc_id: $17610

Esta fruta solo puede comprarse a través de la tienda.



## Things we need to check

- How we can get TAM points
- How to get Einhasad points
- How it works exactly the drop system becouse we are not getting drops. Einhasad maybe?

