package io.vawke.practice.util;

import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Giovanni on 12-2-2017.
 */
@Getter
public class IOUtil {

    private Gson gson = new Gson();

    @Getter
    private static IOUtil util;

    public IOUtil() {
        util = this;
    }

    public String readJson(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return IOUtils.toString(fileInputStream, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeJson(String json, String pathWithFileName) {
        try {
            FileWriter fileWriter = new FileWriter(pathWithFileName);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
