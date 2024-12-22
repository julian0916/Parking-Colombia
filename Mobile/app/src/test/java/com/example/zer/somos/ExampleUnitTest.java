package com.example.zer.somos;

import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.DatosIngreso;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void validarPlacas() {
        assertTrue(DatosIngreso.validarPlacaCarro("JSD123"));
        assertTrue(DatosIngreso.validarPlacaCarro("123JSD"));
        assertTrue(DatosIngreso.validarPlacaCarro("JS1234"));
        assertTrue(DatosIngreso.validarPlacaCarro("T1234"));
        assertFalse(DatosIngreso.validarPlacaCarro("JSD"));
        assertFalse(DatosIngreso.validarPlacaCarro("JSD12"));
        assertFalse(DatosIngreso.validarPlacaCarro("J12DE"));
        assertFalse(DatosIngreso.validarPlacaCarro("J123"));
        assertFalse(DatosIngreso.validarPlacaCarro("JY123"));
        assertFalse(DatosIngreso.validarPlacaCarro("T12346"));
        assertFalse(DatosIngreso.validarPlacaCarro("TT12346"));
    }

    @Test
    public void pruebaFechaa(){
       /* String fechaInicial="2021-02-24T10:31:02.000";
        String fechaFinal="2021-02-24T12:32:58.000";
        String result =Configuracion.getHorasMinutosTranscurridos(fechaInicial,fechaFinal);
        System.out.println("Tiempo "+result);*/
    }
}