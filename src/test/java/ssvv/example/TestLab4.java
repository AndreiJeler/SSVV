package ssvv.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepository;
import ssvv.example.repository.StudentXMLRepository;
import ssvv.example.repository.TemaXMLRepository;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;

public class TestLab4 {

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
            String defaultFileContentGrades = new String(Files.readAllBytes(Paths.get("testing/empty-note.xml")), StandardCharsets.UTF_8);


            PrintWriter printWriter = new PrintWriter("testing/studenti.xml");
            PrintWriter printWriterTema = new PrintWriter("testing/teme.xml");
            PrintWriter printWriterNota = new PrintWriter("testing/note.xml");

            printWriter.print(defaultFileContent);
            printWriterTema.print(defaultFileContentTema);
            printWriterNota.print(defaultFileContentGrades);
            printWriter.close();
            printWriterTema.close();
            printWriterNota.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_addStudent() {
        assertEquals(1, service.saveStudent("2", "Andy", 331));
    }

    @Test
    public void test_addAssignment() {
        assertEquals(1, service.saveTema("1", "Assignment 1", 10, 2));
    }

    @Test
    public void test_addGrade() {
        assertEquals(-1, service.saveNota("", "", 10, 2, "feedback"));
    }

    @Test
    public void test_integration() {
        assertEquals(1, service.saveStudent("2", "Andy", 331));
        assertEquals(1, service.saveTema("1", "Assignment 1", 10, 2));
        assertEquals(1, service.saveNota("2", "1", 10, 10, "Good"));
    }

    @Test
    public void incremental_addStudent() {
        assertEquals(1, service.saveStudent("1", "Elev", 934));
    }

    @Test
    public void incremental_addAssignment() {
        //incremental_addStudent();
        assertEquals(1, service.saveStudent("1", "Elev", 934));
        assertEquals(1, service.saveTema("1", "Assignment", 3, 1));
    }

    @Test
    public void incremental_addGrade() {
        //incremental_addAssignment();
        assertEquals(1, service.saveStudent("1", "Elev", 934));
        assertEquals(1, service.saveTema("1", "Assignment", 3, 1));
        assertEquals(1, service.saveNota("1", "1", 8, 4, "Late"));

    }

}
