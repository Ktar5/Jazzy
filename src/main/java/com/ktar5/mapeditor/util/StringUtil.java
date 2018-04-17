package com.ktar5.mapeditor.util;

import org.pmw.tinylog.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StringUtil {
    //String[] lines = StringUtils.split(FileUtils.readFileToString(new File("...")), '\n');
    public static String readFileAsString(File file) {
        StringBuilder fileData = new StringBuilder();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                fileData.append(String.valueOf(buf, 0, numRead));
            }
            reader.close();
            fr.close();
        } catch (IOException e) {
            Logger.info(e);
            return null;
        }
        return fileData.toString();
    }
    
}
