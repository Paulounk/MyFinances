package com.finances.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.finances.exception.BusinessRuleException;
import com.finances.model.Entry;
import com.finances.model.User;
import com.finances.model.enums.EntryStatus;
import com.finances.model.enums.EntryType;
import com.finances.model.repository.EntryRepositoryTest;
import com.finances.repository.EntryRepository;
import com.finances.service.impl.EntryServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EntryServiceTest {

	@SpyBean
	EntryServiceImpl service;
	
	@MockBean
	EntryRepository repository;
	
	@DisplayName("Deve salvar um lançamento")
	@Test
	public void mustSaveAEntry() {
		
		//Scenary
		Entry entryToSave = EntryRepositoryTest.createEntry();
		Mockito.doNothing().when(service).validate(entryToSave);
		
		Entry entrySave = EntryRepositoryTest.createEntry();
		entrySave.setId(1l);
		entrySave.setStatus(EntryStatus.PENDING);
		Mockito.when(repository.save(entryToSave)).thenReturn(entrySave);
		
		//Action
		Entry entry = service.save(entryToSave);
		
		//Verification
		Assertions.assertThat(entry.getId()).isEqualTo(entrySave.getId());	
		Assertions.assertThat(entry.getStatus()).isEqualTo(EntryStatus.PENDING);
		
	}
	
	@DisplayName("Não deve salvar um lançamento quando houver erro de validação")
	@Test
	public void shouldNotSaveAReleaseWhenThereIsAValidationError() {
		
		//Scenary
		Entry entry = EntryRepositoryTest.createEntry();
		Mockito.doThrow(BusinessRuleException.class).when(service).validate(entry);
		
		//Action and Verification
		Assertions.catchThrowableOfType(() -> service.save(entry), BusinessRuleException.class);
		Mockito.verify(repository, Mockito.never()).save(entry);
		
		
	}
	
	@DisplayName("Deve atualizar um lançamento")
	@Test
	public void mustUpdateAEntry() {
		
		//Scenary
		Entry entrySave = EntryRepositoryTest.createEntry();
		entrySave.setId(1l);
		entrySave.setStatus(EntryStatus.PENDING);
		Mockito.doNothing().when(service).validate(entrySave);
		
	
		Mockito.when(repository.save(entrySave)).thenReturn(entrySave);
		
		//Action
		service.update(entrySave);
		
		//Verification
		Mockito.verify(repository, Mockito.times(1)).save(entrySave);
		
	}
	
	@DisplayName("Não deve atualizar um lançamento que não possui id")
	@Test
	public void shouldNotUpdateAEntryThatHasNoId() {
		
		//Scenary
		Entry entryToSave = EntryRepositoryTest.createEntry();
		
		//Action and Verification
		Assertions.catchThrowableOfType(() -> service.update(entryToSave), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(entryToSave);
		
	}
	
	@DisplayName("Deve deletar um lançamento")
	@Test
	public void mustDeleteAEntry() {
		
		//Scenary
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1l);
		
		//Action
		service.delete(entry);
		
		//Verification
		Mockito.verify(repository).delete(entry);
		
	}
	
	@DisplayName("Não deve deletar um lançamento sem o id")
	@Test
	public void shouldNoTDeleteAEntryWithoutId() {
		
		//Scenary
		Entry entry = EntryRepositoryTest.createEntry();
				
		//Action
		Assertions.catchThrowableOfType(() -> service.delete(entry), NullPointerException.class);
				
		//Verification
		Mockito.verify(repository, Mockito.never()).delete(entry);
	}
	
	@DisplayName("Deve filtrar um lançamento")
	@Test
	public void mustFilterEntries() {
		
		//Scenary
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1l);

		List<Entry> lista = Arrays.asList(entry);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//Action
		List<Entry> result = service.search(entry);
		
		//Verification
		Assertions.assertThat(result).isNotEmpty().hasSize(1).contains(entry);
		
	}
	
	@DisplayName("Deve atualizar o status de um lançamento")
	@Test
	public void mustUpdateTheStatusOfARelease() {
		
		//Scenary
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1l);
		entry.setStatus(EntryStatus.PENDING);
		
		EntryStatus newStatus = EntryStatus.CONFIRMED;
		Mockito.doReturn(entry).when(service).update(entry);
		
		//Action
		service.updateStatus(entry, newStatus);
		
		//Verification
		Assertions.assertThat(entry.getStatus()).isEqualTo(newStatus);
		Mockito.verify(service).update(entry);
		
	}
	
	@DisplayName("Deve obter um lançamento por Id")
	@Test
	public void mustGetAResultById() {
		
		//Scenary
		Long id = 1l;
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(entry));
		
		//Action
		Optional<Entry> result = service.getEntryById(id);
		
		//Verification
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	

	@DisplayName("Não deve obter um lançamento quando o mesmo não existir")
	@Test
	public void mustNotGetAEntryWhenItDoesntExist() {
		
		//Scenary
		Long id = 1l;
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//Action
		Optional<Entry> result = service.getEntryById(id);
		
		//Verification
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	@DisplayName("Deve validar um lançamento")
	@Test
	public void mustValidateAEntry() {
		
		Entry entry = new Entry();
		
		Throwable erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid description.");
		
		entry.setDescription("");
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid description.");
		
		entry.setDescription("Salario");
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid month.");
		
		entry.setYear(0);
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid month.");
		
		entry.setYear(13);
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid month.");
		
		entry.setMonth(1);
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid year.");
		
		entry.setYear(202);
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid year.");
		
		entry.setYear(2020);
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid user.");
		
		entry.setUser(new User());
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid user.");
		
		entry.getUser().setId(1l);
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a value greater than zero.");
		
		entry.setValue(BigDecimal.ZERO);
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a value greater than zero.");
		
		entry.setValue(BigDecimal.valueOf(1));
		
		erro = Assertions.catchThrowable( () -> service.validate(entry) );
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a release type.");

	}
	
	@DisplayName("Deve obter o saldo por usuario")
	@Test
	public void deveObterSaldoPorUsuario() {
		
		//Scenary
		Long idUser = 1l;
		
		Mockito.when(repository
				.getBalanceByTypeEntryAndUserAndStatus(idUser, EntryType.REVENUE, EntryStatus.CONFIRMED)) 
				.thenReturn(BigDecimal.valueOf(100));
		
		Mockito.when( repository
				.getBalanceByTypeEntryAndUserAndStatus(idUser, EntryType.EXPENDITURE, EntryStatus.CONFIRMED)) 
				.thenReturn(BigDecimal.valueOf(50));
		
		//Action
		BigDecimal saldo = service.getBalanceByUser(idUser);
		
		
		//Verification
		Assertions.assertThat(saldo).isEqualTo(BigDecimal.valueOf(50));
		
	}
	
}
