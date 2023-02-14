package com.minsait.services;

import com.minsait.models.Examen;
import com.minsait.repositories.ExamenRepository;
import com.minsait.repositories.PreguntasRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepository examenRepository;
    private PreguntasRepository preguntasRepository;
    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepository.findAll().
                stream().filter(examen -> examen.getNombre().equals(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional=findExamenPorNombre(nombre);
        Examen examen=null;
        if(examenOptional.isPresent()){
            examen=examenOptional.get();
            List<String> preguntas=preguntasRepository.findPreguntasByExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen save(Examen examen) {
        Optional<Examen> examenOptional=Optional.of(examen);
      if(examenOptional.isPresent()){
          if (!examen.getPreguntas().isEmpty()){
              preguntasRepository.savePreguntas(examen.getPreguntas());
          }

          examen= examenRepository.save(examen);
      }
        return examen;
    }
}
