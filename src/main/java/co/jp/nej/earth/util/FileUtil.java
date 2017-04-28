package co.jp.nej.earth.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import co.jp.nej.earth.model.Directory;

public class FileUtil {

    /**
     * get file size
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        return file.getTotalSpace();
    }

    /**
     * get directory size
     *
     * @param directory
     * @return
     */
    public static long getDirectorySize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                length += file.getTotalSpace();
            } else {
                length += getDirectorySize(file);
            }
        }
        return length;
    }

    /**
     * get directory used minimum storage
     *
     * @param directories
     * @return
     */
    public static Directory getDirectoryUsedStorageMin(List<Directory> directories) {
        Directory result = null;
        File fileResult = null;
        for (Directory directory : directories) {
            File fileDirectory = new File(directory.getFolderPath());
            if (result == null) {
                result = directory;
                fileResult = fileDirectory;
            } else if (fileResult.getTotalSpace() > fileDirectory.getTotalSpace()) {
                result = directory;
                fileResult = fileDirectory;
            }
        }
        return result;
    }

    public static byte[] convertFileToBinary(File file) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllBytes(path);

    }
}
