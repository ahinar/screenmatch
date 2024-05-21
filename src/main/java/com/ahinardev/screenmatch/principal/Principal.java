package com.ahinardev.screenmatch.principal;

import com.ahinardev.screenmatch.model.DatosEpisodio;
import com.ahinardev.screenmatch.model.DatosSerie;
import com.ahinardev.screenmatch.model.DatosTemporada;
import com.ahinardev.screenmatch.model.Episodio;
import com.ahinardev.screenmatch.service.ConsumoAPI;
import com.ahinardev.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=1392bb39";
    private ConvierteDatos  conversor = new ConvierteDatos();

    public void muestraElMenu(){
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE +nombreSerie.replace(" ","+")+ API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //busca los datos de todas las temporadas
        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoAPI.obtenerDatos(URL_BASE +nombreSerie.replace(" ","+")+ "&Season="+i+ API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporadas);
        }
        //temporadas.forEach(System.out::println);

        // Mostrar solo el título de los episodios por las temporadas

        //for (int i = 0; i < datos.totalDeTemporadas() ; i++) {
        //     List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
        //     for (int j = 0; j < episodiosTemporada.size(); j++) {
        //         System.out.println(episodiosTemporada.get(j).titulo());
        //     }
        // }

        //mejoria usando lambda
        //temporadas.forEach(t ->  t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las informaciones a una lista del tipo datos episodios

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());


//        System.out.println("Top 5 Episodios");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primer filtro (N/A) " +e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("Segundo Ordenación (M>m" +e))
//                .map(e-> e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Tercer filtro Mayúsculas (m>M)"))
//                .limit(5)
//
//                .forEach(System.out::println);

        // convirtiedo los datos a una lista del tipo episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        //episodios.stream().forEach(System.out::println);

        //Búsqueda de episodios a partir de x año
        //System.out.println("Por favor indica el año a partir del cual deseas ver los episodios");
//        var fecha = teclado.nextInt();
//        teclado.nextLine();

//        LocalDate fechaBusqueda = LocalDate.of(fecha, 1,1);
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada "+ e.getTemporada()+
//                                " Episodio "+e.getTitulo()+
//                                " Fecha lanzamiento "+e.getFechaDeLanzamiento().format(dtf)

              //  ));

        //busca episodio por pedazo de titulo

        System.out.println("Por favor escriba el titulo del episodio que desea ver");
        var pedazoTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            System.out.println("episodio encontrado");
            System.out.println(" los datos son: "+episodioBuscado.get());
        }else {
            System.out.println("No se encontro el episodio");
        }

    }
}
