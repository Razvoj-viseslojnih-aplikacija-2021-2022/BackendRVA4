package rva.ctrls;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rva.jpa.Porudzbina;
import rva.jpa.StavkaPorudzbine;
import rva.repositories.PorudzbinaRepository;
import rva.repositories.StavkaPorudzbineRepository;

@CrossOrigin
@RestController
public class StavkaPorudzbineRestController {

	@Autowired
	private StavkaPorudzbineRepository stavkaPorudzbineRepository;
	
	@Autowired
	private PorudzbinaRepository porudzbinaRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("stavkaPorudzbine")
	public Collection<StavkaPorudzbine> getStavkePorudzbina() {
		return stavkaPorudzbineRepository.findAll();
	}
	
	@GetMapping("stavkaPorudzbine/{id}")
	public StavkaPorudzbine getStavkaPorudzbine(@PathVariable("id") Integer id) {
		return stavkaPorudzbineRepository.getOne(id);
	}
	
	@GetMapping("stavkePorudzbineZaPorudzbinaID/{id}")
	public Collection<StavkaPorudzbine> getStavkeByPorudzbinaID(@PathVariable("id") Integer id) {
		Porudzbina p = porudzbinaRepository.getOne(id);
		return stavkaPorudzbineRepository.findByPorudzbina(p);
	}
	
	@GetMapping("stavkePorudzbineCena/{cena}")
	public Collection<StavkaPorudzbine> getStavkeByCena(@PathVariable("cena") BigDecimal cena) {
		return stavkaPorudzbineRepository.findByCenaLessThanOrderById(cena);
	}
	
	@PostMapping("stavkaPorudzbine")
	public ResponseEntity<StavkaPorudzbine> insertStavkaPorudzbine(@RequestBody StavkaPorudzbine stavka) {
		if(!stavkaPorudzbineRepository.existsById(stavka.getId())) {
			stavkaPorudzbineRepository.save(stavka);
			return new ResponseEntity<StavkaPorudzbine> (HttpStatus.OK);
		}
		return new ResponseEntity<StavkaPorudzbine> (HttpStatus.CONFLICT);
	}
	
	@PutMapping("stavkaPorudzbine")
	public ResponseEntity<StavkaPorudzbine> updateStavkaPorudzbine(@RequestBody StavkaPorudzbine stavka) {
		if(stavkaPorudzbineRepository.existsById(stavka.getId())) {
			stavkaPorudzbineRepository.save(stavka);
			return new ResponseEntity<StavkaPorudzbine> (HttpStatus.OK);
		}
		return new ResponseEntity<StavkaPorudzbine> (HttpStatus.NO_CONTENT);
	}
	@DeleteMapping("stavkaPorudzbine/{id}")
	public ResponseEntity<StavkaPorudzbine> deleteStavkaPorudzbine(@PathVariable("id") Integer id) {
		if(stavkaPorudzbineRepository.existsById(id)) {
			stavkaPorudzbineRepository.deleteById(id);
			if (id == -100) 
				jdbcTemplate.execute("insert into stavka_porudzbine (id, porudzbina, redni_broj,"
						+ " artikl, kolicina, jedinica_mere, cena) "
						+ "values (-100, -100, 1, -100,20, 'komad', 100)");
			return new ResponseEntity<StavkaPorudzbine> (HttpStatus.OK);
		}
		return new ResponseEntity<StavkaPorudzbine> (HttpStatus.NO_CONTENT);
	}
}
