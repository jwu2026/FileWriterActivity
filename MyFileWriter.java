import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class MyFileWriter {
    public static void main(String[] args) {
        String data = "Hello, World!";
        String fileName1 = "example.txt";
        String fileName2 = "example2.txt";
        String fileName3 = "example3.txt";
        String fileName4 = "example4.txt";
        String fileName5 = "example5.txt";

        // 1. Using FileWriter
        try (FileWriter writer = new FileWriter(fileName1)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Using BufferedWriter
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName2))) {
            bufferedWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Using FileOutputStream
        try (FileOutputStream outputStream = new FileOutputStream(fileName3)) {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. Using BufferedOutputStream
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileName4))) {
            bufferedOutputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5. Using Files (java.nio.file)
        try {
            Files.write(Paths.get(fileName5), data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            testHashFileEmptyFiles();
            testHashFileLargeFiles();
            testHashFileSpecialChars();
            testHashFileNonExistent();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String hashFile(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String str = "", line = bufferedReader.readLine();
            while (line != null) {
                str += line;
                line = bufferedReader.readLine();
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(str.getBytes());
            String hStr = "";
            for (byte b : hashBytes) {
                hStr += String.format("%02x", b);
            }

            return hStr;

        } catch (FileNotFoundException e) {
            System.err.println("File is not found");
        } catch (IOException e) {
            System.err.println("Error in file reading");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm is not found");
        }

        return null;

    }

    // Prints output that matches the SHA-256 hash of an empty file
    public static void testHashFileEmptyFiles() throws IOException {
        Path empty = Files.createTempFile("empty", ".txt");
        System.out.println(MyFileWriter.hashFile(empty.toString()));
    }

    // Able to hash large files and prints the correct hash string
    public static void testHashFileLargeFiles() throws IOException {
        Path large = Files.createTempFile("large", ".txt");
        String str = "";
        for (int i = 0; i < 10000; i++) {
            str += "100";
        }
        Files.writeString(large, str);
        System.out.println(MyFileWriter.hashFile(large.toString()));
    }

    // Able to hash files with emojis and characters in another language, prints the
    // correct hash string
    public static void testHashFileSpecialChars() throws IOException {
        Path special = Files.createTempFile("special", ".txt");
        Files.writeString(special, "我喜欢虫🪱🪱");
        System.out.println(MyFileWriter.hashFile(special.toString()));
    }

    // Prints the hash of a nonexistent file and throws FileNotFoundException
    public static void testHashFileNonExistent() throws IOException {
        System.out.println("File doesn't exist: " + MyFileWriter.hashFile("nonexistent.txt"));
    }

    private static void printFileSize(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("File size of " + fileName + " is " + file.length() + " bytes");
        } else {
            System.out.println("File " + fileName + " does not exist.");
        }
    }

}