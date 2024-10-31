package dot.business.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHandler {

    private File inputFile;
    private File outputFolder;
    private final String defaultFileName = "Kassenbons-Abrechnung";
    private String outputFileName =  defaultFileName;
    private String outputFileDate;
    private Path fullOuputFilePath; 
    private final String outputFileType = ".xlsx";
    private final String tessDataPath = "src/main/resources/tessdata";
    private final File documentsDirectory = new File(
            System.getProperty("user.home") + File.separator + "Documents" + File.separator);

    public void setInputFile(String filePath) {
        this.inputFile = new File(filePath);
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFolder() {
        return outputFolder;
    }

    public String getTessDataPath() {
        return tessDataPath;
    }

    public File getDocumentsDirectory() {
        return documentsDirectory;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    
    





    public Path getFullOuputFilePath() {
        return fullOuputFilePath;
    }

    public String getOutputFileDate() {
        return outputFileDate;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setOutputFileDate(String outputFileDate) {
        this.outputFileDate = outputFileDate;
    }

    public static void writeToFile(String name, String res) {
        int counter = 1;
        String pathString = ("src/main/resources/out" + name + ".txt");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(pathString));
            printWriter.write(res);
        } catch (Exception e) {
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

      private String monthAndYearToString(Date date) {
        DateFormat dataFormat = new SimpleDateFormat("MM-yyyy");
        return dataFormat.format(date);
    }

    private String createFullFileName(String previx, String date) {
        return previx + "-" + date + ".xlsx";
    }

    private String fileLocationToString(File folder, String fileName, String date) {
        String path = folder.getAbsolutePath();
        return path.substring(0, path.length() - 1) + createFullFileName(fileName, date);
    }

    private String getFileLocation(File folder, String fileName, String date) {
        String fileLocation = fileLocationToString(folder, fileName, date);
        return fileLocation;
    }

}