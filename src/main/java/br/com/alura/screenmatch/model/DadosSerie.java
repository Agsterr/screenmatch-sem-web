package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Year") String ano,
        @JsonAlias("Rated") String classificacao,
        @JsonAlias("Released") String lancamento,
        @JsonAlias("Runtime") String duracao,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Director") String diretor,
        @JsonAlias("Writer") String escritor,
        @JsonAlias("Actors") String atores,
        @JsonAlias("Plot") String descricao,
        @JsonAlias("Language") String idioma,
        @JsonAlias("Country") String pais,
        @JsonAlias("Awards") String premios,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Ratings") List<Rating> ratings,
        @JsonAlias("Metascore") String metascore,
        @JsonAlias("imdbRating") String imdbRating,
        @JsonAlias("imdbVotes") String imdbVotes,
        @JsonAlias("imdbID") String imdbID,
        @JsonAlias("Type") String tipo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("Response") String response) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Rating(
            @JsonAlias("Source") String fonte,
            @JsonAlias("Value") String valor) {
    }
}
