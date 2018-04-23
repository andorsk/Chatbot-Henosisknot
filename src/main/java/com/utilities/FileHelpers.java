package com.utilities;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHelpers {

    public static List<String> listRecusivelyGetFilesWithExtension(String dir, String ext) {
        ArrayList<String> ret = new ArrayList<String>();
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                listRecusivelyGetFilesWithExtension(file.toString(), ext);
            } else {
                if (FilenameUtils.getExtension(file.getName()).equals(ext)) {
                    ret.add(file.getAbsolutePath());
                }
            }
        }
      return ret;
    }
}
