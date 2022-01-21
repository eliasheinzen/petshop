package br.com.peti9.petshop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

// GET http://localhost:8080/pet/5
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping
    public List<Pet> getAllPets(@RequestParam(required = false) Boolean ativo) {
        if (ativo == null) {
            return this.petRepository.findAll();
        }
        return this.petRepository.findAllByAtivo(ativo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet createPet(@RequestBody CreatePetDto createPet) {
        Pet pet = new Pet();
        pet.setNome(createPet.getNome());
        pet.setEspecie(createPet.getEspecie());
        pet.setDataNascimento(createPet.getDataNascimento());
        pet.setPorte(createPet.getPorte());
        return this.petRepository.save(pet);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long petId, @RequestBody UpdatePetDto updatePet) {
        boolean isDuplicatePet = this.petRepository.existsByNome(updatePet.getNome());
        if (isDuplicatePet) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Optional<Pet> maybePet = this.petRepository.findById(petId);
        if (maybePet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Pet pet = maybePet.get();
        pet.setNome(updatePet.getNome());
        pet.setEspecie(updatePet.getEspecie());
        pet.setDataNascimento(updatePet.getDataNascimento());
        pet.setPorte(updatePet.getPorte());
        return ResponseEntity.ok().body(this.petRepository.save(pet));
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Long petId) {
        this.petRepository.deleteById(petId);
    }

    @PutMapping("/{petId}/ativo")
    public ResponseEntity<Void> updateAtivo(@PathVariable Long petId, @RequestBody UpdateAtivoDto updateAtivo) {
        Optional<Pet> maybePet = this.petRepository.findById(petId);
        if (maybePet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Pet pet = maybePet.get();
        pet.setAtivo(updateAtivo.isAtivo());
        this.petRepository.save(pet);
        return ResponseEntity.noContent().build();
    }

}
