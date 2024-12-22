package com.example.zer.somos.utilidades;

import java.util.List;

public interface IConectorImpresoraBT {
    void errorImprimiendo(Exception ex, List<ContenidoImpresoraBT.Contenido> contenidos);
}
