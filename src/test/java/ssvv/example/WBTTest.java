package ssvv.example;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.*;
import ssvv.example.repository.*;
import ssvv.example.service.*;
import ssvv.example.validation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Unit test for simple App.
 */
    public class WBTTest {
    private StudentXMLRepository studentRepo;
    private NotaXMLRepository notaRepo;
    private TemaXMLRepository temaRepo;

    private Service service;

    @Before
    public void setup() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Nota> notaValidator = new NotaValidator();
        Validator<Tema> temaValidator = new TemaValidator();


        studentRepo = new StudentXMLRepository(studentValidator, "testing/studenti.xml");
        notaRepo = new NotaXMLRepository(notaValidator, "testing/note.xml");
        temaRepo = new TemaXMLRepository(temaValidator, "testing/teme.xml");
        service = new Service(studentRepo, temaRepo, notaRepo);
    }

    @After
    public void tearDown() {
        try {
            String defaultFileContent = new String(Files.readAllBytes(Paths.get("testing/empty-studenti.xml")), StandardCharsets.UTF_8);
            String defaultFileContentTema = new String(Files.readAllBytes(Paths.get("testing/empty-teme.xml")), StandardCharsets.UTF_8);

            PrintWriter printWriter = new PrintWriter("testing/studenti.xml");
            PrintWriter printWriterTema = new PrintWriter("testing/teme.xml");

            printWriter.print(defaultFileContent);
            printWriterTema.print(defaultFileContentTema);
            printWriter.close();
            printWriterTema.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test1(){
        assertEquals(1, service.saveTema("1", "Assignment 1", 10, 2));
    }

    @Test
    public void test2(){
        assertEquals(0, service.saveTema("", "Assignment 2", 10, 2));
    }

    @Test
    public void tc1_wbt() {
        assertEquals(0, service.saveTema("", "desc", 10, 2));
    }

    @Test
    public void tc2_wbt() {
        assertEquals(0, service.saveTema("100", "", 5, 3));
    }

    @Test
    public void tc3_wbt() {
        assertEquals(0, service.saveTema("101", "descript", -1, 5));
    }

    @Test
    public void tc4_wbt() {
        assertEquals(0, service.saveTema("102", "d", 10, 16));
    }

    @Test
    public void tc5_wbt() {
        assertEquals(1, service.saveTema("100", "abc", 4, 3));
        assertEquals(0, service.saveTema("100", "abc", 4, 3));
    }

    @Test
    public void tc6_wbt() {
        assertEquals(1, service.saveTema("105", "xyz", 2, 1));
    }

    @Test
    public void tc7_wbt() {
        assertEquals(0, service.saveTema("101", "descript", 20, 5));
    }

    @Test
    public void tc8_wbt() {
        assertEquals(0, service.saveTema("102", "d", 10, -16));
    }

    @Test
    public void tc9_wbt() {
        assertEquals(0, service.saveTema("110", "asd", 2, 3));
    }

    @Test
    public void tc10_wbt() {
        assertEquals(0, service.saveTema(null, "desc", 10, 2));
    }

    @Test
    public void tc11_wbt() {
        assertEquals(0, service.saveTema(null, "", 5, 3));
    }
}
