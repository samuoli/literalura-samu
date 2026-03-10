package br.com.samu.literalura.repository;

import br.com.samu.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByIdioma(String idiomaEscolhido);

    List<Livro> findTop10ByOrderByNumeroDownloadsDesc();
}