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

        file.setFileName("test");
        file.setRootPath("E:\\AllProjects\\Java_projects\\Sources\\Java_product_projects\\zcsv\\CSVTestFiles");
        try {
            if(!file.closeFile()){
                file.openFile(2);
                file.loadFromFile();
                file.writeNewFile("Hello");
                file.closeFile();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
