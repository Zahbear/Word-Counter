import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


class Config {
    private static final String CONFIG_FILE = "config.properties"; // Todo: change to properties.cfg
    private static final Properties properties = new Properties();

    public static int getWordCountLimit() {
        loadConfig();
        return Integer.parseInt(properties.getProperty("wordCountLimit", "420"));
    }

    public static void setWordCountLimit(int limit) {
        properties.setProperty("wordCountLimit", Integer.toString(limit));
        saveConfig();
    }

    protected static void loadConfig() {
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveConfig() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Main {

    private static final Map<String, Integer> wordCountMap = new HashMap<>();
    private static String filePath;
    private static String fileContents;

    private static int wordCountLimit = 420; // Initialize with a large value

    public static void main(String[] args) {
        Config.loadConfig();
        runCommandMenu();
    }

    private static void setWordCountLimit(int wordCountLimit) {
        Main.wordCountLimit = wordCountLimit;
    }

    private static void setFilePath(String filePath) {
        Main.filePath = filePath;
    }

    private static void setFileContents(String fileContents) {
        Main.fileContents = fileContents;
    }

    public static void runCommandMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            System.out.println();
            System.out.println("Commands Menu");
            System.out.println("1 - Presentation");
            System.out.println("2 - File Selection");
            System.out.println("3 - Analyze File");
            System.out.println("4 - Validation testing");
            System.out.println("0 - Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> presentation();
                case 2 -> selectFile();
                case 3 -> analyzeFile();
                case 4 -> System.out.println("Validation go BRRRR");
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Exiting program. Later kkr00t.");
    }

    private static void printParagraph(String title, String description) {
        System.out.println(title);
        System.out.println(description);
        System.out.println();
    }

    private static void presentation() {
        System.out.printf("\nWelcome to my domain, the WordcounterWithMenu dimension.\n");
        System.out.printf("Here, I will allow you to select a file, read it, analyze the text,\n");
        System.out.printf("and much more. I challenge you to break the flow and cause you an exit.\n\n");

        printParagraph("File Selector", "This will allow you to select a file on your machine. It will prompt the user to enter a path to a file. After that, it will read the contents.");
        printParagraph("File Analyzer", "Will analyze the text in the file and count the individual occurrences. It will also prompt you for a way to present this info.");
        printParagraph("Validation Testing", "This section WILL contain a suite of validation tests to address both functionality, edge cases, and caught errors/loopholes.");
        printParagraph("Exit / Back", "Back will allow you to make a step back in the menu. If you feel you've caught all my secrets, you're free to also Exit the program.");

        System.out.printf("Thank you for being a guest in my WordcounterWithMenu dimension!\n");
    }

    private static void selectFile() {
        Scanner scanner = new Scanner(System.in);
        boolean fileSelected = false;

        while (!fileSelected) {
            System.out.println("Enter new file path: (Format /home/folder/file.extension)");
            setFilePath(scanner.nextLine());

            try {
                if (isFileValid(filePath)) {
                    fileContents = readFile(filePath);
                    System.out.println("File selected and read successfully.");
                    fileSelected = true;
                    Config.setWordCountLimit(420);// resets limit to prior changes
                } else {
                    throw new IOException("File validation failed. Please select a valid file.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean isFileValid(String filePath) {
        File fileToRead = new File(filePath);
        return fileToRead.exists() && fileToRead.isFile();
    }

    private static String readFile(String filePath) {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            System.err.println("An error occurred while reading file " + e.getMessage());
            return "";
        }
    }

    private static void analyzeFile() {
        try {
            if (fileContents == null) {
                System.out.println("File has not been read. Do you want to set a file path? (Y/N)");
                Scanner scanner = new Scanner(System.in);
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("y")) {
                    selectFile();
                } else {
                    System.out.println("Returning to the main menu.");
                    return;
                }
            }

            if (fileContents.isEmpty()) {
                System.out.println("No file content is available. Select a file first.");
            } else {
                // Actual wordcounter call
                countWordsAndAnalyze(fileContents);

                // Submenu for word count options
                printWordCountSubMenu();

                // Now, print the word count results based on user's choices
                printWordCountResults(wordCountMap);
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
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

    private static void printWordCountSubMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Analyze File");
            System.out.println("1. Print File Contents");
            System.out.println("2. Print Wordcount Results");
            System.out.println("3. Set Result Limit");
            System.out.println("4. Select Format and Print");
            System.out.println("0. Back");

            int submenuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (submenuChoice) {
                case 1:
                    printFileContents();
                    break;
                case 2:
                    printWordCountResults(wordCountMap);
                    break;
                case 3:
                    setResultLimit();
                    break;
                case 4:
                    selectFormat();
                    break;
                case 0:
                    return; // Return to the previous menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void setResultLimit() {
        System.out.print("Enter the maximum number of results to display: ");
        Scanner scanner = new Scanner(System.in);
        int newLimit = scanner.nextInt();
        Config.setWordCountLimit(newLimit); // Set and save the new limit
    }

    private static void printWordCountResults(Map<String, Integer> wordCountMap) {
        System.out.println("Word Count Results:");
        System.out.println("_________________________");
        System.out.println("| Word (MapKey) | Value |");
        System.out.println("|_______________|_______|");

        int wordCountLimit = Config.getWordCountLimit(); // Get the word count limit
        int count = 0;

        for (Map.Entry<String, Integer> currentEntry : wordCountMap.entrySet()) {
            if (count >= wordCountLimit) {
                break; // Exit the loop when the limit is reached
            }

            String word = currentEntry.getKey();
            int countValue = currentEntry.getValue();

            // Use formatting strings to align the data in the table
            System.out.printf("| %-13s | %-5d |\n", word, countValue);

            count++;
        }

        System.out.println("_________________________");
    }

    private static void selectFormat() {
        System.out.println("Select Word Count Format:");
        System.out.println("1. Unaltered / Default");
        System.out.println("2. Reverse Order");
        System.out.println("3. Most Occurrences First");
        System.out.println("4. Least Occurrences First");
        System.out.println("5. Alphabetical Order");
        System.out.println("6. Reverse Alphabetical Order (bcs.iFknCan)");

        Scanner scanner = new Scanner(System.in);
        int formatChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        switch (formatChoice) {
            case 1: // Unaltered / Default
                System.out.println("Showing unaltered list, muie.");
                printWordCountResults(wordCountMap);
                break;
            case 2: // Reverse Order
                // Get the original map as a list of entries
                List<Map.Entry<String, Integer>> originalList = new ArrayList<>(wordCountMap.entrySet());
                // Reverse the list to present from the last entry to the first
                Collections.reverse(originalList);
                // Create a new map based on the reversed list
                Map<String, Integer> reverseOrderMap = new LinkedHashMap<>();
                for (Map.Entry<String, Integer> entry : originalList) {
                    reverseOrderMap.put(entry.getKey(), entry.getValue());
                }

                printWordCountResults(reverseOrderMap);
                break;
            case 3: // Most Occurrences First (Descending Order)
                List<Map.Entry<String, Integer>> mostOccurrencesList = new ArrayList<>(wordCountMap.entrySet());
                mostOccurrencesList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

                Map<String, Integer> mostOccurrencesMap = new LinkedHashMap<>();
                for (Map.Entry<String, Integer> entry : mostOccurrencesList) {
                    mostOccurrencesMap.put(entry.getKey(), entry.getValue());
                }

                printWordCountResults(mostOccurrencesMap);
                break;
            case 4: // Least Occurrences First (Ascending Order)
                List<Map.Entry<String, Integer>> leastOccurrencesList = new ArrayList<>(wordCountMap.entrySet());
                leastOccurrencesList.sort((e1, e2) -> e1.getValue().compareTo(e2.getValue()));

                Map<String, Integer> leastOccurrencesMap = new LinkedHashMap<>();
                for (Map.Entry<String, Integer> entry : leastOccurrencesList) {
                    leastOccurrencesMap.put(entry.getKey(), entry.getValue());
                }

                printWordCountResults(leastOccurrencesMap);
                break;
            case 5: // Alphabetical Order
                List<Map.Entry<String, Integer>> alphabeticalList = new ArrayList<>(wordCountMap.entrySet());
                alphabeticalList.sort(Map.Entry.comparingByKey());
//              alphabeticalList.sort(Comparator.comparing(Map.Entry::getKey));

                Map<String, Integer> alphabeticalOrderMap = new LinkedHashMap<>();
                for (Map.Entry<String, Integer> entry : alphabeticalList) {
                    alphabeticalOrderMap.put(entry.getKey(), entry.getValue());
                }

                printWordCountResults(alphabeticalOrderMap);
                break;
            case 6: // Reverse Alphabetical Order
                List<Map.Entry<String, Integer>> reverseAlphabeticalList = new ArrayList<>(wordCountMap.entrySet());
                reverseAlphabeticalList.sort((e1, e2) -> e2.getKey().compareTo(e1.getKey())); // Todo: try quickSort

                Map<String, Integer> reverseAlphabeticalOrderMap = new LinkedHashMap<>();
                for (Map.Entry<String, Integer> entry : reverseAlphabeticalList) {
                    reverseAlphabeticalOrderMap.put(entry.getKey(), entry.getValue());
                }

                printWordCountResults(reverseAlphabeticalOrderMap);
                break;
            default:
                System.out.println("Using the default format.");
                printWordCountResults(wordCountMap);
                break;
        }
    }

    private static void printFileContents() {
        System.out.println("File Contents:");
        System.out.println(fileContents);
    }
}




// 4, 6, 1 , 3, 5, 2, 7, 9
// 4 , 6
// 6 , 1
// 4 , 1, 6, 3, 5, 2, 7, 9
// 4,






