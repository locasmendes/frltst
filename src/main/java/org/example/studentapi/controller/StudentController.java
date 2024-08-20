package org.example.studentapi.controller;

import org.example.studentapi.model.Student;
import org.example.studentapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "Listar estudantes", description = "Retorna uma lista de estudantes")
    @ApiResponse(responseCode = "200", description = "Estudantes encontrados")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estudante por ID", description = "Retorna um estudante com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Estudante encontrado")
    @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar parcialmente um estudante", description = "Atualiza parcialmente um estudante com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Estudante atualizado")
    @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    public ResponseEntity<Student> patchStudent(@PathVariable(value = "id") Long id,
                                                @RequestBody Map<String, Object> updates) {
        Student patchedStudent = studentService.patchStudent(id, updates);
        return ResponseEntity.ok(patchedStudent);
    }


    @PostMapping("/register")
    @Operation(summary = "Registrar um estudante", description = "Registra um novo estudante")
    @ApiResponse(responseCode = "201", description = "Estudante registrado")
    @ApiResponse(responseCode = "400", description = "Campo inválido")
    @ApiResponse
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um estudante", description = "Atualiza um estudante com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Estudante atualizado")
    @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long id,
                                                 @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um estudante", description = "Deleta um estudante com base no ID fornecido")
    @ApiResponse(responseCode = "204", description = "Estudante deletado")
    @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    public ResponseEntity<Void> deleteStudent(@PathVariable(value = "id") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
