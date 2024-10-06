import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FormerClass {

    private static final Map<String, Integer> wordCountMap = new HashMap<>();

    public static void main(String[] args) {


        if (args.length != 1) {
            System.out.print("This got here");
            return;
        }

        String filePath = args[0];

        if (!doesFileExist(filePath)) {
            System.out.print("File doesn't exists or is not a file");
            return;
        }

        String fileContents = readPath(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            e.printStackTrace(); // This prints the stack trace
        }

        countWordsAndAnalyze(fileContents);

//    This does not show results in new line
//        System.out.print("Word Count Map: " + wordCountMap);
        System.out.println("Word Count Map:");
        for (Map.Entry<String, Integer> currentEntry : wordCountMap.entrySet()) {
            System.out.println(currentEntry.getKey() + ": " + currentEntry.getValue());
        }
    }

    private static boolean doesFileExist(String filePath) {
        File fileToRead = new File(filePath);
        return fileToRead.exists() && fileToRead.isFile();
    }

    public static String readPath(String inputPath) {
        try {
            return Files.readString(Path.of(inputPath));
        } catch (IOException e) {

            System.err.println("An error occurred while rading the file: " + e.getMessage());
            return "Exception found!";
        }
    }

    private static void countWordsAndAnalyze(String text) {
        String[] words = text.split("\\s+"); //Split text into words

        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if (!word.isEmpty()) {
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }
    }
}
