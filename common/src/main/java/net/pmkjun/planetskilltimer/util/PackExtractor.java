package net.pmkjun.planetskilltimer.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.LongConsumer;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class PackExtractor {

    public static void extractPack(Path destination, File pack, String name) throws IOException {
        extractPack(destination, pack, name, c -> {}, () -> {});
    }

    public static void extractPack(Path destination, File pack, String name, LongConsumer itemCountConsumer, Runnable onItemFinished) throws IOException {
        try (ZipFile zipFile = new ZipFile(pack.toString())) {
            itemCountConsumer.accept(zipFile.size());

            Stream<? extends ZipEntry> zipStream = zipFile.stream().parallel();

            zipStream.forEach(zipEntry -> {
            	File newFile = new File(destination.resolve(name).toFile(), zipEntry.getName());
                newFile.getParentFile().mkdirs();

                if (!zipEntry.isDirectory()) {
                    try (FileOutputStream outputStream = new FileOutputStream(newFile)) {
                        BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));

                        byte[] buffer = new byte[4096];
                        int read;
                        while ((read = inputStream.read(buffer)) >= 0) {
                            outputStream.write(buffer, 0, read);
                        }
                        inputStream.close();
                        onItemFinished.run();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
    public static void createZip(Path destination, File source, String name) throws IOException {
        createZip(destination, source, name, c -> {}, () -> {});
    }
    
    public static void createZip(Path destination, File source, String name, LongConsumer itemCountConsumer, Runnable onItemFinished) throws IOException {
        File zipFile = new File(destination.toFile(), name + ".zip");
        zipFile.getParentFile().mkdirs();
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            File[] files = source.listFiles();
            if (files != null) {
                itemCountConsumer.accept(files.length);
                for (File file : files) {
                    zipFile(file, zipOut, file.getName());
                    onItemFinished.run();
                }
            } else {
                System.out.println("Source directory is empty or not a directory.");
            }
        }
    }
    
    private static void zipFile(File file, ZipOutputStream zipOut, String fileName) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            
            byte[] buffer = new byte[4096];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }
            zipOut.closeEntry();
        }
    }
}