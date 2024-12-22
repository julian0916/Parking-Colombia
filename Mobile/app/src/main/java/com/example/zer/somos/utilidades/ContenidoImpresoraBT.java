package com.example.zer.somos.utilidades;

import java.util.ArrayList;
import java.util.List;

public class ContenidoImpresoraBT {

    private static final String CODIFICACION = "IBM860";
    // private static final String CODIFICACION = "GBK";
    private List<Contenido> contenidos;

    public ContenidoImpresoraBT() {
        contenidos = new ArrayList<>();
    }

    public List<Contenido> getContenidos() {
        return contenidos;
    }



    public Formato getFormato() {
        return new Formato();
    }

    public void addIzquierda(String texto) {
        addIzquierda(texto, new Formato());
    }

    public void addIzquierda(String texto, Formato formato) {
        try {
            add(texto.getBytes(
                    CODIFICACION),
                    Alineacion.alineadoIzquierda(),
                    formato.getFormato());
        } catch (Exception ex) {
        }
    }

    public void addDerecha(String texto) {
        addDerecha(texto, new Formato());
    }

    public void addDerecha(String texto, Formato formato) {
        try {
            add(texto.getBytes(
                    CODIFICACION),
                    Alineacion.alineadoDerecha(),
                    formato.getFormato());
        } catch (Exception ex) {
        }
    }

    public void addCentro(String texto) {
        addCentro(texto, new Formato());
    }

    public void addCentro(String texto, Formato formato) {
        try {
            add(texto.getBytes(
                    CODIFICACION),
                    Alineacion.alineadoCentro(),
                    formato.getFormato());
        } catch (Exception ex) {
        }
    }

    public void addEnter() {
        addIzquierda("\n");
    }

    private void add(byte[] texto, byte[] alineacion, byte[] formato) {
        Contenido nuevoContenido = new Contenido();
        nuevoContenido.texto = texto;
        nuevoContenido.alineacion = alineacion;
        nuevoContenido.formato = formato;
        contenidos.add(nuevoContenido);
    }

    public byte[] getCentrada() {
        return Alineacion.alineadoCentro();
    }

    public byte[] getIzquierda() {
        return Alineacion.alineadoIzquierda();
    }

    public byte[] getDerecha() {
        return Alineacion.alineadoDerecha();
    }



    private static class Alineacion {
        public static byte[] alineadoDerecha() {
            return new byte[]{0x1B, 'a', 0x02};
        }

        public static byte[] alineadoIzquierda() {
            return new byte[]{0x1B, 'a', 0x00};
        }

        public static byte[] alineadoCentro() {
            return new byte[]{0x1B, 'a', 0x01};
        }

    }

    public class Contenido {
        private byte[] texto;
        private byte[] alineacion;
        private byte[] formato;

        public byte[] getTexto() {
            return texto;
        }

        public byte[] getAlineacion() {
            return alineacion;
        }

        public byte[] getFormato() {
            return formato;
        }
    }

    public class Formato {
        private byte[] mFormat;

        public Formato() {
            // Default:
            mFormat = new byte[]{27, 33, 0};
        }

        public byte[] getFormato() {
            return mFormat;
        }

        public Formato negrita() {
            // Apply bold:
            mFormat[2] = ((byte) (0x8 | mFormat[2]));
            return this;
        }

        public Formato peque() {
            mFormat[2] = ((byte) (0x1 | mFormat[2]));
            return this;
        }

        public Formato alto() {
            mFormat[2] = ((byte) (0x10 | mFormat[2]));
            return this;
        }

        public Formato ancho() {
            mFormat[2] = ((byte) (0x20 | mFormat[2]));
            return this;
        }

        public Formato subrayado() {
            mFormat[2] = ((byte) (0x80 | mFormat[2]));
            return this;
        }
    }
}
