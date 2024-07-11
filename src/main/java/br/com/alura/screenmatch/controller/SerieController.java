package br.com.alura.screenmatch.controller;


import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

//para não precisar ficar colocando /series
@RequestMapping("/series")
public class SerieController {


    //injeção de dependencia
    @Autowired
    private SerieService servico;

    @GetMapping
    public List<SerieDTO> obterSeries(){

        //chamada do metodo da classe serieserviço graças a injeção de dependencia
     return servico.obterTodasAsSeries();
    }
   @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series(){
        return servico.obterTop5Series();
   }

   @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return servico.obterLancamentos();
   }

   @GetMapping("/{id}")     //o parametro vem do html
    public SerieDTO obterPorParametro(@PathVariable Long id){

        // é passado para o metodo da classe service que faz a busca no repositorio
        return servico.obterPorId(id);
   }

   @GetMapping("/{id}/temporadas/todas")
public List<EpisodioDTO> obterTodasTeporadas(@PathVariable Long id){

        return servico.obterTodasTemporadas(id);

   }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numero){
        return servico.obterTemporadasPorNumero(id, numero);
    }
}
