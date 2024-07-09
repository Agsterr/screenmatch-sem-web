package br.com.alura.screenmatch.controller;


import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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


}
