package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar séries por titulo
                    5-  Buscar série por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - buscar séries por temporadas
                    9 - Digite o trecho do episodio 
                    10 -Top 5 episódios por séries
                    11 -buscar episódios apartir de uma data            
                    0 - Sair                                 
                    """;

            System.out.println(menu);
             opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();

                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;

                case 6:
                    buscarTop5Series();
                    break;

                case 7:
                    buscarSeriesPorCategoria();
                    break;

                case 8:
                    buscarSeriesPorTemporadas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                     topEpisodiosPorSeries();

                    break;

                case 11:
                 buscarEpisodiosDepoisDeUmaData();


                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }




    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        //salvo banco de dados
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = series.stream().filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.epsodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }else {
            System.out.println("Série não encontrada!!!");
        }
    }

    private void listarSeriesBuscadas(){

        //buscando as series no banco de dados e ja criando a lista com as informações
        series = repositorio.findAll();


        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {

        System.out.println("Escolha uma serie pelo nome: ");
        var nomeDaSerie = leitura.nextLine();
         serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeDaSerie);
        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        }else{
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações apartir de que valor? ");
        var avaliacao = leitura.nextDouble();
   List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao );
        System.out.println("Series encontradas!!!");
        //System.out.println(seriesEncontradas);
       seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + "Avaliação: " + s.getAvaliacao()));

    }

    //derived queries
    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: " + s.getAvaliacao()));


    }

    private void buscarSeriesPorCategoria() {

        System.out.println("Deseja buscar séries de que categoria/gênero? ");
        var nomeGenero = leitura.nextLine();

        //chamar o enum

        Categoria categoria = Categoria.fromPortugues(nomeGenero);

        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria  " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }
    private void buscarSeriesPorTemporadas() {
        System.out.println("digite ate quantas temporadas a série deve ter!!!");
        var totalTemporadas = leitura.nextInt();

        System.out.println("Digite a partir de que avalição !!!");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesPorTemporada = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        seriesPorTemporada.forEach(t -> System.out.println(t.getTitulo() + " quantidade de temporadas: " + t.getTotalTemporadas() + " Avaliação: " + t.getAvaliacao()));
    }
    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episodio para a busca? : ");
        var trechoEpisodio = leitura.nextLine();

       List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
       episodiosEncontrados.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpsodio(), e.getTitulo()));

    }

    private void topEpisodiosPorSeries() {
        buscarSeriePorTitulo();
        if(serieBuscada.isPresent()){
           Serie serie = serieBuscada.get();
           List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
           topEpisodios.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                   e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpsodio(), e.getTitulo(),e.getAvaliacao()));
        }



    }
    private void buscarEpisodiosDepoisDeUmaData() {

        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();

            System.out.println("Digite o ano limite de lançamento");
            var anoDeLancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie,anoDeLancamento);

            episodiosAno.forEach(System.out::println);




        }
    }


}