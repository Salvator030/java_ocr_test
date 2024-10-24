package dot.business.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.Sheet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import dot.business.receipt.Receipt;

public class FastexcelHelper {

    public Map<Integer, List<String>> readExcel(String fileLocation) {
        Map<Integer, List<String>> data = new HashMap<>();
        try {
            try (FileInputStream file = new FileInputStream(fileLocation);
                    ReadableWorkbook wb = new ReadableWorkbook(file)) {
                Sheet sheet = wb.getFirstSheet();
                try (Stream<Row> rows = sheet.openStream()) {
                    rows.forEach(r -> {
                        data.put(r.getRowNum(), new ArrayList<>());

                        for (Cell cell : r) {
                            data.get(r.getRowNum()).add(cell.getRawValue());
                        }
                    });
                }
            }

        } catch (Exception e) {
            System.err.println(e);
        }
        return data;
    }

    private void createTableHead(Worksheet ws) {
        ws.width(0, 15);
        ws.width(1, 25);
        ws.width(2, 15);

        ws.range(0, 0, 0, 2).style().fontName("Arial").fontSize(16).bold().fillColor("3366FF").set();
        ws.value(0, 0, "Datum");
        ws.value(0, 1, "Laden");
        ws.value(0, 2, "Summe");
    }

    private void createSummRow(Worksheet ws, int rowNumber, double summ) {
        ws.range(rowNumber, 0, rowNumber, 2).style().fontSize(14).bold().set();
        ws.value(rowNumber, 0, "Gesammt:");
        ws.value(rowNumber, 2, summ);
    }

    private double getTotalSumm(List<Receipt> receipts) {
        double summ = 0;
        for (Receipt r : receipts) {
            summ += r.getSumm();
        }
        return summ;
    }

    private List<Receipt> parseDataToReceiptList(Map<Integer, List<String>> data)
            throws NumberFormatException, ParseException {
        Integer[] keySet = data.keySet().toArray(new Integer[0]);
        int lastRecepitIndex = keySet.length - 2;
        List<Receipt> receiptsList = new ArrayList<>();
        for (int i = 1; i <= lastRecepitIndex; i++) {

            receiptsList.add(new Receipt(new SimpleDateFormat("dd.MM.yyyy").parse(data.get(keySet[i]).get(0)),
                    data.get(keySet[i]).get(1), Double.parseDouble(data.get(keySet[i]).get(2))));
        }

        return receiptsList;
    }

    private void createTableBody(Worksheet ws, List<Receipt> receiptsList)
            throws NumberFormatException, ParseException {

        DateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy");
        int rowNumber = 2;
        for (Receipt r : receiptsList) {
            ws.range(rowNumber, 0, rowNumber, 2).style().wrapText(true).set();
            ws.value(rowNumber, 0, dataFormat.format(r.getDate()));
            ws.value(rowNumber, 1, r.getShopName());
            ws.value(rowNumber, 2, r.getSumm());
            ++rowNumber;
        }
        ++rowNumber;
        createSummRow(ws, rowNumber, getTotalSumm(receiptsList));
    }

    private String monthAndYearToString(Date date) {
        DateFormat dataFormat = new SimpleDateFormat("MM-yyyy");
        return dataFormat.format(date);
    }

    private String createFileName(String monthAndYear){
        return "Abrechnung-"+ monthAndYear;
    }

    private String getFileLocation(String fileName){
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        return path.substring(0, path.length() - 1) + fileName + ".xlsx";
    }

    public void writeExcel(String fileName, Receipt receipt) throws IOException, NumberFormatException, ParseException {
       
        String fileLocation = fileName != null ? getFileLocation(fileName) : getFileLocation(createFileName(monthAndYearToString(receipt.getDate())));
        Map<Integer, List<String>> data = readExcel(fileLocation);
        List<Receipt> receipts = parseDataToReceiptList(data);
        receipts.add(receipt);

        try (OutputStream os = Files.newOutputStream(Paths.get(fileLocation));
                Workbook wb = new Workbook(os, "MyApplication", "1.0")) {
            Worksheet ws = wb.newWorksheet("Sheet 1");
            createTableHead(ws);
            createTableBody(ws, receipts);

        }
    }
}