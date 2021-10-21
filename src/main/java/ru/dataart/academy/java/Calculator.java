package ru.dataart.academy.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Calculator {

    private final static String UNPACKED_ZIPS_DIR = "zipUnpacked" + File.separator;

    /**
     * @param zipFilePath -  path to zip archive with text files
     * @param character   - character to find
     * @return - how many times character is in files
     */
    public Integer getNumberOfChar(String zipFilePath, char character) {
        //Task implementation
        Integer counter = 0;

        try {
            List<File> filesFromZip = unpackZip(zipFilePath);
            for (File file : filesFromZip) {
                List<String> lines = Files.readAllLines(file.toPath());
                for (String line : lines) {
                    for (char letter : line.toCharArray()) {
                        if (letter == character) {
                            counter++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return counter;
    }

    /**
     * @param zipFilePath - path to zip archive with text files
     * @return - max length
     */

    public Integer getMaxWordLength(String zipFilePath) {
        //Task implementation
        int maxWordLength = 0;

        try {
            List<File> filesFromZip = unpackZip(zipFilePath);
            for (File file : filesFromZip) {
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1);
                for (String line : lines) {
                    for (String word : line.split(" ")) {
                        maxWordLength = Math.max(word.length(), maxWordLength);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxWordLength;
    }

    private List<File> unpackZip(String zipFilePath) {
        List<File> filesFromZip = new ArrayList<>();

        if (zipFilePath == null || zipFilePath.isEmpty()) {
            throw new IllegalStateException("Incorrect file path");
        }

        File dir = new File(UNPACKED_ZIPS_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        try (ZipFile zf = new ZipFile(zipFilePath, StandardCharsets.UTF_8)) {
            Enumeration<? extends ZipEntry> zipEntries = zf.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();

                String pathName = UNPACKED_ZIPS_DIR + zipEntry.getName();
                Files.copy(zf.getInputStream(zipEntry), Paths.get(pathName), StandardCopyOption.REPLACE_EXISTING);
                File file = new File(pathName);
                if (Files.exists(file.toPath())) {
                    filesFromZip.add(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filesFromZip;
    }

}
