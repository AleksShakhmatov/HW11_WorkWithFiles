package ru.coluchy;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.coluchy.model.Forester;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParsingFilesTest {

    private final ClassLoader cl = ParsingFilesTest.class.getClassLoader();

    @Test
    void pdfParsingTestFromZipFile() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files/filesforHW.zip")) {
            assert is != null;
            try (ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equals("Gips.pdf")) {
                        PDF content = new PDF(zis);
                        assertTrue(content.text.contains("Calculation of drywall for partitions, walls, ceilings"));
                        System.out.println(entry.getName());
                    }
                }
            }
        }
    }

    @Test
    void xlsParsingTestFromZipFile() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files/filesforHW.zip")) {
            assert is != null;
            try (ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equals("Cats.xlsx")) {
                        XLS xls = new XLS(zis);
                        Assertions.assertEquals("Ginger", xls.excel.getSheet("List1").getRow(1).getCell(0).getStringCellValue());
                        System.out.println(entry.getName());
                    }
                }
            }
        }
    }


    @Test
    void csvParsingTestFromZipFile() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files/filesforHW.zip")) {
            assert is != null;
            try (ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equals("Cars.csv")) {
                        CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                        List<String[]> content = csvReader.readAll();
                        Assertions.assertArrayEquals(
                                new String[]{"Toyota", "Black", "4500"}, content.get(3));
                        System.out.println(entry.getName());
                    }
                }
            }
        }
    }

    @Test
    void jsonParsingTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files/Forester.json")) {
            assert is != null;
            try (Reader reader = new InputStreamReader(is)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Forester forester = objectMapper.readValue(reader, Forester.class);

                Assertions.assertEquals("Subaru", forester.getBrand());
                Assertions.assertEquals("Forester", forester.getModel());
                Assertions.assertEquals("Japan", forester.getCountry());
                Assertions.assertArrayEquals(new String[]{"SF", "SG", "SH", "SJ", "SK"},
                        forester.getVehicle().toArray());
            }
        }
    }
}













