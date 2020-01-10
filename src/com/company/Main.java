package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ZCSVFile newFile = new ZCSVFile();
        newFile.setRootPath("E:\\");
        newFile.setFileName("newFile");
        //newFile.writeNew("hello");
        //newFile.append("Hello!");
        System.out.println(newFile.getLine(1));
    }
}
