package org.example.studentapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.studentapi.model.Student;
import org.example.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        student.setNome("João");
        student.setSobrenome("Silva");
        student.setMatricula("123456"); // Quais são as regras pra matrícula? Pode ter até quantos dígitos? Pode ter letras? Pode ser nulo? Considere usar testes parametrizados para testar essas regras.

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent(student);

        assertNotNull(createdStudent);
        assertEquals("João", createdStudent.getNome());
        verify(studentRepository, times(1)).save(student);
    }

    @ParameterizedTest // Exemplo de teste parametrizado.
    @CsvSource({
        "123456, false",
        "0, true",
        "999999999, true",
        "12345678, false",
    })
    void testCreateStudentMatricula(String matricula, boolean excecaoEsperada) {
        Student student = new Student();
        student.setNome("João");
        student.setSobrenome("Silva");
        student.setMatricula(matricula);

        if (excecaoEsperada) {
            when(studentRepository.save(any(Student.class))).thenThrow(new RuntimeException("Matricula inválida"));
        } else {
            when(studentRepository.save(any(Student.class))).thenReturn(student);
        }

        if (excecaoEsperada) {
            try {
                studentService.createStudent(student);
            } catch (RuntimeException e) {
                assertEquals("Matricula inválida", e.getMessage());
            }
        } else {
            Student createdStudent = studentService.createStudent(student);
            assertNotNull(createdStudent);
            assertEquals("João", createdStudent.getNome());
            verify(studentRepository, times(1)).save(student);
        }
    }

    @Test
    void testDeleteStudent() {

    }

    @Test
    void testGetAllStudents() {

    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> foundStudent = studentService.getStudentById(1L);

        assertTrue(foundStudent.isPresent());
        assertEquals(1L, foundStudent.get().getId());
    }

    @Test
    void testPatchStudent() {

    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
    
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setNome("Nome Teste");
    
        Student result = studentService.updateStudent(1L, updatedStudent);
    
        assertEquals("Nome Teste", result.getNome());
    }
}
