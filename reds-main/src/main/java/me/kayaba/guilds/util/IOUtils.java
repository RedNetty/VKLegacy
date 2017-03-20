package me.kayaba.guilds.util;

import com.google.common.io.*;

import java.io.*;
import java.net.*;
import java.util.*;

public final class IOUtils {
    private IOUtils() {

    }


    public static String inputStreamToString(InputStream inputStream) throws IOException {
        return CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
    }


    public static void saveInputStreamToFile(InputStream inputStream, File file) throws IOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        }
    }


    public static List<String> getFilesWithoutExtension(File directory) {
        final List<String> list = new ArrayList<>();
        File[] filesList = directory.listFiles();

        if (filesList != null) {
            for (File file : filesList) {
                if (file.isFile()) {
                    String name = file.getName();
                    if (org.apache.commons.lang.StringUtils.contains(name, '.')) {
                        name = org.apache.commons.lang.StringUtils.split(name, '.')[0];
                        list.add(name);
                    }
                }
            }
        }

        return list;
    }


    public static String toString(InputStream in, String encoding) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int len;

        while ((len = in.read(buf)) != -1) {
            byteArrayOutputStream.write(buf, 0, len);
        }

        return new String(byteArrayOutputStream.toByteArray(), encoding);
    }


    public static String read(File file) throws IOException {
        return inputStreamToString(new FileInputStream(file));
    }


    public static void write(File file, String string) {
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(string);
        } catch (FileNotFoundException e) {
            LoggerUtils.exception(e);
        }
    }


    public static List<File> listFilesRecursively(File root) {
        File[] list = root.listFiles();
        final List<File> fileList = new ArrayList<>();

        if (list == null) {
            return fileList;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                fileList.addAll(listFilesRecursively(f));
            } else {
                fileList.add(f);
            }
        }

        return fileList;
    }


    public static String getContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        return IOUtils.toString(in, encoding);
    }
}
