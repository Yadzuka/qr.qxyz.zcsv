package org.eustrosoft.zscv;// EustroSoft.org PSPN/CSV project
//
// (c) Alex V Eustrop & EustroSoft.org 2020
// 
// LICENSE: BALES, ISC, MIT, BSD on your choice
//
//

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
    private final static String NEXT_LINE_SYMBOL = "\n\r";
    private final static String FILE_EXTENSION = ".csv";

    private static FileChannel channel;

    private Path rootPath;
    private String sourceFileName = null;
    private ArrayList<ZCSVRow> fileRows = new ArrayList<>();

    // Experimental
    public void setArrayList(ArrayList<ZCSVRow> row){
        fileRows = row;
    }
    //

    public void setRootPath(String rootPath) {
        this.rootPath = Paths.get(rootPath+"\\");
    }

    public void setFileName(String fileName) {
        sourceFileName = fileName + FILE_EXTENSION;
    }

    public String getFileName() {
        return sourceFileName;
    }

    // actions on file
    // open file for read (or write, or append, or lock)
    public FileChannel openFile(int mode) throws IOException {
        if(channel == null) {
            try {
                if (mode > MODES_TO_FILE_ACCESS.length - 1 || mode < 0)
                    throw new ZCSVException();
                RandomAccessFile raf = new RandomAccessFile(rootPath + sourceFileName, MODES_TO_FILE_ACCESS[mode]);
                channel = raf.getChannel();
                return channel;
            } catch (ZCSVException ex) {
                ex.WriteError("Exception in mode selecting part");
            }
        }
        return null;
    }

    // close file and free it for others
    public boolean closeFile() throws IOException {
        if(channel != null) {
            if (channel.isOpen()) {
                channel.close();
                channel = null;
                return true;
            }
        }
        return false;
    }

    // exclusively lock file (can be used before update)
    public boolean isFileLock() throws IOException {
        FileLock lock;
        try {
            lock = channel.lock(0, channel.size(), false);
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
            System.out.println(new String(buffer.array(), StandardCharsets.UTF_8));
        }catch(IOException ex){
            return 0;
        }
        return 1;
    }

    //reload data from file if changed
    public int reloadFromFile() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.force(true);
            channel.read(buffer);
            return 1;
        }catch (IOException ex){
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
        try {
            String fullPath = rootPath + newFileName + FILE_EXTENSION;
            Path path = Paths.get(fullPath);
            Files.createFile(path);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
            for(int i = 0;i < fileRows.size();i++){
                writer.write(fileRows.get(i).get(i).toString() + NEXT_LINE_SYMBOL);
            }
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
