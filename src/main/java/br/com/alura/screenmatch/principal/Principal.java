package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpsodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
//
//        for (int i = 0; i < dados.totalTemporadas(); i++  ){
//            List<DadosEpsodio> episodiosTemporada = temporadas.get(i).epsodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //expressão lambda
        temporadas.forEach(t -> t.epsodios().forEach(e -> System.out.println(e.titulo())) );
//        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico" );
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("N"))
//                .map(n -> n.toUpperCase())
//                .forEach(System.out::println);

        List<DadosEpsodio> dadosEpsodios = temporadas.stream()
                .flatMap(t -> t.epsodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 5 episódios");
        dadosEpsodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpsodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.epsodios().stream()
                .map(d -> new Episodio(t.numero(), d))
                )
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);
        System.out.println("Apartir de que ano você deseja ver os epsódios? ");
        var ano = scanner.nextInt();
        scanner.nextLine();
        LocalDate localDate = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e ->e.getDataLancamento()!= null && e.getDataLancamento().isAfter(localDate))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Eposódio: " + e.getTitulo() +
                                " Data lançamento: " + e.getDataLancamento().format(formatter)
                        )

                 );



    }
}
