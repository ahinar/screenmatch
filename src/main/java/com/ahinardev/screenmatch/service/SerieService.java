package com.ahinardev.screenmatch.service;

import com.ahinardev.screenmatch.dto.EpisodioDTO;
import com.ahinardev.screenmatch.dto.SerieDTO;
import com.ahinardev.screenmatch.model.Categoria;
import com.ahinardev.screenmatch.model.Episodio;
import com.ahinardev.screenmatch.model.Serie;
import com.ahinardev.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obtenerTodasLasSeries() {
        return convierteDatos(repository.findAll());
    }

    public List<SerieDTO> obtenerTop5() {
        return convierteDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }
    public List<SerieDTO> obtenerLanzamientosMasRecientes(){
        return convierteDatos(repository.lanzamientosMasRecientes());
    }
    public List<SerieDTO> convierteDatos(List<Serie> serie){
        return serie.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster(),
                        s.getGenero(), s.getActores(), s.getSinopsis()))
                .collect(Collectors.toList());
    }

    public SerieDTO obtenerPorId(Long id) {
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster(),
                    s.getGenero(), s.getActores(), s.getSinopsis());
        }
        return null;
    }

    public List<EpisodioDTO> obtenerTodasLasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream().map(e-> new EpisodioDTO(e.getTemporada(),e.getTitulo(),e.getNumeroEpisodio())).collect(Collectors.toList());
        }
        return null;
    }


    public List<EpisodioDTO> obtenerTemporadasPorNumero(Long id, Long numeroTemporadas) {
        return repository.obtenerTemporadasPorNumero(id, numeroTemporadas).stream().map(e->new EpisodioDTO(e.getTemporada(),
                e.getTitulo(),e.getNumeroEpisodio())).collect(Collectors.toList());
    }

    public List<SerieDTO> obtenerSeriesPorCategoria(String nombreGenero) {
        Categoria categoria = Categoria.fromEspanol(nombreGenero);
        return convierteDatos(repository.findByGenero(categoria));
    }

    public List<EpisodioDTO> obtenerTopEpisodios(Long id) {
        var serie = repository.findById(id).get();
        return repository.topEpisodiosPorSerie(serie)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(),e.getTitulo(),
                        e.getNumeroEpisodio()))
                .collect(Collectors.toList());
    }
}
