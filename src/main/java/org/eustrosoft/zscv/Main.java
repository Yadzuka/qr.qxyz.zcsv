package org.eustrosoft.zscv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        ZCSVRow row = new ZCSVRow();
        ZCSVFile file = new ZCSVFile();
        int code;
        boolean success;
        file.setFileName("test");
        file.setRootPath("E:\\AllProjects\\Java_projects\\Sources\\Java_product_projects\\zcsv\\CSVTestFiles");
        file.setConfigureFilePath("E:\\AllProjects\\Java_projects" +
                "\\Sources\\Java_product_projects\\zcsv\\CSVTestFiles\\ConfigurePath\\HelloConfigure.csv");
        try {
            // IF OPENING MODE = 0 - WE CANT WRITE IN THE FILE. (EXCEPTION HANDLED)
                if(file.tryOpenFile(1)) {
                    file.loadFromFile();
                    String k = file.getLineByIndex(7);
                    row = file.editRowObjectByIndex(7);
                    file.closeFile();
                    System.out.println(k);
                    System.out.println(row.get(1));
                    System.out.println(row.setStringSpecificIndex(0,"JJJ"));
                    file.reloadFromFile();
                    System.out.println(file.getLineByIndex(7));
                }else
                    System.out.println("Error with opening");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
