package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")


public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpsodio;
    private Double avaliacao;
    private LocalDate dataLancamento;


    //mapeamento muitos pra 1 com a classe serie
    @ManyToOne()
    private Serie serie;

    public Episodio() {
    }

    public Episodio(int numeroTemporada, DadosEpsodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpsodio = dadosEpisodio.numero();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }
       try {
           this.dataLancamento = LocalDate.parse(dadosEpisodio.lancamento());
       }catch (DateTimeParseException ex){
           this.dataLancamento = null;
       }




    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpsodio() {
        return numeroEpsodio;
    }

    public void setNumeroEpsodio(Integer numeroEpsodio) {
        this.numeroEpsodio = numeroEpsodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpsodio=" + numeroEpsodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento
                ;
    }
}
