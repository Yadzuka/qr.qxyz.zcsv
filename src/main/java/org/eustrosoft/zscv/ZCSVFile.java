package org.eustrosoft.zscv;// EustroSoft.org PSPN/CSV project
//
// (c) Alex V Eustrop & EustroSoft.org 2020
// 
// LICENSE: BALES, ISC, MIT, BSD on your choice
//
//

import org.apache.logging.log4j.core.appender.routing.Route;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * work with File as CSV database
 */
public class ZCSVFile{

    private final static String [] MODES_TO_FILE_ACCESS = {"r","rw","rws","rwd"};
    private final static String NEXT_LINE_SYMBOL = "\n";
    private final static String FILE_EXTENSION = ".csv";

    private static FileChannel channel;

    private String rootPath;
    private String sourceFileName = null;
    private ArrayList fileRows = new ArrayList();

    public void setRootPath(String rootPath) { this.rootPath = rootPath + "\\"; }

    public void setFileName(String fileName) {
        sourceFileName = fileName + FILE_EXTENSION;
    }

    public String getFileName() {
        return sourceFileName;
    }

    // actions on file
    // open file for read (or write, or append, or lock)
    // ALL FILE STRINGS NOW DOWNLOADED TO THE ARRAY LIST AND CHANNEL OPENED
    public void openFile(int mode) throws IOException {
        if(channel == null) {
            try {
                if (mode > MODES_TO_FILE_ACCESS.length - 1 || mode < 0)
                    throw new ZCSVException("Неприавльно указан мод работы с файлом!");
                RandomAccessFile raf = new RandomAccessFile(rootPath + sourceFileName, MODES_TO_FILE_ACCESS[mode]);
                channel = raf.getChannel();

                System.out.println("Channel opened!");
            } catch (ZCSVException ex) {
                ex.printError();
            }
        }else{
            System.out.println("Channel already opened!");
        }
    }

    // close file and free it for others
    public boolean closeFile() throws IOException {
        if(channel != null) {
            if (channel.isOpen()) {
                channel.close();
                channel = null;

                System.out.println("Channel closed successfully!");

                return true;
            }
        }

        System.out.println("Channel doesn't closed!");

        return false;
    }

    // exclusively lock file (can be used before update)
    public boolean isFileLock() throws IOException {
        FileLock lock;
        if(channel == null) {
            System.out.println("Channel doesn't defined!");
            return false;
        }
        try {
            lock = channel.lock(0, channel.size(), false);
            System.out.println("Channel locked!");
        }catch (IOException ex){
            return true;
        }
        return (lock == null);
    }

    //actions on file content
    // load all lines from file & parse valid rows
    public int loadFromFile() throws IOException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);

            byte [] bytes = buffer.array();
            String f = new String(bytes, StandardCharsets.UTF_8);

            for(String s : f.trim().split(NEXT_LINE_SYMBOL)){
                if((s == null || "".equals(s)) && s.startsWith("#"))
                    continue;
                else
                    fileRows.add(new ZCSVRow(s));
            }

            System.out.println("Array filled by each string!");
            return 1;
        }catch(IOException | NullPointerException ex){
            System.out.println("Array does not filled!");
            return 0;
        }
    }

    //reload data from file if changed
    public int reloadFromFile() {
        try {
            channel.force(true);
            System.out.println("Reloaded from file!");
            return 1;
        }catch (IOException ex){
            System.out.println("Doesn't reloaded!");
            return  0;
        }
    }

    // update file content based on changes done on rows
    public int updateFromChannel() throws IOException {
        channel.force(true);
        return 1;
    }

    // fully rewrite content of file with in-memory data
    public int rewriteFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rootPath + sourceFileName));
            for(int i = 0;i < fileRows.size();i++){
                writer.write(fileRows.get(i).toString() + NEXT_LINE_SYMBOL);
            }
        }catch (IOException ex){
            return 0;
        }
        return 1;
    }

    // the same as as above but new file only
    public int writeNewFile(String newFileName) throws IOException {
        ZCSVRow row;
        String fullPath = rootPath + newFileName + FILE_EXTENSION;
        try {
            Path path = Paths.get(fullPath);
            Files.createFile(path);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
            for (int i = 0; i < fileRows.size(); i++) {
                row = (ZCSVRow) fileRows.get(i);
                writer.write(row.toString() + NEXT_LINE_SYMBOL);
            }
        }catch (ClassCastException ex){

        } catch (IOException ex) {
            return 0;
        }
        return 1;
    }

    // write changes to file but do not touch any existing data (it's paranodal-safe version of update() method)
    public boolean appendAndGoToNextLine(String stringToWrite) {
        try {
            BufferedWriter writer = new BufferedWriter
                    (new OutputStreamWriter
                            (new FileOutputStream(rootPath + sourceFileName, true), StandardCharsets.UTF_8));
            try (BufferedWriter out = writer) {
                out.write(stringToWrite + "\n");
                out.flush();
            }
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
        return true;
    }

    // get line from loaded file by number (as is, text upto \n)
    public String getLineByIndex(int i) throws IOException {
        String stringToGet;
        BufferedReader reader = new BufferedReader(new FileReader(rootPath + sourceFileName));
        i--;
        while ((stringToGet = reader.readLine()) != null) {
            if (i < 0) {
                return "Not valid value";
            } else if (i > 0) {
                i--;
            } else {
                return stringToGet;
            }
        }
        return "This string does not existing";
    }

    // get read-only row from loaded file by number (only proper rows, not commented lines)
    public ZCSVRow getRowObjectByIndex(int i) {
        return new ZCSVRow().getNames();
    }

    // the same as above but ready for update, change it and use update() method of parent ZCSVFile
    public ZCSVRow editRowObjectByIndex(int i) {
        return new ZCSVRow();
    }



    // constructors
    public ZCSVFile() {

    }

} //ZCSVFile
