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
    public static void saveZipFile(Path destination, File pack, String name) throws IOException {
        Path newZipFilePath = destination.resolve(name);
        System.out.println("Saving ZIP file to: " + newZipFilePath);

        // 디렉터리 확인 및 생성
        if (!destination.toFile().exists()) {
            destination.toFile().mkdirs();
        }

        try (ZipFile zipFile = new ZipFile(pack);
             ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(newZipFilePath.toFile()))) {

            zipFile.stream().forEach(zipEntry -> {
                try (BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry))) {
                    ZipEntry newEntry = new ZipEntry(zipEntry.getName());
                    zipOutputStream.putNextEntry(newEntry);
                    
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, bytesRead);
                    }
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    System.err.println("Error processing entry: " + zipEntry.getName());
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.err.println("Error creating ZIP file: " + newZipFilePath);
            e.printStackTrace();
        }
    }
}