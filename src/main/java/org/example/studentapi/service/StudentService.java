package org.example.studentapi.service;

import org.example.studentapi.model.Student;
import org.example.studentapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {

    private static final String STUDENT_NOT_FOUND = "Student not found for this id: ";
    private static final String MATRICULA_EXISTS = "Matricula already exists: ";
    private static final String INVALID_FIELD = "Invalid field: ";
    // Considere criar um enum para essas mensagens de erro, e talvez um exception handler para tratar essas exceções, na qual você poderia passar a mensagem de erro como parâmetro. (Ex: throw new StudentApiException(ErrosEnum.STUDENT_NOT_FOUND, Map.of("id", id)))

    private static final String NOME = "nome";
    private static final String SOBRENOME = "sobrenome";
    private static final String MATRICULA = "matricula";
    private static final String TELEFONES = "telefones";

    private final StudentRepository studentRepository;

    @Autowired // EU ACHO que não precisa usar @Autowired quando tem um construtor. O Spring irá injetar automaticamente os beans necessários no construtor, inclusive dispensando a necessidade de instanciar o objeto com new.
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) { // Por que retornar um Optional? Se o estudante não for encontrado, você pode lançar uma exceção, com o status 404, por exemplo. Isso evita que o código que chama esse método tenha que lidar com o Optional.
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        validateStudent(student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND + id));

        student.setNome(studentDetails.getNome());
        student.setSobrenome(studentDetails.getSobrenome());
        student.setMatricula(studentDetails.getMatricula());
        student.setTelefones(studentDetails.getTelefones());

        validateStudent(student);
        return studentRepository.save(student);
    }
    // Considere avaliar o acoplamento entre a classe de entidade Student e StudentService. O método updateStudent está fazendo muitas coisas, como buscar o estudante no banco de dados, atualizar os dados e salvar novamente no banco. Talvez seja interessante criar um método updateStudent no repositório, que faça a atualização dos dados, e chamar esse método no service.
    // Além disso, as camadas da service e do repository estão bem acopladas. Considere criar uma camada de DTO para separar as camadas e diminuir o acoplamento.
    public Student patchStudent(Long id, Map<String, Object> updates) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case NOME:
                    student.setNome((String) value);
                    break;
                case SOBRENOME:
                    student.setSobrenome((String) value);
                    break;
                case MATRICULA:
                    student.setMatricula((String) value);
                    break;
                case TELEFONES:
                    student.setTelefones((List<String>) value); // Considere validar se o valor é uma lista de strings
                    break;
                default:
                    throw new IllegalArgumentException(INVALID_FIELD + key);
            }
        });

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id) // Considere criar um método deleteById no repository, para que a lógica de deleção fique no repositório.
                .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND + id));

        studentRepository.delete(student);
    }

    private void validateStudent(Student student) { // Considere criar uma classe de validação para separar as regras de validação da lógica de negócio
        if (studentRepository.findByMatricula(student.getMatricula()).isPresent()) {
            throw new IllegalArgumentException(MATRICULA_EXISTS + student.getMatricula());
        }
    }
}
