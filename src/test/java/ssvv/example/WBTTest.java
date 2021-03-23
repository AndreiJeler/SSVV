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
}
