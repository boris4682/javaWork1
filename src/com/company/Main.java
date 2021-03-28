package com.company;

import com.company.lib.HDD;
import com.company.lib.RecordingDevice;
import com.company.lib.SSD;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static String path = System.getProperty("user.dir") + "\\src\\com\\company\\";
    public static String logPath = path + "files\\log.txt";
    public static String importedFilePath = path + "files\\SSD_HDD.csv";
    public static String serializedFilePath = path + "files\\RecordingDevices.txt";
    private static FileWriter logger = null;


    public static void main(String[] args) throws IOException {

        try {
                File logFile = new File(logPath);
                logFile.createNewFile();

                logger = new FileWriter(logPath, true);

                ArrayList<SSD> ssdList = readFile(importedFilePath, SSD.class);
                ArrayList<HDD> hddList = readFile(importedFilePath, HDD.class);

                List<RecordingDevice> allData = new ArrayList<RecordingDevice>(ssdList);
                allData.addAll(hddList);

                serialize(allData);
                List<RecordingDevice> deserializedData = deserialize();
                //Output of the program
                for (RecordingDevice deviceData : deserializedData) {
                    System.out.println(deviceData);
                }

        } catch (Throwable e) {
            logger.write("\n" + e.getClass().toString() + ": " + e.getMessage() + "\n");
            e.printStackTrace();
        } finally {
            logger.close();
        }
    }

    public static <T extends RecordingDevice> ArrayList<T> readFile(String path, Class<T> typeClass) throws Throwable {

        ArrayList<T> recodingDevices = new ArrayList<>();

        try {
            logger.write("Reading file: " + path + "\n");
            //CSVReader reader = new CSVReader(new FileReader(path));
            CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    // Skip the header
                    .withSkipLines(1)
                    .build();
            //Read all rows at once
            List<String[]> allRows = reader.readAll();

            Constructor<T> constructor = typeClass.getConstructor();
            String classFullName = constructor.getName();
            String className = classFullName.substring(classFullName.lastIndexOf('.')+1, classFullName.length());
            Class[] cArg = new Class[2]; //My constructor has 2 arguments
            cArg[0] = String[].class; //First argument is of *object* type Array
            cArg[1] = FileWriter.class; //Second argument is of *object* type FileWriter
            //Read CSV line by line and use the string array
            for(String[] row : allRows){
                List<String> list = Arrays.asList(row);
                String strOutput = list.toString();
                strOutput = strOutput.replaceAll("\\[|\\]", "");
                String[] words = strOutput.split(";");
                if(words[0].equals(className)){
                    T objByTemplate = typeClass.getDeclaredConstructor(cArg).newInstance(words, logger);
                    recodingDevices.add(objByTemplate);
                    logger.write(objByTemplate.toString() + "\n");
                }
            }
        } catch (Exception e) {
            logger.write("An exception was thrown while reading the file " + path + "\n");
            throw e instanceof InvocationTargetException ? e.getCause() : e;
        }
        logger.write("The file was read successfully: " + path + "\n");
        return recodingDevices;
    }

    public static void serialize(Object obj) throws IOException {
        FileOutputStream outputStream = null;
        logger.write(obj + "Serialize to a file along the path: " + serializedFilePath + "\n");
        try {
            outputStream = new FileOutputStream(serializedFilePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
            logger.write(" Seriallization was successful; \n");
        } catch (Exception e) {
            logger.write(obj + " Seriallization was failed; \n");
            throw e;
        }
    }

    public static <T> T deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        try {
            logger.write("Start deseriallize to path: " + serializedFilePath + "\n");
            fileInputStream = new FileInputStream(serializedFilePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            logger.write(" Deseriallized was success; \n");
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            logger.write(serializedFilePath + " Deseriallized was failed; \n");
            throw e;
        }
    }
}
