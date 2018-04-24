package com.basictests;

import com.utilities.FileHelpers;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    final List<String> ext = new ArrayList<String>();;
    @BeforeTest
    public void init(){
        ext.add(".txt");
        ext.add(".tmp");
        ext.add(".mpm");
        ext.add(".cat");
    }
    @Test
    public void TestListFilesWithExtension(){
        Path t = createFoldersWithFilesSample();
        if(t != null){
            //Get Files with ext.
            Assert.assertEquals(FileHelpers.listRecusivelyGetFilesWithExtension(t.toString(), ".txt", null).size(), 7);
            deleteTempFolderRecursive(t.toString());
        } else throw new NullPointerException();

    }

    private void deleteTempFolderRecursive(String t){
        try {
            FileUtils.deleteDirectory(new File(t));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private Path createFoldersWithFilesSample(){
        try {
            Path t = Files.createTempDirectory("tmp");
            createFilesUnderParent(t.toString(), 12);
            for(int i = 0; i<2; i++){
                File dir = new File(t.toString() + "/dir" + i);
                dir.mkdir();
                createFilesUnderParent(dir.getAbsolutePath(), 5);
            }
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void createFilesUnderParent(String parent_dir , int num){
        for(int i =0; i< num; i++){
            File f = null;
            try {
                int extindex = i % ext.size();
                f = File.createTempFile("tmp", ext.get(extindex), new File(parent_dir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
