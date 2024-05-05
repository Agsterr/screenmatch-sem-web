package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpsodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
   private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();

    private  ConverteDados converteDados = new ConverteDados();

   private final String ENDERECO = "https://www.omdbapi.com/?t=";
   private final String API_KEY = "&apikey=7d01bb64";
    public void exibirMenu(){
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();

        var json = consumoApi.obterDados(
        ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);


        DadosSerie dados = converteDados.obterDados(json, DadosSerie.class);
        System.out.println(dados);

             List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);

		}

       // temporadas.forEach(System.out::println);

        for (int i = 0; i < dados.totalTemporadas(); i++  ){
            List<DadosEpsodio> episodiosTemporada = temporadas.get(i).epsodios();
            for (int j = 0; j < episodiosTemporada.size(); j++){
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }
    }
}
