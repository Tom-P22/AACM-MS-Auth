package cl.municipalidad.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.municipalidad.auth.model.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByUsername(String username);
}