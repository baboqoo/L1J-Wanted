@echo on
C:\xampp\xampp_start
C:\xampp\mysql\bin\mysqld
C:\xampp\mysql\bin\mysql -u root -e "DROP DATABASE l1j_remastered";
C:\xampp\mysql\bin\mysql -u root -e "CREATE DATABASE IF NOT EXISTS l1j_remastered";
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\backups\2024_02_09_l1j_remastered_structure.sql
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\backups\2024_02_09_l1j_remastered_data_1.sql
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\backups\2024_02_09_l1j_remastered_data_2.sql
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\updates\2024_02_15_npcaction.sql
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\updates\2024_02_18_app_board_notice.sql
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\updates\2024_02_18_uml_conversion.sql
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\updates\2024_02_24_npc_delete.sql
C:\xampp\mysql\bin\mysql -u root -p l1j_remastered < C:\Users\Admin\Documents\GitHub\L1J-REMASTERED\db\updates\2024_02_25_npc.sql

pause