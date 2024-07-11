package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries() {

        return converteDados(repository.findAll());


    }

    public List<SerieDTO> obterTop5Series() {
        // passando a busca como parametro para o metodo
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());


    }

    private List<SerieDTO> converteDados(List<Serie> series) {
        //transformando serie em seriedto
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(),
                        s.getTotalTemporadas(), s.getAvaliacao(),
                        s.getGenero(), s.getAtores(), s.getPoster(),
                        s.getSinopse())).collect(Collectors.toList());

    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    //recebe o id da classe controller
    public SerieDTO obterPorId(Long id) {

        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {

            Serie s = serie.get();

            return new SerieDTO(s.getId(), s.getTitulo(),
                    s.getTotalTemporadas(), s.getAvaliacao(),
                    s.getGenero(), s.getAtores(), s.getPoster(),
                    s.getSinopse());


        }


        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {

        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {

            Serie s = serie.get();

            return s.getEpisodios()
                    .stream().map(e -> new EpisodioDTO(e.getTemporada(),
                            e.getTitulo(),
                            e.getNumeroEpisodio()))
                            .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return repository.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
                .collect(Collectors.toList());
    }

    }

