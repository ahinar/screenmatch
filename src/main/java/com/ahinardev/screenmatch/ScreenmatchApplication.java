package com.ahinardev.screenmatch;

import com.ahinardev.screenmatch.model.DatosEpisodio;
import com.ahinardev.screenmatch.model.DatosSerie;
import com.ahinardev.screenmatch.model.DatosTemporadas;
import com.ahinardev.screenmatch.principal.Principal;
import com.ahinardev.screenmatch.repository.SerieRepository;
import com.ahinardev.screenmatch.service.ConsumoAPI;
import com.ahinardev.screenmatch.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication {


	public static void main(String[] args) {

		SpringApplication.run(ScreenmatchApplication.class, args);
	}


}
