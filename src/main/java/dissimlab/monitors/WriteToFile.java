package dissimlab.monitors;

import jxl.*;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class WriteToFile {

    private static WritableCellFormat timesBoldUnderline;
    private static WritableCellFormat times;
    private static String inputFile = "d:/test.xls";

    public static void WriteToXLS(MonitoredVar monitoredVar) throws IOException, WriteException {
        File file = new File(inputFile);
        WritableWorkbook workbook = null;
        WritableSheet excelSheet = null;

        if(!file.exists())
        {
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("pl", "PL"));

            workbook = Workbook.createWorkbook(file, wbSettings);
        }
        else
        {
            workbook = Workbook.createWorkbook(file, read());
        }

        WritableSheet tempSheet = workbook.getSheet(monitoredVar.getName());
        if(tempSheet != null)
            excelSheet = tempSheet;
        else
        {
            int numberOfSheets = workbook.getNumberOfSheets();
             excelSheet = workbook.createSheet(monitoredVar.getName(), numberOfSheets);
        }

        createLabel(excelSheet);
        createContent(excelSheet, monitoredVar);

        workbook.write();
        workbook.close();
    }

    private static void createContent(WritableSheet sheet, MonitoredVar monitoredVar) throws WriteException {
        int numberOfSamples = monitoredVar.numberOfSamples();
        Change change;

        for(int i = 0; i < numberOfSamples; i++)
        {
            change = monitoredVar.getChanges().get(i);
            addNumber(sheet, 1, i+1, change.getValue());
            addNumber(sheet, 0, i+1, change.getTime());
        }
    }

    private static void createLabel(WritableSheet sheet)
            throws WriteException {

        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        times = new WritableCellFormat(times10pt);
        times.setWrap(true);

        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        addCaption(sheet, 0, 0, "Simulation Time");
        addCaption(sheet, 1, 0, "Value");
    }

    private static void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private static void addNumber(WritableSheet sheet, int column, int row,
                           Double doubleVar) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, doubleVar, times);
        sheet.addCell(number);
    }

    private static void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }


    public static Workbook read() throws IOException  {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);

            /*Sheet sheet = w.getSheet(0);*/
            return w;

        } catch (BiffException e) {
            e.printStackTrace();
            return null;
        }
    }
}
