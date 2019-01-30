import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import static java.lang.System.out;

/**
 *  @author Maitri Shah
 *  @version 1.0
 *  <b>
 *  <p>This is the main file which creates interactive prompt</p>
 *  </b>
 *
 */
public class DBMSPrompt {

    /* This can be changed to whatever you like */
    static String prompt = "mpsql> ";
    static String version = "v1.01(example)";
    static String copyright = "©2018 Maitri Shah";
    static boolean isExit = false;

    /*
     * Page size for alll files is 512 bytes by default.
     * You may choose to make it user modifiable
     */
    static long pageSize = 512;
    static String dbPath="C:\\Users\\Maito\\Desktop\\Study\\Sem2\\DB\\Assignement\\Programming 2\\FinalCodes\\Databases";
    static String currentDB="";
    static String dbCatalogPath="";
    static String dbUserDataPath="";

    /*
     *  The Scanner class is used to collect user commands from the prompt
     *  There are many ways to do this. This is just one.
     *
     *  Each time the semicolon (;) delimiter is entered, the userCommand
     *  String is re-populated.
     */
    static Scanner scanner = new Scanner(System.in).useDelimiter(";");

    /** ***********************************************************************
     *  Main method
     */
    public static void main(String[] args) {

        /* Display the welcome screen */
        splashScreen();

        /* Variable to collect user input from the prompt */
        String userCommand = "";

        while(!isExit) {
            System.out.print(prompt);
            /* toLowerCase() renders command case insensitive */
            userCommand = scanner.next().replace("\n", " ").replace("\r", "").trim().toLowerCase();
            // userCommand = userCommand.replace("\n", "").replace("\r", "");
            parseUserCommand(userCommand);
        }
        System.out.println("Exiting...");


    }

    /** ***********************************************************************
     *  Static method definitions
     */

    /**
     *  Display the splash screen
     */
    public static void splashScreen() {
        System.out.println(line("-",80));
        System.out.println("Welcome to mpBaseLite"); // Display the string.
        System.out.println("stormBaseLite Version " + getVersion());
        System.out.println(getCopyright());
        System.out.println("\nType \"help;\" to display supported commands.");
        System.out.println(line("-",80));
    }

    /**
     * @param s The String to be repeated
     * @param num The number of time to repeat String s.
     * @return String A String object, which is the String s appended to itself num times.
     */
    public static String line(String s,int num) {
        String a = "";
        for(int i=0;i<num;i++) {
            a += s;
        }
        return a;
    }

    public static void printCmd(String s) {
        System.out.println("\n\t" + s + "\n");
    }
    public static void printDef(String s) {
        System.out.println("\t\t" + s);
    }

    /**
     *  Help: Display supported commands
     */
    public static void help() {
        out.println(line("*",80));
        out.println("SUPPORTED COMMANDS\n");
        out.println("All commands below are case insensitive\n");
        out.println("SHOW TABLES;");
        out.println("\tDisplay the names of all tables.\n");
        //printCmd("SELECT * FROM <table_name>;");
        //printDef("Display all records in the table <table_name>.");
        out.println("SELECT <column_list> FROM <table_name> [WHERE <condition>];");
        out.println("\tDisplay table records whose optional <condition>");
        out.println("\tis <column_name> = <value>.\n");
        out.println("DROP TABLE <table_name>;");
        out.println("\tRemove table data (i.e. all records) and its schema.\n");
        out.println("UPDATE TABLE <table_name> SET <column_name> = <value> [WHERE <condition>];");
        out.println("\tModify records data whose optional <condition> is\n");
        out.println("VERSION;");
        out.println("\tDisplay the program version.\n");
        out.println("HELP;");
        out.println("\tDisplay this help information.\n");
        out.println("EXIT;");
        out.println("\tExit the program.\n");
        out.println(line("*",80));
    }

    /** return the DavisBase version */
    public static String getVersion() {
        return version;
    }

    public static String getCopyright() {
        return copyright;
    }

    public static void displayVersion() {
        System.out.println("stormBaseLite Version " + getVersion());
        System.out.println(getCopyright());
    }

