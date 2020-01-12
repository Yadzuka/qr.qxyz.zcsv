package org.eustrosoft.zcsvtest;

import org.eustrosoft.zscv.ZCSVFile;
import org.eustrosoft.zscv.ZCSVRow;
import org.junit.*;
import org.junit.runner.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ZCSVTest {

    private final static String pathToDirectoryWithTests =
            "E:\\AllProjects\\Java_projects\\Sources\\Java_product_projects\\zcsv\\src\\test\\javaCSVtests";
    ///
    ///     ZCSVFile test section
    ///
    @Test
    public void  writeNewFileTest(){
        String fileName = "testToExistingCreatedNewFile";
        String buffer;
        String bufferToSecondFile;
        boolean result = false;

        ArrayList<ZCSVRow> list = new ArrayList<>();
        list.add(new ZCSVRow("Hello Moscow!"));

        ZCSVFile newFile = new ZCSVFile();
        newFile.setArrayList(list);
        newFile.setFileName(fileName);
        newFile.setRootPath(pathToDirectoryWithTests);
        try {
            newFile.writeNewFile(fileName + "First");
            if(checkFileExisting(pathToDirectoryWithTests+"\\" + fileName + "First.csv")) {
                BufferedReader readerToFirstFile = new BufferedReader
                        (new InputStreamReader
                                (new FileInputStream(pathToDirectoryWithTests+"\\"+newFile.getFileName()),
                                    StandardCharsets.UTF_8));
                BufferedReader readerToSecondFile = new BufferedReader
                        (new InputStreamReader
                                (new FileInputStream(pathToDirectoryWithTests+"\\" + fileName + "First.csv"),
                                        StandardCharsets.UTF_8));

                while((buffer = readerToFirstFile.readLine()) != null){
                    if((bufferToSecondFile = readerToSecondFile.readLine()) != null) {
                        if (buffer.equals(bufferToSecondFile))
                            result = true;
                        else {
                            result = false;
                            break;
                        }
                    }else {
                        result = false;
                    }
                }
                Assert.assertTrue(result);
            }
        }catch (FileNotFoundException ex){
            System.out.printf
                    ("File \"%s\" by the path \"%s\" does not created!\n", fileName, pathToDirectoryWithTests);
            Assert.fail();
        }catch (IOException ex){
            System.out.println("Unknown IOException in writeNewFile() method class ZCSVFile");
            Assert.fail();
        }
    }

    /*@Test
    public void rewriteFileTest(){
        ZCSVFile rewriteFile = new ZCSVFile();
        rewriteFile.setRootPath(pathToDirectoryWithTests);
        rewriteFile.setFileName("rewriteFileTest");

        rewriteFile.rewriteFile();
    }*/
    ///
    ///    ZCSVRow test section
    ///
    private boolean checkFileExisting(String path){
        return Files.exists(Paths.get(path));
    }

}
