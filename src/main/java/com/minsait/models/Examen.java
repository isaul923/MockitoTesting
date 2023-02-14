package com.minsait.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Examen {
    Long id;
    String nombre;
    List<String> preguntas;

    public Examen(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.preguntas = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Examen{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", preguntas=" + preguntas +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Examen examen = (Examen) o;
        return Objects.equals(id, examen.id) && Objects.equals(nombre, examen.nombre) && Objects.equals(preguntas, examen.preguntas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, preguntas);
    }

    public Examen() {
    }
}
