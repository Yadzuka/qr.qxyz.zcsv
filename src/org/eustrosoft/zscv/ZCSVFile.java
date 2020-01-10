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
public class ZCSVFile {

    private final String [] MODES_TO_FILE_ACCESS = {"r","rw","rws","rwd"};
    private final static String NEXT_LINE_SYMBOL = "\n\r";
    private final static String FILE_EXTENSION = ".csv";

    private Path rootPath;
    private String source_file_name = null;
    private ArrayList col_rows = new ArrayList();

    public void setRootPath(String rootPath) {
        this.rootPath = Paths.get(rootPath+"/");
    }

    public void setFileName(String fileName) {
        source_file_name = fileName + FILE_EXTENSION;
    }

    public String getFileName() {
        return source_file_name;
    }

    // actions on file
    //open file for read (or write, or append, or lock)
    public FileChannel open(int mode) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(rootPath + source_file_name, MODES_TO_FILE_ACCESS[mode]);
        FileChannel channel = raf.getChannel();
        return channel;
    }

    // close file and free it for others
    public boolean close(FileChannel targetChannel) throws IOException {
        if(targetChannel.isOpen())
            targetChannel.close();
        else
            return false;
        return true;
    }

    // exclusively lock file (can be used before update)
    public boolean isLock(FileChannel targetChannel) throws IOException {
        FileLock lock;
        try {
            lock = targetChannel.lock(0, targetChannel.size(), false);
        }catch (IOException ex){
            return true;
        }
        return (lock == null);
    }

    //actions on file content
    // load all lines from file & parse valid rows
    public int load(FileChannel targetChannel) throws IOException {
        try {
            FileChannel chan = targetChannel;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            chan.read(buffer);

            System.out.println(new String(buffer.array(), StandardCharsets.UTF_8));
        }catch(IOException ex){
            return 0;
        }
        return 1;
    }

    //reload data from file if changed
    public int reload(FileChannel targetChannel) {
        try {
            targetChannel.force(true);
            return 1;
        }catch (IOException ex){
            return  0;
        }
    }

    // update file content based on changes done on rows
    public int update(FileChannel targetChannel) throws IOException {
        targetChannel.force(true);
        return 1;
    }

    // fully rewrite content of file with in-memory data
    public int rewrite() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rootPath + source_file_name));
            for(int i = 0;i < col_rows.size();i++){
                writer.write(col_rows.get(i).toString() + NEXT_LINE_SYMBOL);
            }
        }catch (IOException ex){
            return 0;
        }
        return 1;
    }

    // the same as as above but new file only
    public int writeNew(String newFileName) throws IOException {
        try {
            String fullPath = rootPath + newFileName + ".csv";
            Path path = Path.of(fullPath);
            Files.createFile(path);
        } catch (IOException ex) {
            return 0;
        }
        return 1;
    }

    // write changes to file but do not touch any existing data (it's paranodal-safe version of update() method)
    public boolean append(String stringToWrite) {
        try {
            BufferedWriter writer = new BufferedWriter
                    (new OutputStreamWriter
                            (new FileOutputStream(rootPath + source_file_name, true), StandardCharsets.UTF_8));
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
    public String getLine(int i) throws IOException {
        String stringToGet;
        BufferedReader reader = new BufferedReader(new FileReader(rootPath + source_file_name));
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
    public ZCSVRow getRow(int i) {
        return new ZCSVRow().getNames();
    }

    // the same as above but ready for update, change it and use update() method of parent ZCSVFile
    public ZCSVRow editRow(int i) {
        return new ZCSVRow();
    }

    // constructors
    public ZCSVFile() {

    }

} //ZCSVFile
