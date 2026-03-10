package br.com.samu.literalura;

import br.com.samu.literalura.model.Livro;
import br.com.samu.literalura.principal.Principal;
import br.com.samu.literalura.repository.AutorRepository;
import br.com.samu.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LivroRepository livroRepositorio;

    @Autowired
    private AutorRepository autorRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(livroRepositorio, autorRepositorio);
        principal.exibeMenu();
    }
}
