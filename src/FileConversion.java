import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileConversion {

    // Method to convert files between .txt and .csv formats
    public static void convertFile(String file, boolean txtOrCsv) throws IOException {
        // Open the FileInputStream within this method
        FileInputStream input = new FileInputStream(file);
        Scanner scanner = new Scanner(input);
        List<String> lines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        input.close();

        if (txtOrCsv) {
            convertTxtToCsv(file, lines);
        } else {
            convertCsvToTxt(file, lines);
        }
    }

    private static void convertTxtToCsv(String file, List<String> lines) throws IOException {
        String csvFile = file.replace(".txt", ".csv");
        try (PrintWriter out = new PrintWriter(csvFile)) {
            for (String line : lines) {
                String[] tokens = line.split("\\s+");
                String csvLine = Arrays.stream(tokens)
                        .map(token -> token.contains(",") ? "\"" + token + "\"" : token)
                        .collect(Collectors.joining(","));
                out.println(csvLine);
            }
        }
    }

    private static void convertCsvToTxt(String file, List<String> lines) throws IOException {
        String txtFile = file.replace(".csv", ".txt");
        try (PrintWriter out = new PrintWriter(txtFile)) {
            for (String line : lines) {
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                String txtLine = Arrays.stream(tokens)
                        .map(token -> token.replaceAll("^\"|\"$", ""))
                        .collect(Collectors.joining(" "));
                out.println(txtLine);
            }
        }
    }

    // Method to normalize file contents
    public static void normalizeFile(String file, boolean txtOrCsv) throws IOException {
        FileInputStream input = new FileInputStream(file);
        Scanner scanner = new Scanner(input);
        List<String> normalizedLines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cells = txtOrCsv ? line.split("\\s+") : line.split(","); // Split based on file type
            for (int i = 0; i < cells.length; i++) {
                cells[i] = normalizeCell(cells[i]);
            }
            normalizedLines.add(String.join(txtOrCsv ? " " : ",", cells));
        }
        scanner.close();
        input.close();

        // Write normalized content back to the file
        try (PrintWriter out = new PrintWriter(file)) {
            for (String normalizedLine : normalizedLines) {
                out.println(normalizedLine);
            }
        }
    }

    private static String normalizeCell(String cell) {
        if (cell.isEmpty()) {
            return "N/A";
        } else if (cell.matches("-?\\d+")) { // Integer
            return String.format("%+010d", Integer.parseInt(cell));
        } else if (cell.matches("-?\\d*\\.\\d+")) { // Float/Double
            double value = Double.parseDouble(cell);
            if (value > 100 || value < 0.01) {
                return String.format("%+.2e", value);
            } else {
                return String.format("%+.2f", value);
            }
        } else if (cell.length() > 13) { // Long String
            return cell.substring(0, 10) + "...";
        } else {
            return cell; // No change
        }
    }
}
