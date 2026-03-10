# LiterAlura - Catálogo de Livros 

O **LiterAlura** é uma aplicação Java de console que permite gerenciar um catálogo de livros consumindo dados da API [Gutendex](https://gutendex.com/). O projeto foi desenvolvido como parte do desafio da formação Java + Spring Boot da Alura.

## 🛠️ Tecnologias Utilizadas
- **Java 25**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Jackson** (para desserialização de JSON)
- **Maven** (gerenciamento de dependências)

## 🚀 Funcionalidades
1. **Busca por Título: Você digita o nome do livro, ele puxa na API, se tiver disponível converte o JSON e faz o INSERT no Postgres.
2. **Listagem Geral: Quer ver o que já tem no banco? Ele faz o SELECT * e te mostra tudo formatado.
3. **Filtro de Autores Vivos: Um dos pontos mais técnicos. Você dá um ano e ele filtra quem ainda estava vivo naquela época (lógica de datas no SQL).
4. **Filtro por Idioma: Busca rápida por siglas (pt, en, fr).
5. **Top 10: Pra ver quais são os que tem os maiores números de downloads.

## ⚙️ Como rodar o projeto
1. Clone o repositório.
2. No seu application.properties, muda o username e a password do seu Postgres.
3. E Rode a LiteraluraApplication no seu IntelliJ.

---
Desenvolvido por [Seu Nome/Samuel] 🚀
