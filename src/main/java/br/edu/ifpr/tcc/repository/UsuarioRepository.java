package br.edu.ifpr.tcc.repository;

import br.edu.ifpr.tcc.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByIdAndFisioterapeutaIsTrue(Long id);

    Optional<Usuario> findByIdAndPacienteIsTrue(Long id);

    Optional<Usuario> findByPacienteIsTrue();

    List<Usuario> findAllByPacienteIsTrue();

    List<Usuario> findAllByFisioterapeutaIsTrue();


}
