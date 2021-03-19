package ssvv.example;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

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
public class AppTest
{
    private StudentXMLRepository studentRepo;
    private NotaXMLRepository notaRepo;
    private TemaXMLRepository temaRepo;

    private Service service;

    @Before
    public void setup(){
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Nota> notaValidator = new NotaValidator();
        Validator<Tema> temaValidator = new TemaValidator();

        studentRepo = new StudentXMLRepository(studentValidator, "testing/studenti.xml");
        notaRepo = new NotaXMLRepository(notaValidator, "testing/note.xml");
        temaRepo = new TemaXMLRepository(temaValidator, "testing/teme.xml");
        service = new Service(studentRepo,temaRepo,notaRepo);

        try {
            String defaultFileContent = new String(Files.readAllBytes(Paths.get("testing/empty-studenti.xml")), StandardCharsets.UTF_8);

            PrintWriter printWriter = new PrintWriter("testing/studenti.xml");

            printWriter.print(defaultFileContent);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testAdd1() {
        int r = service.saveStudent("1", "John", 934);
        assertEquals("John", studentRepo.findOne("1").getNume());
        assertEquals(1, r);
    }

    @Test
    public void testAdd2()
    {
        Validator<Student> studentValidator = new StudentValidator();
        StudentRepository repository = new StudentRepository(studentValidator);
        Student s = repository.save(new Student("", "John", 934));
        assertNull(s);
    }

    @Test
    public void TC7_EC_Valid(){
        int r = service.saveStudent("7","Emi",933);
        assertEquals(933,studentRepo.findOne("7").getGrupa());
    }

    @Test
    public void  TC6_EC_Valid(){
        int r = service.saveStudent("6","Stef",934);
        assertTrue(studentRepo.findOne("6").getGrupa() < 938);
    }

    @Test
    public void TC6_EC_Invalid(){
        int r = service.saveStudent("6","Stef",934);
        assertFalse(studentRepo.findOne("6").getGrupa() > 938);
    }

    @Test
    public void TC5_EC_Valid(){
        int r = service.saveStudent("5","Stef",931);
        assertTrue(studentRepo.findOne("5").getGrupa() > 110);
    }

    @Test
    public void TC5_EC_Invalid() {
        int r = service.saveStudent("5", "Stef", 100);
        assertEquals(0, r);
    }

    @Test
    public void TC4_EC_Valid(){
        int r = service.saveStudent("4","Andrei Bo$$", 934);
        assertEquals(1,r);
    }

    @Test
    public void TC4_EC_Invalid(){
        int r = service.saveStudent("4","Andrei Bo$$",  0);
        assertEquals(0,r);
    }
}
