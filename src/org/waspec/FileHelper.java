package org.waspec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHelper {

    public static boolean save(String fileName, SerializableToString object) {
        PrintStream fileOutput;
        try {
            fileOutput = new PrintStream(new File(fileName));
        } catch (FileNotFoundException e) {
            return false;
        }
        ArrayList<String> lines = object.serialize();
        for (String s : lines) {
            fileOutput.println(s);
        }
        fileOutput.close();
        return true;
    }

    public static boolean load(String fileName, SerializableToString object) {
        Scanner fileInput;
        try {
            fileInput = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            return false;
        }
        ArrayList<String> lines = new ArrayList<>();
        while (fileInput.hasNext()) {
            String s = fileInput.nextLine();
            lines.add(s);
        }
        object.deserialize(lines);
        fileInput.close();
        return true;
    }
}
