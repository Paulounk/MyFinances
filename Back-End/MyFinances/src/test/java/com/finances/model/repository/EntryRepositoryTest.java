package com.finances.model.repository;

import java.math.BigDecimal;
import java.util.Optional;

//import static org.assertj.core.api.Assertions.*; = Importa todos os metodos estaticos da classe Assertions
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.finances.model.Entry;
import com.finances.model.enums.EntryStatus;
import com.finances.model.enums.EntryType;
import com.finances.repository.EntryRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test") 
public class EntryRepositoryTest {

	@Autowired
	EntryRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@DisplayName("Deve salvar um lançamento.")
	@Test
	public void mustSaveAEntry() {
		Entry entry = Entry.builder()
				.year(2021)
				.month(11)
				.description("Lançamento de Teste")
				.value(BigDecimal.valueOf(10))
				.type(EntryType.REVENUE)
				.status(EntryStatus.PENDING).build();
		
		entry = repository.save(entry);
		
		Assertions.assertThat(entry.getId()).isNotNull();
	}
	
	@DisplayName("Deve deletar um lançamento.")
	@Test
	public void mustDeleteAEntry() {
		
		Entry entry = createEntry();
		entityManager.persist(entry);
		
		repository.delete(entry);
		
		Entry emptyEntry = entityManager.find(Entry.class, entry.getId());
		
		Assertions.assertThat(emptyEntry).isNull();
		
		
	}
	
	@DisplayName("Deve atualizar um lançamento.")
	@Test
	public void mustUpdateAEntry() {
		Entry entry = createPersistAEntry();
		entry.setYear(2017);
		entry.setDescription("Lançamento atualizado");
		
		repository.save(entry);
		
		Entry entryUpdate = entityManager.find(Entry.class, entry.getId());
		
		Assertions.assertThat(entryUpdate.getYear()).isEqualTo(2017);
		Assertions.assertThat(entryUpdate.getDescription()).isEqualTo("Lançamento atualizado");
	}
	
	@DisplayName("Deve buscar um lançamento pelo Id")
	@Test
	public void mustSearchForAEntryById() {
		Entry entry = createPersistAEntry();
		
		Optional<Entry> entryFound = repository.findById(entry.getId());
		
		Assertions.assertThat(entryFound.isPresent()).isTrue();
		
	}
	
	
	
	//Metodos auxiliares para os testes
	
	private Entry createPersistAEntry() {
		Entry entryPersist = createEntry();
		entityManager.persist(entryPersist);
		return entryPersist;
		 
	}
	
	//Deixando metodo publico e estatico para acessa-lo no EntryServiceTest
	public static Entry createEntry() {
		return Entry.builder()
				.year(2021)
				.month(11)
				.description("Lançamento de Teste")
				.value(BigDecimal.valueOf(10))
				.type(EntryType.REVENUE)
				.status(EntryStatus.PENDING).build();
	}
	
	
	
}