    public static void parseUserCommand (String userCommand) {

        /* commandTokens is an array of Strings that contains one token per array element
         * The first token can be used to determine the type of command
         * The other tokens can be used to pass relevant parameters to each command-specific
         * method inside each case statement */
        String[] commandTokens = userCommand.split(" ");
        /*
         *  This switch handles a very small list of hardcoded commands of known syntax.
         *  You will want to rewrite this method to interpret more complex commands.
         */
        switch (commandTokens[0]) {
            case "show":
                System.out.println("CASE: SHOW");
                if(commandTokens.length==2){
                    if (commandTokens[1].equals("databases")) {
                        parseShowDatabase();
                    }
                    else if(commandTokens[1].equals("tables")){
                        parseShowTable();
                    }
                }
                else
                    System.out.println("Wrong Command");
                break;
            case "create":
                System.out.println("CASE: CREATE");
                if (commandTokens.length==3 && commandTokens[1].equals("database")){
                    parseCreateDatabase(commandTokens[2]);
                }
                else if(commandTokens[1].equals("table")){
                    if(currentDB.isEmpty())
                        System.out.println("Database is not specified");
                    else
                        parseCreateTable(userCommand);
                }
                break;

            case "drop":
                System.out.println("CASE: DROP");
                if (commandTokens.length==3 && commandTokens[1].equals("database")) {
                    dropDatabase(commandTokens[2]);
                }
                else if(commandTokens.length==3 && commandTokens[1].equals("table")) {
                    dropTable(commandTokens[2]);
                }
                break;
            case "use":
                System.out.println("CASE: USE");
                if(currentDB.isEmpty())
                    useDatabase(commandTokens[1]);
                break;
            case "insert":
                System.out.println("CASE: INSERT");
                insertQuery(userCommand);
                break;
            case "delete":
                System.out.println("CASE: DELETE");
                deleteQuery(userCommand);
                break;
            case "update":
                System.out.println("CASE: UPDATE");
                parseUpdate(userCommand);
                break;
            case "select":
                System.out.println("CASE: SELECT");
                parseQuery(userCommand);
                break;
            case "help":
                help();
                break;
            case "version":
                displayVersion();
                break;
            case "exit":
                isExit = true;
                break;
            case "quit":
                isExit = true;
            default:
                System.out.println("I didn't understand the command: \"" + userCommand + "\"");
                break;
        }
    }
    public static void defaultCreateSteps(RandomAccessFile file){
        try{
            file.setLength(pageSize);
            file.seek(0);
            file.writeByte(13);
            file.writeByte(0);
            file.writeShort(511);
            file.writeInt(-1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     *  Stub method for creating database
     *  @ param createDBString is a String of the user input
     */
    public static void parseCreateDatabase(String dbName) {
//		System.out.println("STUB: This is the createDatabase method.");
//		System.out.println("\tParsing the string:\"" + createDBString + "\"");
        try {
            File dbFile = new File(dbPath+"\\" +dbName);
            boolean dbExist=dbFile.exists();
            if (!dbExist) {
                Boolean dbSuccessFlag = dbFile.mkdir();
                if (!dbSuccessFlag) {
                    System.out.println("database "+dbName+" has not been created");
                }
                else {
                    String catalogPath=dbPath+"\\" +dbName+"\\catalog" ;
                    File metadataFile = new File(catalogPath);
                    File dataFile = new File(dbPath+ "\\" +dbName+"\\user_data");
                    Boolean mdSuccessFlag = metadataFile.mkdir();
                    Boolean dataSuccessFlag = dataFile.mkdir();
                    if (mdSuccessFlag && dataSuccessFlag) {
                        try {
                            RandomAccessFile davisbaseTablesCatalog = new RandomAccessFile(catalogPath+"\\davisbase_tables.tbl", "rw");
                            defaultCreateSteps(davisbaseTablesCatalog);
                            davisbaseTablesCatalog.close();
                        }
                        catch (Exception e){
                            out.println("Unable to create the database_tables file");
                            out.println(e);
                        }
                        try {
                            RandomAccessFile davisbaseTablesCatalog = new RandomAccessFile(catalogPath+"\\davisbase_columns.tbl", "rw");
                            defaultCreateSteps(davisbaseTablesCatalog);
                            davisbaseTablesCatalog.close();
                        }
                        catch (Exception e){
                            out.println("Unable to create the database_tables file");
                            out.println(e);
                        }
                        System.out.println("database "+dbName+" is successfully created.");
                        useDatabase(dbName);
                    }
                    else {
                        System.out.println("metadata or data is not created");
                    }
                }
            }
            else {
                System.out.println("Can't create database "+dbName+"; database already exists");
            }
        }
        catch(Exception e) {
            System.out.println("Exception " + e);
        }
    }


    /**
     *  Stub method for creating database
     *  @ param createDBString is a String of the user input
     */
    public static void parseShowDatabase() {
        //System.out.println("STUB: This is the parseShowDatabase method.");
        //  System.out.println("\tParsing the string:\"" + createDBString + "\"");
        File file = new File(dbPath);
       // System.out.println(dbPath);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        //System.out.println(Arrays.toString(directories));
        if(directories.length>0) {
            System.out.println("Databases:");
            for (int i = 0; i < directories.length; i++) {
                System.out.println(directories[i]);
            }
        }
    }
    /**
     *  Stub method for dropping database
     *  @ param dropDBString is a String of the user input
     *
     */
    public static boolean deleteRecursive(File path) {

        boolean successFlag = true;
        try{
            if (path.isDirectory()){
                for (File f : path.listFiles()){
                    successFlag = successFlag && deleteRecursive(f);
                }
            }}
        catch (Exception e){
            e.printStackTrace();
        }
        return successFlag && path.delete();
    }

    public static void dropDatabase(String dbString)  {
//            System.out.println("STUB: This is the dropDatabase method.");
//            System.out.println("\tParsing the string:\"" + dropDBString + "\"");
        File dbDeletePath=new File(dbPath+"\\"+dbString);
        //System.out.println(dbDeletePath);
        try{
            if (!dbDeletePath.exists()){
                System.out.println("Database not found");
                //throw new FileNotFoundException(dbDeletePath.getAbsolutePath());
            }
            else{
                boolean flag=deleteRecursive(dbDeletePath);
                if(flag){
                    System.out.println("Database deleted");
                    currentDB="";
                    dbCatalogPath="";
                    dbUserDataPath="";
                }
            }}
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     *  Stub method for dropping database
     *  @ param useDBString is a String of the user input
     */
    public static void useDatabase(String dbString) {
//            System.out.println("STUB: This is the useDatabase method.");
//            System.out.println("\tParsing the string:\"" + useDBString + "\"");

        currentDB=dbPath+"\\"+dbString;
        if(!new File(currentDB).isDirectory()){
            currentDB="";
            return;
        }

        dbCatalogPath=currentDB+"\\catalog";
        dbUserDataPath=currentDB+"\\user_data";
        if(!new File(currentDB).isDirectory()) {
            currentDB = "";
            dbCatalogPath="";
            dbUserDataPath="";
            System.out.println("Database does not exist");
        }
        else{
            System.out.println("Database has been changed to "+dbString);
        }
        //System.out.println(currentDB);
    }
    /**
     *  Stub method for creating new tables
     *  @ param queryString is a String of the user input
     */
    public static Boolean checkDataType(String dataTypeName) {
        try {
            switch (dataTypeName) {
                case "int":
                case "text":
                case "double":
                case "date":
                case "smallint":
                case "tinyint":
                case "bigint":
                case "null":
                case "datetime":
                case "real":
                    return true;
                default:
                    throw new Exception("Invalid DataType!!!!!!!!!!!");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }
    public static int recordSieze(String dataTypeName){
        int recordSize=0;
        boolean isDataType = checkDataType(dataTypeName);
        if (isDataType == false) {
            System.out.println("Incorrect syntax");
            return -1;
        } else {

            if (dataTypeName.equalsIgnoreCase("int")) {
                recordSize = recordSize + 4;
            } else if (dataTypeName.equalsIgnoreCase("tinyint")) {
                recordSize = recordSize + 1;
            } else if (dataTypeName.equalsIgnoreCase("bigint")) {
                recordSize = recordSize + 8;
            } else if (dataTypeName.equalsIgnoreCase("smallint")) {
                recordSize = recordSize + 2;
            } else if (dataTypeName.equalsIgnoreCase("real")) {
                recordSize = recordSize + 4;
            } else if (dataTypeName.equalsIgnoreCase("double")) {
                recordSize = recordSize + 8;
            } else if (dataTypeName.equalsIgnoreCase("datetime")) {
                recordSize = recordSize + 8;
            } else if (dataTypeName.equalsIgnoreCase("date")) {
                recordSize = recordSize + 8;
            }
        }
        return recordSize;
    }
    public static int serialCode(String dataTypeName){
        boolean datatypetrue;
        int serialCode=0;
        datatypetrue = checkDataType(dataTypeName);
        if (datatypetrue == false) {
            return -1;
        }
        //System.out.println(datatypetrue);
        String data_type = dataTypeName;
        if (data_type.equalsIgnoreCase("int")) {
            serialCode = 0x06;
        } else if (data_type.equalsIgnoreCase("tinyint")) {
            serialCode = 0x04;
        } else if (data_type.equalsIgnoreCase("smallint")) {
            serialCode = 0x05;
        } else if (data_type.equalsIgnoreCase("bigint")) {
            serialCode = 0x07;
        } else if (data_type.equalsIgnoreCase("real")) {
            serialCode = 0x08;
        } else if (data_type.equalsIgnoreCase("double")) {
            serialCode = 0x09;
        } else if (data_type.equalsIgnoreCase("datetime")) {
            serialCode = 0x0A;
        } else if (data_type.equalsIgnoreCase("date")) {
            serialCode = 0x0B;
        } else if (data_type.equalsIgnoreCase("text")) {
            serialCode = 0x0C;
        }
        return serialCode;
    }
    public static void parseCreateTable(String createTableString) {

//            System.out.println("STUB: Calling your method to create a table");
//            System.out.println("Parsing the string:\"" + createTableString + "\"");
        String[] createTableTokens = createTableString.split(" ");

        /* Define table file name */
        String tableName, columnList;
        String tempColumnData=createTableTokens[2];
        if(tempColumnData.contains("(")){
            tableName=tempColumnData.substring(0,tempColumnData.indexOf('('));
        }
        else{
            tableName = createTableTokens[2];
        }
        // System.out.println("Table: "+tableName);
        columnList=createTableString.substring(createTableString.indexOf('(')+1,createTableString.indexOf(')'));
        //System.out.println(columnList);
        /*  Code to create a .tbl file to contain table data */
        String tableFileName=currentDB+"\\user_data\\"+tableName+".tbl";
        if(new File(tableFileName).exists()){
            System.out.println("Table is already created");
            return;
        }

        ArrayList<String> dataTypes;
        ArrayList<Object> values;
        String tablePath=dbCatalogPath+"\\davisbase_tables.tbl";
        dataTypes=new ArrayList<>();
        values=new ArrayList<>();
        dataTypes.add("text");
        values.add(tableName);
        insertValues(tablePath,dataTypes,values);

        String columnTablePath;
        columnTablePath=dbCatalogPath+ "\\davisbase_columns.tbl";
        //RandomAccessFile catalogColumnFile = new RandomAccessFile(columnTablePath, "rw");
        String[] columns=columnList.split(",");
        String columnName,dataType;

        for(int i=0;i<columns.length;i++) {
            columnName = (columns[i].trim()).split(" ")[0];
            dataType = (columns[i].trim()).split(" ")[1];
            //4 for rowid 1 for position 1 for null
            dataTypes=new ArrayList<>();
            values=new ArrayList<>();
            dataTypes.add("text");
            dataTypes.add("text");
            dataTypes.add("text");
            dataTypes.add("tinyint");
            dataTypes.add("tinyint");
            values.add(tableName);
            values.add(columnName);
            values.add(dataType);
            values.add(i);
            values.add(0);
            //System.out.println("Datatypes in create column: "+values);
            insertValues(columnTablePath, dataTypes,values);
        }
        try {
            RandomAccessFile tableFile = new RandomAccessFile(tableFileName, "rw");
            defaultCreateSteps(tableFile);
            tableFile.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        /*  Code to insert a row in the davisbase_tables table
         *  i.e. database catalog meta-data
         */

        /*  Code to insert rows in the davisbase_columns table
         *  for each column in the new table
         *  i.e. database catalog meta-data
         */
    }
    /**
     *  Stub method for displaying table
     *  @ param showTableStr is a String of the user input
     */
    public static void parseShowTable() {
        //System.out.println("STUB: This is the showTable method.");
        //System.out.println("\tParsing the string:\"" + showTableStr + "\"");
        File folder = new File(currentDB+"\\user_data");
        //System.out.println(folder);
        File[] listOfFiles = folder.listFiles();
        // System.out.println(listOfFiles.length);
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            }
        }
    }

    /**
     *  Stub method for dropping tables
     *  @ param dropTableString is a String of the user input
     */
    public static void dropTable(String dropTableName) {
//        System.out.println("STUB: This is the dropTable method.");
//        System.out.println("\tParsing the string:\"" + dropTableString + "\"");
       // System.out.println("Table:  "+dropTableName);
        String tableFileName=currentDB+"\\user_data\\"+dropTableName+".tbl";
        if(!new File(tableFileName).exists()){
            System.out.println("Table does not exist");
            return;
        }
        try{
            RandomAccessFile tableFile=new RandomAccessFile(dbCatalogPath+"\\davisbase_tables.tbl","rw");
            int pageNumbers = ((int) tableFile.length()) / 512;
            //System.out.println(pageNumbers);
            String tempTableName;
            int numberOfRecords, startIndex, nextRecordLocation, numberOfData;
            for (int i = 0; i < pageNumbers; i++) {
                startIndex = i * 512;
                tableFile.seek(startIndex);
                tableFile.readByte();
                numberOfRecords = tableFile.readByte();
                tableFile.readShort();
                tableFile.readInt();
                int currentTopLocation = (int) tableFile.getFilePointer();
                int tableNameLength=0;
                //System.out.println(numberOfRecords);
                for (int j = 0; j < numberOfRecords; j++) {
                    tableFile.seek(currentTopLocation + j * 2);
                    //System.out.println(columnFile.getFilePointer());
                    nextRecordLocation = (int) tableFile.readShort();
                    if(nextRecordLocation==-1)
                        continue;
                    // System.out.println(nextRecordLocation);
                    tableFile.seek(nextRecordLocation);
                    tableFile.readByte();//Number of data
                    tableFile.readByte();//serail code for id
                    tableNameLength=(int)tableFile.readByte()-12;
                    //System.out.println(tableNameLength);
                    tableFile.readInt();
                    char readTable[]=new char[tableNameLength];
                    for(int ch=0;ch<tableNameLength;ch++)
                        readTable[ch]=(char)tableFile.readByte();
                    tempTableName=String.valueOf(readTable);
                    //System.out.println("Temp:   "+tempTableName);
                    if(dropTableName.trim().equalsIgnoreCase(tempTableName)){
                       // System.out.print("in");
                        tableFile.seek(currentTopLocation+j*2);
                        tableFile.writeShort(-1);
                        File file = new File(dbUserDataPath + "\\" + dropTableName + ".tbl", "r");
                        file.delete();
                    }
                }
            }
            tableFile.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  Stub method for executing queries
     *  @ param queryString is a String of the user input
     */
    public static ArrayList<List> giveColums(String tableName) {
        ArrayList<List> result=new ArrayList<>();
        try {
            RandomAccessFile columnFile = new RandomAccessFile(dbCatalogPath + "\\davisbase_columns.tbl", "rw");
            // System.out.println(columnFile.length());
            int pageNumbers = ((int) columnFile.length()) / 512;
            //System.out.println(pageNumbers);
            int numberOfRecords, startIndex, nextRecordLocation, numberOfData;
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> columnDataTypes = new ArrayList<>();
            ArrayList<Integer> columnPositions = new ArrayList<>();
            ArrayList<Integer> nullValues = new ArrayList<>();
            for (int i = 0; i < pageNumbers; i++) {
                startIndex = i * 512;
                columnFile.seek(startIndex);
                columnFile.readByte();
                numberOfRecords = columnFile.readByte();
                columnFile.readShort();
                columnFile.readInt();
                int currentTopLocation = (int) columnFile.getFilePointer();
                //System.out.println(numberOfRecords);
                for (int j = 0; j < numberOfRecords; j++) {
                    columnFile.seek(currentTopLocation + j * 2);
                    //System.out.println(columnFile.getFilePointer());
                    nextRecordLocation = (int) columnFile.readShort();
                   // System.out.println(nextRecordLocation);
                    columnFile.seek(nextRecordLocation);
                    numberOfData = columnFile.readByte();
                    byte serialCodes[] = new byte[numberOfData];
                    columnFile.read(serialCodes);
                    columnFile.readInt();
                    byte tableNameByte[] = new byte[serialCodes[1] - 12];
                    columnFile.read(tableNameByte);
                    if (tableName.equalsIgnoreCase(new String(tableNameByte))) {
                        byte columnNameByte[] = new byte[serialCodes[2] - 12];
                        columnFile.read(columnNameByte);
                        columnNames.add(new String(columnNameByte));
                        byte datatypeByte[] = new byte[serialCodes[3] - 12];
                        columnFile.read(datatypeByte);
                        columnDataTypes.add(new String(datatypeByte));
                        columnPositions.add((int) columnFile.readByte());
                        nullValues.add((int) columnFile.readByte());
                    }
                }
            }
            result.add(columnNames);
            result.add(columnDataTypes);
            result.add(columnPositions);
            result.add(nullValues);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public static void parseQuery(String queryString) {
//        System.out.println("STUB: This is the parseQuery method");
//        System.out.println("\tParsing the string:\"" + queryString + "\"");
        String fromTokens[]=queryString.split("from");
        String beforeFromTokens[]=fromTokens[0].trim().split(" ");
        String afterFromTokens[]=fromTokens[1].trim().split(" ");
        ArrayList<List> answer = giveColums(afterFromTokens[0]);
        List<String> columnNames=answer.get(0);
        List<String> columnDataTypes=answer.get(1);
        List<Integer> columnPositions=answer.get(2);
        String tableFileName=currentDB+"\\user_data\\"+afterFromTokens[0]+".tbl";
        if(!new File(tableFileName).exists()){
            System.out.println("Table does not exist");
            return;
        }
            try {
                RandomAccessFile columnFile = new RandomAccessFile(dbUserDataPath + "\\" + afterFromTokens[0] + ".tbl", "rw");
                int pageNumbers = ((int) columnFile.length()) / 512;
                int numberOfRecords, startIndex, nextRecordLocation, numberOfData;
                //    System.out.println(columnDataTypes);
                for (int i = 0; i < pageNumbers; i++) {
                    startIndex = i * 512;
                    columnFile.seek(startIndex);
                    columnFile.readByte();
                    numberOfRecords = columnFile.readByte();
                    columnFile.readShort();
                    columnFile.readInt();
                    int currentTopLocation = (int) columnFile.getFilePointer();
                    String neededColumnName,neededColumnValue,neededColumnDataType;
                    neededColumnDataType="";
                    neededColumnValue="";
                    int neededColumnIndex=-1;
                    boolean foundRow;
                    boolean whereExist=fromTokens[1].contains("where");
                    boolean allSelect=beforeFromTokens[1].trim().equalsIgnoreCase("*");
                    if(whereExist){
                        afterFromTokens=fromTokens[1].trim().split("where");
                        neededColumnName=afterFromTokens[1].trim().split("=")[0];
                        neededColumnValue=afterFromTokens[1].trim().split("=")[1];
                        neededColumnDataType=columnDataTypes.get(columnNames.indexOf(neededColumnName));
                        neededColumnIndex=columnNames.indexOf(neededColumnName);
                    }
                    if(allSelect){
                        for(int col=0;col<columnNames.size();col++)
                            System.out.print(columnNames.get(col)+"\t");
                        System.out.println();
                        System.out.println("------------------------------------------");
                    }
                    else {
                        String columnList[]=fromTokens[0].trim().substring(fromTokens[0].indexOf("select")+7,fromTokens[0].length()-1).trim().split(",");
                        // System.out.println("Size:   "+columnList[0]);
                        for(int mainIn=0;mainIn<columnNames.size();mainIn++){
                            for(int col=0;col<columnList.length;col++)
                                if(columnNames.get(mainIn).equalsIgnoreCase(columnList[col]))
                                    System.out.print(columnList[col]+"\t");
                        }
                        System.out.println();
                        System.out.println("------------------------------------------");
                    }
                    //System.out.println(numberOfRecords);
                    for (int j = 0; j < numberOfRecords; j++) {
                        foundRow=false;
                        columnFile.seek(currentTopLocation + j * 2);
                        nextRecordLocation = (int) columnFile.readShort();
                        columnFile.seek(nextRecordLocation);
                        numberOfData = columnFile.readByte();
                        columnFile.readByte();
                        byte serialCodes[] = new byte[numberOfData-1];
                        columnFile.read(serialCodes);
                        columnFile.readInt();
                        int recordLocation=(int)columnFile.getFilePointer();
                        int totalIndex=0;
                        if(whereExist){
                            for(int colWhere=0;colWhere<neededColumnIndex;colWhere++){
                                if((int)serialCodes[colWhere]>12)
                                    totalIndex+=(int)serialCodes[colWhere]-12;
                                else
                                    totalIndex+=(int)serialCodes[colWhere];
                            }
                            byte readData[]=new byte[totalIndex];
                            columnFile.read(readData);
                            if (neededColumnDataType.trim().equalsIgnoreCase("tinyint")) {
                                //System.out.print((int) columnFile.readByte() + "\t");
                                if(((int)columnFile.readByte())==(Integer.parseInt(neededColumnValue)))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("smallint")) {
                                if(((int)columnFile.readByte())==(Integer.parseInt(neededColumnValue)))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("int")) {
                                if((columnFile.readInt())==(Integer.parseInt(neededColumnValue)))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("bigint")) {
                                if((columnFile.readDouble())==(Double.parseDouble(neededColumnValue)))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("real")) {
                                if((columnFile.readInt())==(Integer.parseInt(neededColumnValue)))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("double")) {
                                if((columnFile.readDouble())==(Double.parseDouble(neededColumnValue)))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("datetime")) {
                                byte tempVal[] = new byte[8];
                                columnFile.read(tempVal);
                                if(tempVal.toString().equalsIgnoreCase(neededColumnValue))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("date")) {
                                byte tempVal[] = new byte[8];
                                columnFile.read(tempVal);
                                if(tempVal.toString().equalsIgnoreCase(neededColumnValue))
                                    foundRow=true;
                            } else if (neededColumnDataType.trim().equalsIgnoreCase("text")) {
                                int len = (int) serialCodes[neededColumnIndex] - 12;
                                char chAr[] = new char[len];
                                for (int l = 0; l < len; l++) {
                                    chAr[l] = (char) columnFile.readByte();
                                }
                                if(String.valueOf(chAr).equalsIgnoreCase(neededColumnValue))
                                    foundRow=true;
                            }
                            if(!foundRow)
                                continue;
                        }
                        columnFile.seek(recordLocation);
                        if (allSelect) {

                            for (int k = 0; k < numberOfData-1; k++) {
                               // System.out.println("serailcode"+k+":    "+serialCodes[k]);
                                //System.out.println("Datatype:"+columnDataTypes.get(k));
                                if (columnDataTypes.get(k ).trim().equalsIgnoreCase("tinyint")) {
                                    System.out.print((int) columnFile.readByte() + "\t");
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("smallint")) {
                                    System.out.print((int) columnFile.readByte() + "\t");
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("int")) {
                                    System.out.print((int) columnFile.readInt() + "\t");
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("bigint")) {
                                    System.out.print(columnFile.readDouble() + "\t");
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("real")) {
                                    System.out.print(columnFile.readInt() + "\t");
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("double")) {
                                    System.out.print(columnFile.readDouble() + "\t");
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("datetime")) {
                                    byte tempVal[] = new byte[8];
                                    columnFile.read(tempVal);
                                    System.out.print(tempVal.toString() + "\t");
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("date")) {
                                    byte tempVal[] = new byte[8];
                                    columnFile.read(tempVal);
                                    System.out.print(tempVal.toString() + "\t");
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("text")) {
                                    int len = (int) serialCodes[k] - 12;
                                    char chAr[] = new char[len];
                                    for (int l = 0; l < len; l++) {
                                        chAr[l] = (char) columnFile.readByte();
                                    }
                                    System.out.print(String.valueOf(chAr) + "\t");
                                }
                            }
                            System.out.println();
                        }
                        else {
                            String columnList[]=fromTokens[0].trim().substring(fromTokens[0].indexOf("select")+7,fromTokens[0].length()-1).trim().split(",");
                            boolean exist[]=new boolean[numberOfData-1];
                            for (int k = 0; k < numberOfData-1; k++) {
                                Arrays.fill(exist,false);
                                // System.out.println("serailcode"+k+":    "+serialCodes[k]);
                                //System.out.println("Datatype:"+columnDataTypes.get(k));
                                for(int c=0;c<columnList.length;c++){
                                    if(columnList[c].trim().equalsIgnoreCase(columnNames.get(k))){
                                        exist[columnPositions.get(columnNames.indexOf(columnList[c].trim()))]=true;
                                    }
                                }
                                //if(!exist) continue;
                                if (columnDataTypes.get(k).trim().equalsIgnoreCase("tinyint")) {
                                    if(exist[k])
                                        System.out.print((int) columnFile.readByte() + "\t");
                                    else
                                        columnFile.readByte();
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("smallint")) {
                                    if(exist[k])
                                        System.out.print((int) columnFile.readByte() + "\t");
                                    else
                                        columnFile.readByte();
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("int")) {
                                    if(exist[k])
                                        System.out.print(columnFile.readInt() + "\t");
                                    else
                                        columnFile.readInt();
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("bigint")) {
                                    if(exist[k])
                                        System.out.print(columnFile.readDouble() + "\t");
                                    else
                                        columnFile.readDouble();
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("real")) {
                                    if(exist[k])
                                        System.out.print(columnFile.readInt() + "\t");
                                    else
                                        columnFile.readInt();
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("double")) {
                                    if(exist[k])
                                        System.out.print(columnFile.readDouble() + "\t");
                                    else
                                        columnFile.readDouble();
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("datetime")) {
                                    byte tempVal[] = new byte[8];
                                    columnFile.read(tempVal);
                                    if(exist[k])
                                        System.out.print(tempVal.toString() + "\t");
                                } else if (columnDataTypes.get(k ).trim().equalsIgnoreCase("date")) {
                                    byte tempVal[] = new byte[8];
                                    columnFile.read(tempVal);
                                    if(exist[k])
                                        System.out.print(tempVal.toString() + "\t");
                                } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("text")) {
                                    int len = (int) serialCodes[k] - 12;
                                    char chAr[] = new char[len];
                                    for (int l = 0; l < len; l++) {
                                        chAr[l] = (char) columnFile.readByte();
                                    }
                                    if(exist[k])
                                        System.out.print(String.valueOf(chAr) + "\t");
                                }
                            }
                            System.out.println();
                        }
                    }
                }
            }
            catch(Exception e){
                    e.printStackTrace();
                }

    }

    /**
     *  Stub method for updating records
     *  @ param updateString is a String of the user input
     */
    public static void insertValues(String filePath,ArrayList<String> columnDataTypes,ArrayList<Object> values){
        try {
            RandomAccessFile tableFile = new RandomAccessFile(filePath, "rw");
            int startIndex;
            int pageNumbers;
            int numberRecord, startPointer, rowId, recordSize;
            //4 for rowid 1 for position 1 for null
            pageNumbers = ((int) tableFile.length()) / 512;
            startIndex = 512 * (pageNumbers - 1);
            //System.out.println(tableFile.length()+"Strart Index:   "+pageNumbers);
            tableFile.seek(startIndex);
            tableFile.readByte();
            numberRecord = (int)tableFile.readByte();
            startPointer = tableFile.readShort();
            tableFile.readInt();
            for (int j = 1; j <= numberRecord; j++)
                tableFile.readShort();
            rowId = numberRecord + 1;
            //7- 1 for number of records and 6 for column attributes and 4 for rowid int value and 1 for its datatype entry
            recordSize=1+values.size()+4+1;
           // System.out.println("DataType in method:"+columnDataTypes);
            //System.out.println("Values: "+values);
            for(int i=0;i<values.size();i++){
               // System.out.println("Value"+i);

                if(columnDataTypes.get(i).equalsIgnoreCase("text")){
                    //System.out.println("Value Length:   "+((String)values.get(i)).length());
                    recordSize+=((String)values.get(i)).length();
                }
                else{
                   // System.out.println("Value"+i);
                    recordSize+=recordSieze(columnDataTypes.get(i));
                }
            }
            //recordSize = 4 +dataType.length()+columnName.length()+2+ tableName.length()+7;
            //2 for adding address for that entry
            //System.out.println("In file"+tableFile.getFilePointer());
            if ((startPointer - tableFile.getFilePointer()) >= (recordSize + 2)) {
                tableFile.seek(startPointer - recordSize);
                //tableFile.writeInt(rowId);
                tableFile.writeByte(values.size()+1);
                tableFile.writeByte(serialCode("int"));

                for(int i=0;i<values.size();i++){
                    if(columnDataTypes.get(i).equalsIgnoreCase("text")){
                       // System.out.println("inp"+((String)values.get(i)).trim());
                        tableFile.writeByte(((String)values.get(i)).length()+serialCode("text"));
                    }
                    else{
                        tableFile.writeByte(serialCode(columnDataTypes.get(i)));
                    }
                }
                tableFile.writeInt(rowId);
                for(int i=0;i<values.size();i++){
                    if(columnDataTypes.get(i).equalsIgnoreCase("tinyint")){
                        tableFile.writeByte((int)values.get(i));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("smallint")){
                        tableFile.writeByte(Integer.parseInt((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("int")){
                        tableFile.writeInt((int)values.get(i));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("bigint")){
                        tableFile.writeDouble(Double.parseDouble((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("real")){
                        tableFile.write(ByteBuffer.allocate(4).putInt(Integer.parseInt((String)values.get(i))).array());
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("double")){
                        tableFile.writeDouble(Double.parseDouble((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("datetime")){
                        tableFile.write(ByteBuffer.allocate(8).putDouble(Double.parseDouble((String)values.get(i))).array());
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("date")){
                        tableFile.write(ByteBuffer.allocate(8).putDouble(Double.parseDouble((String)values.get(i))).array());
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("text")){
                        tableFile.writeBytes(((String)values.get(i)));
                    }
                }
                tableFile.seek(startIndex + 1);
                tableFile.writeByte(rowId);
                tableFile.writeShort(startPointer - recordSize);
                tableFile.readInt();
                for (int j = 1; j <= numberRecord; j++)
                    tableFile.readShort();
                tableFile.writeShort(startPointer - recordSize);

                // System.out.println(catalogTableFile.getFilePointer());
            } else {
                pageNumbers++;
                //System.out.println("Pagenumber: "+pageNumbers);
                tableFile.setLength(pageNumbers * 512);
                //  System.out.println("Total Length: "+catalogTableFile.length());
                tableFile.seek(startIndex + 4);
                //  System.out.println("Strart Index loaction for ahead file:   "+catalogTableFile.getFilePointer());
                //catalogTableFile.readInt();
                tableFile.writeInt(startIndex + 512);
                startIndex=(pageNumbers - 1) * 512;
                startPointer=pageNumbers*512-1;
                tableFile.seek(startIndex);
                //  System.out.println("New Index:  "+catalogTableFile.getFilePointer());
                tableFile.writeByte(13);
                tableFile.writeByte(0);
                tableFile.writeShort(pageNumbers * 512 - 1);
                tableFile.writeInt(-1);
                tableFile.seek(startPointer-recordSize);

                tableFile.writeByte(values.size()+1);
                tableFile.writeInt(serialCode("int"));
                for(int i=0;i<values.size();i++){
                    if(columnDataTypes.get(i).equalsIgnoreCase("text")){
                        tableFile.writeByte(((String)values.get(i)).length()+serialCode("text"));
                    }
                    else{
                        tableFile.writeByte(serialCode(columnDataTypes.get(i)));
                    }
                }
                tableFile.writeInt(rowId);
                for(int i=0;i<values.size();i++){
                    if(columnDataTypes.get(i).equalsIgnoreCase("tinyint")){
                        tableFile.writeByte(Integer.parseInt((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("smallint")){
                        tableFile.writeByte(Integer.parseInt((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("int")){
                        tableFile.writeInt(Integer.parseInt((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("bigint")){
                        tableFile.writeDouble(Double.parseDouble((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("real")){
                        tableFile.write(ByteBuffer.allocate(4).putInt(Integer.parseInt((String)values.get(i))).array());
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("double")){
                        tableFile.writeDouble(Double.parseDouble((String)values.get(i)));
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("datetime")){
                        tableFile.write(ByteBuffer.allocate(8).putDouble(Double.parseDouble((String)values.get(i))).array());
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("date")){
                        tableFile.write(ByteBuffer.allocate(8).putDouble(Double.parseDouble((String)values.get(i))).array());
                    }
                    else if(columnDataTypes.get(i).equalsIgnoreCase("text")){
                        tableFile.writeBytes(((String)values.get(i)).substring(1,((String)values.get(i)).length()-1));
                    }
                }
                tableFile.seek(startIndex + 1);
                tableFile.writeByte(1);
                tableFile.writeShort(startPointer - recordSize);
                tableFile.readInt();
                for (int j = 1; j <= numberRecord; j++)
                    tableFile.readShort();
                tableFile.writeShort(startPointer - recordSize);
            }
            tableFile.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseUpdate(String updateString) {
//        System.out.println("STUB: This is the dropTable method");
//        System.out.println("Parsing the string:\"" + updateString + "\"");
        String whereTokens[]=updateString.split("where");

        String tableName=whereTokens[0].split(" ")[1];
        String tableFileName=currentDB+"\\user_data\\"+tableName+".tbl";
        if(!new File(tableFileName).exists()){
            System.out.println("Table does not exist");
            return;
        }
        ArrayList<List> answer = giveColums(tableName);
        List<String> columnNames=answer.get(0);
        List<String> columnDataTypes=answer.get(1);
        List<Integer> columnPositions=answer.get(2);
        String neededColumnName,neededColumnValue,neededColumnDataType;
        int neededColumnIndex=-1;
        neededColumnName=whereTokens[1].substring(0,whereTokens[1].indexOf("=")).trim();
        neededColumnValue=whereTokens[1].substring(whereTokens[1].indexOf("=")+1,whereTokens[1].length()).trim();
        neededColumnDataType=columnDataTypes.get(columnNames.indexOf(neededColumnName));
        neededColumnIndex=columnPositions.get(columnNames.indexOf(neededColumnName));
        try {
            RandomAccessFile columnFile = new RandomAccessFile(dbUserDataPath + "\\" + tableName + ".tbl", "rw");
            int pageNumbers = ((int) columnFile.length()) / 512;
            int numberOfRecords, startIndex, nextRecordLocation, numberOfData;
            String columnChangeName = whereTokens[0].trim().substring(whereTokens[0].indexOf("set") + 3, whereTokens[0].length() - 1).trim().split("=")[0];
            String columnChangeValues = whereTokens[0].trim().substring(whereTokens[0].indexOf("set") + 3, whereTokens[0].length() - 1).trim().split("=")[1];
            //    System.out.println(columnDataTypes);
            for (int i = 0; i < pageNumbers; i++) {
                startIndex = i * 512;
                columnFile.seek(startIndex);
                columnFile.readByte();
                numberOfRecords = columnFile.readByte();
                columnFile.readShort();
                columnFile.readInt();
                int currentTopLocation = (int) columnFile.getFilePointer();

                boolean foundRow;
                for (int j = 0; j < numberOfRecords; j++) {
                    foundRow = false;
                    columnFile.seek(currentTopLocation + j * 2);
                    nextRecordLocation = (int) columnFile.readShort();
                    columnFile.seek(nextRecordLocation);
                    numberOfData = columnFile.readByte();
                    columnFile.readByte();
                    byte serialCodes[] = new byte[numberOfData - 1];
                    columnFile.read(serialCodes);
                    columnFile.readInt();
                    int recordLocation =0;
                    int totalIndex = 0;

                    for (int colWhere = 0; colWhere < neededColumnIndex; colWhere++) {
                        if ((int) serialCodes[colWhere] > 12)
                            totalIndex += (int) serialCodes[colWhere] - 12;
                        else
                            totalIndex += (int) serialCodes[colWhere];
                    }
                    recordLocation = (int) columnFile.getFilePointer();

                    byte readData[] = new byte[totalIndex];
                    columnFile.read(readData);
                    if (neededColumnDataType.trim().equalsIgnoreCase("tinyint")) {
                        //System.out.print((int) columnFile.readByte() + "\t");
                        if (((int) columnFile.readByte()) == (Integer.parseInt(neededColumnValue)))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("smallint")) {
                        if (((int) columnFile.readByte()) == (Integer.parseInt(neededColumnValue)))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("int")) {
                        if ((columnFile.readInt()) == (Integer.parseInt(neededColumnValue)))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("bigint")) {
                        if ((columnFile.readDouble()) == (Double.parseDouble(neededColumnValue)))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("real")) {
                        if ((columnFile.readInt()) == (Integer.parseInt(neededColumnValue)))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("double")) {
                        if ((columnFile.readDouble()) == (Double.parseDouble(neededColumnValue)))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("datetime")) {
                        byte tempVal[] = new byte[8];
                        columnFile.read(tempVal);
                        if (tempVal.toString().equalsIgnoreCase(neededColumnValue))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("date")) {
                        byte tempVal[] = new byte[8];
                        columnFile.read(tempVal);
                        if (tempVal.toString().equalsIgnoreCase(neededColumnValue))
                            foundRow = true;
                    } else if (neededColumnDataType.trim().equalsIgnoreCase("text")) {
                        int len = (int) serialCodes[neededColumnIndex] - 12;
                        char chAr[] = new char[len];
                        for (int l = 0; l < len; l++) {
                            chAr[l] = (char) columnFile.readByte();
                        }
                        if (String.valueOf(chAr).trim().equalsIgnoreCase(neededColumnValue))
                            foundRow = true;
                    }
                    if (!foundRow)
                        continue;

                    columnFile.seek(recordLocation);
                   // columnFile.readInt();
                    for (int k = 0; k < numberOfData - 1; k++) {
                        // System.out.println("serailcode"+k+":    "+serialCodes[k]);
                        //System.out.println("Datatype:"+columnDataTypes.get(k));
                        if (columnDataTypes.get(k).trim().equalsIgnoreCase("tinyint")) {
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.writeByte(Integer.parseInt(columnChangeValues));
                            else
                                columnFile.readByte();
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("smallint")) {
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.writeByte(Integer.parseInt(columnChangeValues));
                            else
                                columnFile.readByte();
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("int")) {
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.writeInt(Integer.parseInt(columnChangeValues));
                            else
                                columnFile.readInt();
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("bigint")) {
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.writeDouble(Double.parseDouble(columnChangeValues));
                            else
                                columnFile.readDouble();
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("real")) {
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.write(ByteBuffer.allocate(4).putDouble(Double.parseDouble(columnChangeValues)).array());
                            else
                                columnFile.readInt();
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("double")) {
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.writeDouble(Double.parseDouble(columnChangeValues));
                            else
                                columnFile.readDouble();
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("datetime")) {
                            byte tempVal[] = new byte[8];
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.write(ByteBuffer.allocate(8).putDouble(Double.parseDouble(columnChangeValues)).array());
                            else
                                columnFile.read(tempVal);
                            //System.out.print(tempVal.toString() + "\t");
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("date")) {
                            byte tempVal[] = new byte[8];
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName))
                                columnFile.write(ByteBuffer.allocate(8).putDouble(Double.parseDouble(columnChangeValues)).array());
                            else
                                columnFile.read(tempVal);
                            //System.out.print(tempVal.toString() + "\t");
                        } else if (columnDataTypes.get(k).trim().equalsIgnoreCase("text")) {
                            int len = (int) serialCodes[k] - 12;
                            //System.out.println(columnChangeValues.length());
                            if (columnNames.get(k).equalsIgnoreCase(columnChangeName)) {
                                String temp="";
                                if (columnChangeValues.length() > len) {
                                    columnChangeValues = columnChangeValues.substring(0, len);
                                    columnFile.writeBytes(columnChangeValues);
                                }
                                else {

                                    temp=columnChangeValues;
//                                    System.out.println(columnChangeValues.length());
//                                    System.out.println(len-columnChangeValues.length());
                                    for(int y=0;y<(len-columnChangeValues.length());y++)
                                        temp+=" ";
                                   // System.out.println(columnChangeValues.length());
                                    columnFile.writeBytes(temp);
                                }

                            } else {
                                char chAr[] = new char[len];
                                for (int l = 0; l < len; l++) {
                                    chAr[l] = (char) columnFile.readByte();
                                }
                            }
                            //System.out.print(String.valueOf(chAr) + "\t");
                        }
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    /**
     *  Stub method for inserting records
     *  @ param insertQuery is a String of the user input
     */
    public static void  insertQuery(String queryString) {
//        System.out.println("STUB: This is the insert method");
//        System.out.println("Parsing the string:\"" + queryString + "\"");

        String tableName=queryString.split(" ")[2];
        String tableFileName=currentDB+"\\user_data\\"+tableName+".tbl";
        if(!new File(tableFileName).exists()){
            System.out.println("Table does not exist");
            return;
        }
        ArrayList<List> answer = giveColums(tableName.trim());
        List<String> columnNames=answer.get(0);
        List<String> columnDataTypes=answer.get(1);
        List<Integer> columnPositions = answer.get(2);
        List<Integer> nullValues = answer.get(3);
        ArrayList<String> dataTypes;
        ArrayList<Object> valueArray;
        String valueSplit[]=queryString.split("values");
        String columnList[];
       // System.out.println("TableName:  "+tableName);
        String file = dbUserDataPath + "\\" + tableName + ".tbl";

        if(valueSplit[0].contains("(") && valueSplit[0].contains(")")){
            columnList=valueSplit[0].substring(valueSplit[0].indexOf("(")+1,valueSplit[0].indexOf(")")).split(",");
            for(int i=0;i<columnList.length;i++)
                columnList[i]=columnList[i].trim();
            String values[] = (valueSplit[1].substring(valueSplit[1].indexOf("(") + 1, valueSplit[1].indexOf(")"))).split(",");
            ArrayList<String> columnArrayList = new ArrayList<String>(Arrays.asList(columnList));
            dataTypes = new ArrayList<>();
            valueArray = new ArrayList<>();
           // System.out.println("ColumDataTypes: "+columnArrayList);
            // System.out.println("Values: "+values[2]);
            for (int i=0 ; i<columnNames.size() ; i++){
                if(columnArrayList.contains(columnNames.get(i))){
                    dataTypes.add(columnDataTypes.get(i));
                    //System.out.println("ColumnName: "+columnNames.get(i));
                    if (columnDataTypes.get(i).equalsIgnoreCase("text"))
                        values[columnArrayList.indexOf(columnNames.get(i))] = (values[columnArrayList.indexOf(columnNames.get(i))]).substring(1,values[columnArrayList.indexOf(columnNames.get(i))] .length() - 1);
                    valueArray.add(values[columnArrayList.indexOf(columnNames.get(i))]);
                }
                else{
                    if(nullValues.get(i)==1){
                        System.out.println(columnNames.get(i)+" can't be null");
                        return;
                    }
                    else{
                        dataTypes.add(columnDataTypes.get(i));
                        if(columnDataTypes.get(i).equalsIgnoreCase("text"))
                            valueArray.add("null");
                        else
                            valueArray.add(-1);
                    }
                }
            }
            //System.out.println("Datatypes:"+dataTypes);
           // System.out.println("Values: "+valueArray);
        }
        else {
            String values[] = (valueSplit[1].substring(valueSplit[1].indexOf("(") + 1, valueSplit[1].indexOf(")"))).split(",");

            dataTypes = new ArrayList<>();
            valueArray = new ArrayList<>();
            //LinkedHashMap<String,Object> valMap=new LinkedHashMap<>();
            //System.out.println("ColumDataTypes: "+columnDataTypes);
           // System.out.println("Values: "+values[2]);
            for (int i = 0; i < columnDataTypes.size(); i++) {
                if (columnDataTypes.get(i).equalsIgnoreCase("text"))
                    values[i] = values[i].substring(1, values[i].length() - 1);
                valueArray.add(values[i]);
                dataTypes.add(columnDataTypes.get(i));
                //valMap.put(columnDataTypes.get(i),values[i]);
            }
        }
        insertValues(file, dataTypes, valueArray);

    }
    /**
     *  Stub method for deleting records
     *  @param queryString is a String of the user input
     */
    public static void deleteQuery(String queryString) {
        System.out.println("STUB: This is the delete method");
        System.out.println("Parsing the string:\"" + queryString + "\"");
    }
}
