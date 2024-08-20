package org.example.studentapi.service;

import org.example.studentapi.model.Student;
import org.example.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStudentById() {
        // Dado que um estudante existe no repositório
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Quando o serviço é chamado para obter o estudante pelo ID
        Optional<Student> foundStudent = studentService.getStudentById(1L);

        // Então o estudante deve ser encontrado
        assertTrue(foundStudent.isPresent());
        assertEquals(1L, foundStudent.get().getId());
    }

    @Test
    void testCreateStudent() {
        // Dado que um estudante novo é criado
        Student student = new Student();
        student.setNome("João");
        student.setSobrenome("Silva");
        student.setMatricula("123456");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Quando o serviço é chamado para salvar o estudante
        Student createdStudent = studentService.createStudent(student);

        // Então o estudante deve ser salvo corretamente
        assertNotNull(createdStudent);
        assertEquals("João", createdStudent.getNome());
        verify(studentRepository, times(1)).save(student);
    }

    // Outros testes para updateStudent e deleteStudent podem ser adicionados aqui
}
