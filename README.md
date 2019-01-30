# DavisBase
Implemented a database engine from command line which can support DDL, DML, VDL commands 

To run:
Java needs to be installed
Commands from commandline:
javac dbmspromt.java
java dbmsprompt

Output:
Now you will see a dbmspromt.

Commands:
“Create”:
  •	Database:
    Query: create database dbname;
    Action: 
    o	New database will be created named dbname and database will be changed to that database.
    o	Two folders user_data and catalog will be created.
    o	In catalog, two files davisbase_tables.tbl and davisbase_columns.tbl are created.
    o	If database having dbname is already there, then it will show error message.
  •	Table:
    Query: create table tablename (column1 int, column2 text);
    Action:
    o	In davisbase_tables.tbl file from catalog, table name is added.
    o	In davisbase_columns.tbl file from catalog, column name is added with attributes tablename, datatype, columnname, position,             is_nullable.
    o	In user_data, tablename.tbl file is created.
    o	If table having tablename is already there, then it will show error message.
    
“Show”
  •	Database:
    Query:	show databases;
    Action: 
    o	All database name will be showed.
  •	Table:
    Query: show columns;
    Action: 
    o	All column name will be showed.

“Drop”
  •	Database:
    Query: drop database dbname;
    Action:
    o	All the files in the database will be deleted.
  •	Table:
    Query: drop table tablename;
    Action: 
    o	Table file is deleted.
    o	In davisbase_tables, the location is updated to -1.
“Use”
	Query: Use dbname;
	Action:
  o	File Path is changed to the dbname folder
  
“Insert”
	Query: insert into tablename values (val1,val2..);
		Insert into tablename (col1, col2..) values (val1,val2..);
	Action:
  o	In tablename file from user_data folder, values are inserted.
  o	If the column is not null, and value is not given, then it shows error message.
  o	Otherwise, it will add dummy value and values are inserted.

“Select”
	Query: 
    select * from tablename;
		Select * from tablename where colname=val;
		Select col1,col2,.. from tablename;
		Select col1,col2,.. from tablename;
	Action:
  o	Values from table file are got according to condition.
  
“Update”
	Query: Update col1=val1 from tablename where colname=val;
  Action:
  o	Value from table for particular condition is changed in table file.


Assumption:
In each file, there is one column named rowid which has int datatype and it is incremented with number of records. It is considered as primary key.
