package co.zer.model;

public class Estados {
    public static class Registro {
        public static final Long ABIERTA = 0l;
        public static final Long SALIO_PAGO = 1l;
        public static final Long SALIO_GRACIA = 2l;
        public static final Long SALIO_REPORTADO = 3l;
        public static final Long PAGO_EXTEMPORANEO = 4l;
        public static final Long PREPAGO = 5l;
    }

    public static class PeriodoGracia {
        public final static Long MINUTOS_GRACIA = 15l;
    }

    public static class ParaIngresar {
        public static final Long PUEDE_INGRESAR = 0l;
        public static final Long DEBE_CERRAR_CUENTA = 1l;
        public static final Long YA_SE_ENCUENTRA_EN_ZONA = 2l;
        public static final Long ESTA_EN_PREPAGO = 3l;
        public Long idCuentaDebeCerrar = null;
        public Long respuesta = null;
    }

    public static class Placa {
        public final static int PLACA_CARRO = 1;
        public final static int PLACA_MOTO = 2;
        public final static int NO_ES_PLACA = 0;
    }

    public static class TipoVehiculo {
        public final static String CARRO = "Carro";
        public final static String MOTO = "Moto";
    }

}