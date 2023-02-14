package com.minsait.services;

import com.minsait.models.Examen;
import com.minsait.repositories.ExamenRepository;
import com.minsait.repositories.PreguntasRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepository examenRepository;
    @Mock
    PreguntasRepository preguntasRepository;

    @InjectMocks
    ExamenServiceImpl service;


    @Captor
    ArgumentCaptor<Long> captor;

    @Captor
    ArgumentCaptor<Examen> examenCaptor;

    @Test
    void findExamenPorNombre() {

        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        String nombre = "Quimica";
        Optional<Examen> examen = service.findExamenPorNombre(nombre);
        assertTrue(examen.isPresent());
        assertEquals(nombre, examen.get().getNombre());
    }

    @Test
    void findByExamenPorNombreConPreguntas() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Fisica");

        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        verify(examenRepository, times(1)).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(anyLong());
        verify(preguntasRepository, atMostOnce()).findPreguntasByExamenId(anyLong());

    }

    @Test
    void textException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong()))
                .thenThrow(IllegalArgumentException.class);
        String nombre = "Fisica";
        assertThrows(IllegalArgumentException.class, () -> service.findExamenPorNombreConPreguntas(nombre));

        //  assertEquals(IllegalArgumentException.class, assertThrows(IllegalArgumentException.class,
        //      () -> service.findExamenPorNombreConPreguntas(nombre).getClass()));


    }

    @Test
    void testDoThrow() {
        //Given
        doThrow(RuntimeException.class).when(preguntasRepository).
                savePreguntas(Datos.PREGUNTAS);
        //When

        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        //Then

        assertThrows(RuntimeException.class, () -> service.save(examen));


    }

    @Test
    void doAnswer() {
        //given
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
/*
        when(preguntasRepository.findPreguntasByExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        when(preguntasRepository.findPreguntasByExamenId(2L)).thenReturn(Datos.PREGUNTAS);
*/
        Mockito.doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 1L ? Datos.PREGUNTAS : Collections.EMPTY_LIST;

        }).when(preguntasRepository).findPreguntasByExamenId(anyLong());
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertAll(
                () -> assertEquals(1L, examen.getId(), () -> "El examen no es matematicas"),
                () -> assertFalse(examen.getPreguntas().isEmpty(), () -> "El examen no es Matematicas"),
                () -> assertTrue(examen.getPreguntas().isEmpty(), () -> "El examen es matrematicas")
        );

    }

    @Test
    void testArgumentCaptor() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntasRepository).findPreguntasByExamenId(captor.capture());

        assertEquals(1L, captor.getValue());
    }

    @Test
    void testSaveSinPreguntas() {
        Long nuevoExamenID =Datos.EXAMENES.get(Datos.EXAMENES.size()-1).getId()+1;
        Examen examen =new Examen(nuevoExamenID,"Programacion");
        when(examenRepository.save(examen)).thenReturn(examen);
        Examen examenSave = service.save(examen);
        verify(examenRepository).save(examenCaptor.capture());
        assertTrue(examenCaptor.getValue().getId()==nuevoExamenID);
    }

    @Test
    void testConPreguntas() {
        Long nuevoExamenID =Datos.EXAMENES.get(Datos.EXAMENES.size()-1).getId()+1;
        Examen examen =new Examen(nuevoExamenID,"Programacion");
        when(examenRepository.save(examen)).thenReturn(examen);
        examen.setPreguntas(Datos.PREGUNTAS);
        Examen examenSave = service.save(examen);
        verify(examenRepository).save(examenCaptor.capture());
        assertTrue(examenCaptor.getValue().getId()==nuevoExamenID);

    }
}