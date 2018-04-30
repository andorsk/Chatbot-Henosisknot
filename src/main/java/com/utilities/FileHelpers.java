package com.utilities;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHelpers {

    /**
     * @param dir
     * @param ext
     * @param existingFiles Since it is recursive passes existing file list.
     * @return
     */
    public static List<File> listRecusivelyGetFilesWithExtension(File dir, String ext, ArrayList<File> existingFiles) {

        if (existingFiles == null || existingFiles.isEmpty()) {
            existingFiles = new ArrayList<File>();
        }

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                listRecusivelyGetFilesWithExtension(file, ext, existingFiles);
            } else {
                String extension = "." + FilenameUtils.getExtension("." + file.getName());
                if (extension.equals(ext)) {
                    existingFiles.add(file);
                }
            }
        }
        return existingFiles;
    }

    /**
     * Wrapper for the recursive method that passes a null value for the existing list of files.
     * @param dir
     * @param ext
     * @return
     */
    public static List<File> listRecusivelyGetFilesWithExtension(File dir, String ext) {
        return listRecusivelyGetFilesWithExtension(dir, ext, null);
    }
}