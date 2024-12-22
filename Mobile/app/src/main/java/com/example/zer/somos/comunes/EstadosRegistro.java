package com.example.zer.somos.comunes;

public class EstadosRegistro {
    public static final Long ABIERTA = 0l;
    public static final Long SALIO_PAGO = 1L;
    public static final Long SALIO_GRACIA = 2L;
    public static final Long SALIO_REPORTADO = 3L;
    public static final Long PAGO_EXTEMPORANEO = 4L;
    public static final Long PREPAGO = 5L;

    public static final String T_ABIERTA = "Abierta";
    public static final String T_SALIO_PAGO = "Salio y pago";
    public static final String T_SALIO_GRACIA = "Gratis";
    public static final String T_SALIO_REPORTADO = "Reportada";
    public static final String T_PAGO_EXTEMPORANEO = "Pago extemporaneo";
    public static final String T_PREPAGO = "Prepago";

    public static String getEstadoNombre(Long estado){
        switch(estado.intValue()){
            case 0:
                return T_ABIERTA;
            case 1:
                return T_SALIO_PAGO;
            case 2:
                return T_SALIO_GRACIA;
            case 3:
                return T_SALIO_REPORTADO;
            case 4:
                return T_PAGO_EXTEMPORANEO;
            default:
                return T_PREPAGO;
        }
    }
}
