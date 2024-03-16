echo -e "Welcome to the import/export database utility\n"
echo -e "the default location of mysqldump file is: /opt/lampp/bin/mysqldump\n"
echo -e "the default location of mysql file is: /opt/lampp/bin/mysql\n"
read -p 'Would like you like to change the default location [y/n]: ' location_change
read -p "Please enter your username: " u_name
read -p 'Would you like to import or export a database: [import/export]: ' action
echo

mysqldump_location=/opt/lampp/bin/mysqldump
mysql_location=/opt/lampp/bin/mysql

if [ "$action" == "export" ]; then
	if [ "$location_change" == "y" ]; then
		read -p 'Give the location of mysqldump that you want to use: ' mysqldump_location
		echo
	else
		echo -e "Using default location of mysqldump\n"
	fi
	read -p 'Give the name of database in which you would like to export: ' db_name
	read -p 'Give the complete path of the .sql file in which you would like to export the database: ' sql_file
	$mysqldump_location -u $u_name -p $db_name > $sql_file
elif [ "$action" == "import" ]; then
	if [ "$location_change" == "y" ]; then
		read -p 'Give the location of mysql that you want to use: ' mysql_location
		echo
	else
		echo -e "Using default location of mysql\n"
	fi
	read -p 'Give the complete path of the .sql file you would like to import: ' sql_file
	read -p 'Give the name of database in which to import this file: ' db_name
	$mysql_location -u $u_name -p $db_name < $sql_file
else
	echo "please select a valid command"
fi