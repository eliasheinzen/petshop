package br.com.peti9.petshop;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByAtivo(boolean ativo);

    boolean existsByNome(String nome);

}
