package br.com.samu.literalura.principal;

import br.com.samu.literalura.model.*;
import br.com.samu.literalura.repository.AutorRepository;
import br.com.samu.literalura.repository.LivroRepository;
import br.com.samu.literalura.service.ConsumoApi;
import br.com.samu.literalura.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private List<DadosLivro> dadosLivro = new ArrayList<>();
    private LivroRepository livroRepositorio;
    private AutorRepository autorRepositorio;

    public Principal(LivroRepository livroRepositorio, AutorRepository autorRepositorio) {
        this.livroRepositorio = livroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void exibeMenu(){
        var opcao = -1;
        while(opcao != 0){
            var menu = """
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros de um determinado idioma
                    6- Listar o Top 10 por números de downloads
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosNoAno();
                    break;
                case 5:
                    listarLivrosDeUmIdioma();
                    break;
                case 6:
                    listarTop10Livros();
                    break;

            }
        }
    }


    private void buscarLivro() {
        System.out.println("Qual livro você deseja buscar?");
        var nomeLivro = leitura.nextLine();

        var json = consumoApi.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
        DadosResposta dados = conversor.obterDados(json, DadosResposta.class);

        // Usamos o ifPresent para garantir que só tentamos salvar se a API retornou algo
        dados.resultado().stream().findFirst().ifPresent(d -> {
            try {
                Livro livro = new Livro(d);
                livroRepositorio.save(livro);
                System.out.println("\n----- LIVRO ENCONTRADO -----");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Downloads: " + livro.getNumeroDownloads());
                System.out.println("----------------------------\n");
            } catch (Exception e) {
                System.out.println("Erro: Não foi possível salvar o livro. (Talvez ele já esteja registrado?)");
            }
        });

        if (dados.resultado().isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse título.");
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepositorio.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado no banco de dados.");
        } else {
            System.out.println("\n---------- LIVROS REGISTRADOS ----------");
            livros.forEach(l -> {
                System.out.println("Título: " + l.getTitulo());
                System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNome() : "Desconhecido"));
                System.out.println("Idioma: " + l.getIdioma());
                System.out.println("Downloads: " + l.getNumeroDownloads());
                System.out.println("----------------------------------------");
            });
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepositorio.findAll();

        autores.forEach(a ->
                System.out.println("Autor: " + a.getNome() +
                        " | Nascimento: " + a.getAnoNascimento() +
                        " | Falecimento: " + a.getAnoFalecimento())
        );
    }

    private void listarAutoresVivosNoAno() {
        System.out.println("Digite o ano que deseja pesquisar:");
        var ano = leitura.nextInt();
        leitura.nextLine();

        // Chamamos o repositório passando o mesmo ano para os dois parâmetros da query
        List<Autor> autoresVivos = autorRepositorio.findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);

        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo no ano de " + ano);
        } else {
            System.out.println("\n---------- AUTORES VIVOS EM " + ano + " ----------");
            autoresVivos.forEach(a -> {
                System.out.println("Autor: " + a.getNome());
                System.out.println("Nascimento: " + a.getAnoNascimento());
                System.out.println("Falecimento: " + (a.getAnoFalecimento() != null ? a.getAnoFalecimento() : "Ainda vivo/Desconhecido"));
                System.out.println("----------------------------------------------");
            });
        }
    }
    private void listarLivrosDeUmIdioma() {
        System.out.println("""
            Digite o código do idioma para busca:
            es - espanhol
            en - inglês
            fr - francês
            pt - português
            """);
        var idiomaEscolhido = leitura.nextLine();

        List<Livro> livrosPorIdioma = livroRepositorio.findByIdioma(idiomaEscolhido);

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + idiomaEscolhido);
        } else {
            System.out.println("\n---------- LIVROS EM " + idiomaEscolhido.toUpperCase() + " ----------");
            livrosPorIdioma.forEach(l -> {
                System.out.println("Título: " + l.getTitulo());
                System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNome() : "Desconhecido"));
                System.out.println("Número de Downloads: " + l.getNumeroDownloads());
                System.out.println("----------------------------------------------");
            });
        }
    }

    private void listarTop10Livros() {
        List<Livro> topLivros = livroRepositorio.findTop10ByOrderByNumeroDownloadsDesc();

        System.out.println("\n---------- TOP 10 LIVROS MAIS BAIXADOS ----------");
        topLivros.forEach(l -> {
            System.out.println("Título: " + l.getTitulo());
            System.out.println("Downloads: " + l.getNumeroDownloads());
            System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNome() : "Desconhecido"));
            System.out.println("-------------------------------------------------");
        });
    }

}
