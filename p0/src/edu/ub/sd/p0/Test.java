package edu.ub.sd.p0;

// Clase para testear ComUtils

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtils cmUtils = new ComUtils(file);
            cmUtils.writeTest();
            System.out.println(cmUtils.readTest());
        } catch (IOException e) {
            System.out.println("Error Found during Operation:" + e.getMessage());
            e.printStackTrace();
        }
    }
}

