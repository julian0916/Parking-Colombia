package co.zer.service;

import co.zer.model.EstadoVehiculo;
import co.zer.model.Estados;
import co.zer.model.RegistroCompleto;
import co.zer.model.Zona;
import co.zer.repository.IZonaDAO;
import co.zer.utils.Utilidades;

import java.sql.Connection;
import java.time.*;
import java.util.regex.Pattern;

public class ComunRegistro {


    /**
     * Permite eliminar los caracteres diferentes de letras y números
     * en un texto suministrado
     *
     * @param contenido texto al que se desea limpiar
     * @return texto solo con letras y números
     */
    public static String limpiarContenidosPlaca(String contenido) {
        String regexp = "[^a-zA-Z0-9]*";
        return contenido.replaceAll(regexp, "").toUpperCase();
    }

    /**
     * Permite conocer si una placa suministrada es de carro
     *
     * @param placa texto con la placa formato CCC###
     * @return retorna true si cumple con el formato esperado
     * para la placa de un carro
     */
    public static boolean validarPlacaCarro(String placa) {
        String regexp = "([a-zA-Z]{3}[0-9]{3})|" +
                "([0-9]{3}[a-zA-Z]{3})|" +//caso raro
                "([a-zA-Z]{2}[0-9]{4})|" +//servicio diplomatico
                "([a-zA-Z][0-9]{4})";//transporte
        return Pattern.matches(regexp, placa);
    }

    /**
     * Verifica si la placa suministrada corresponde a la
     * de una moto
     *
     * @param placa texto con el contenido con formato
     *              CCC## o CCC##C
     * @return retorna true si la placa corresponde a la
     * de una moto
     */
    public static boolean validarPlacaMoto(String placa) {
        String regexp = "[a-zA-Z]{3}[0-9]{2}|[a-zA-Z]{3}[0-9]{2}[a-zA-Z]{1}";
        return Pattern.matches(regexp, placa);
    }

    /**
     * Retorna el tipo de placa que corresponde al texto enviado para verificar
     * 0:no es una placa
     * 1: placa de carro
     * 2: placa de moto
     *
     * @param placa texto con la placa que se desea verificar
     * @return retorna 0 si no una placa 1 si es de carro y 2 si es de moto
     */
    public static int getTipoPlaca(String placa) {
        if (validarPlacaCarro(placa)) {
            return Estados.Placa.PLACA_CARRO;
        }
        if (validarPlacaMoto(placa)) {
            return Estados.Placa.PLACA_MOTO;
        }
        return Estados.Placa.NO_ES_PLACA;
    }

    public static void colocarDatosZona(Connection conn, RegistroCompleto reg, Long idZona, IZonaDAO iZonaDAO) throws Exception {
        try {
            Zona zona = iZonaDAO.getZonaPorID(conn, idZona);
            LocalDate date = LocalDate.now();
            long dia = date.getDayOfWeek().getValue();
            reg.setHIniciaZona(zona.getFinSemanaInicia().toLocalTime());
            reg.setHTerminaZona(zona.getFinSemanaTermina().toLocalTime());
            if (dia > 5) {
                reg.setHIniciaZona(zona.getEntreSemanaInicia().toLocalTime());
                reg.setHTerminaZona(zona.getEntreSemanaTermina().toLocalTime());
            }
            reg.setValorH(zona.getValorHoraMoto());
            if (reg.getEsCarro()) {
                reg.setValorH(zona.getValorHoraCarro());
            }
            reg.setMinutosGraciaZona(zona.getMinutosGracia());
            reg.setMinutosParaNuevaGraciaZona(zona.getMinutosParaNuevaGracia());
        } catch (Exception ex) {
            throw new Exception("Los datos de la zona son incorrectos");
        }
    }

    public static String completarCeroIzquierda(int valor, int dig) {
        String val = String.valueOf(valor).trim();
        while (val.length() < dig) {
            val = "0" + val;
        }
        return val;
    }

    public static ZoneId getZonaHoraria() {
        return Utilidades.ZONA_HORARIA;
    }

    public static Long getFechaActualNumero() {
        LocalDate date = LocalDate.now(getZonaHoraria());
        return getFechaActualNumero(date);
    }

    public static Long getFechaActualNumero(LocalDate date) {
        String fechaString = completarCeroIzquierda(date.getYear(), 4);
        fechaString += completarCeroIzquierda(date.getMonthValue(), 2);
        fechaString += completarCeroIzquierda(date.getDayOfMonth(), 2);
        return Long.parseLong(fechaString, 10);
    }

    public static LocalDateTime getFechaHoraActual() {
        LocalDateTime dateTime = LocalDateTime.now(getZonaHoraria());
        return dateTime;
    }

    public static Long minutosTranscurridosIngresoFin(RegistroCompleto reg) throws Exception {
        return Duration.between(
                Utilidades.retirarSegundosNano(reg.getFHIngreso()),
                Utilidades.retirarSegundosNano(reg.getFHEgreso())
        ).toMinutes();
    }

    public static Long minutosACobrar(RegistroCompleto reg,
                                      EstadoVehiculo estadoVehiculo,
                                      LocalDateTime lDTActual) throws Exception {
        LocalDateTime lDTIngreso = reg.getFHIngreso();
        //Controla que la zona este en servicio
        LocalTime lTIniciaServicio = lDTIngreso.toLocalTime().isBefore(reg.getHIniciaZona()) ? reg.getHIniciaZona() : lDTIngreso.toLocalTime();
        //La fecha de cobro no debe superar el mismo día del ingreso
        LocalDateTime lDTCierreCobro = lDTIngreso.with(reg.getHTerminaZona());
        if (lDTActual.isAfter(lDTCierreCobro)) {
            lDTActual = lDTCierreCobro;
        }
        //Controla que la zona no este en servicio
        LocalTime lTTerminaServicio = lDTActual.toLocalTime().isAfter(reg.getHTerminaZona()) ? reg.getHTerminaZona() : lDTActual.toLocalTime();
        LocalDateTime lDTExpiraCredito = null;
        //verifica si aún cuenta con saldo de pagos previos
        if (estadoVehiculo != null) {
            lDTExpiraCredito = estadoVehiculo.getFHExpiraCredito();
            if (lDTExpiraCredito != null && lDTExpiraCredito.toLocalDate().isEqual(lDTActual.toLocalDate())) {
                lTIniciaServicio = lDTExpiraCredito.toLocalTime().isBefore(lTIniciaServicio) ? lTIniciaServicio : lDTExpiraCredito.toLocalTime();
            }
        }
        //cuenta los minutos que realmente se le deben cobrar
        Long minutos = 0l;

        //Se puede usar para pequeñas verificaciones
        //lTIniciaServicio=LocalDateTime.of(2021, 4, 14, 13, 8, 23, 1).toLocalTime();
        //lTTerminaServicio=LocalDateTime.of(2021, 4, 14, 13, 22, 16, 9).toLocalTime();
        if (lTTerminaServicio.isAfter(lTIniciaServicio)) {
            minutos = Duration.between(
                    Utilidades.retirarSegundosNano(lTIniciaServicio),
                    Utilidades.retirarSegundosNano(lTTerminaServicio)).toMinutes();
        }
        return minutos;
    }

    /**
     * determina si se debe aplicar el periodo de gracia a los
     * minutos que se desean cobrar
     *
     * @param minutosACobrar
     * @param estadoVehiculo
     * @return
     * @throws Exception
     */
    public static boolean aplicaPeriodoGracia(Long minutosACobrar,
                                              EstadoVehiculo estadoVehiculo,
                                              Long minutosGracia,
                                              Long minutosParaNuevoPeriodoGracia) throws Exception {
        boolean NO = false;
        boolean SI = true;
        if (minutosACobrar > minutosGracia) {
            return NO;
        }
        if (estadoVehiculo == null) {
            return SI;
        }
        LocalDateTime periodoGracia = Utilidades.retirarSegundosNano(estadoVehiculo.getFHUltimoPeriodoGracia());
        if (periodoGracia == null) {
            return SI;
        }
        if (periodoGracia.plusMinutes(minutosParaNuevoPeriodoGracia).isAfter(Utilidades.retirarSegundosNano(getFechaHoraActual()))) {
            return NO;
        }
        return SI;
    }

    /**
     * Calcula las horas y las fracciones las trata como una hora adicional
     *
     * @param minutosACobrar
     * @return
     */
    public static Long horasACobrar(Long minutosACobrar) {
        final long HORA = 60l;
        Long horas = 0l;
        if (minutosACobrar > 0) {
            horas = Math.floorDiv(minutosACobrar, HORA);
            if (minutosACobrar % HORA != 0) {
                horas += 1;
            }
        }
        return horas;
    }

    /**
     *Calcula el tiempo en que expira el saldo prepago, sumando al existente
     * nuevas horas de ser necesario.
     *
     * @param horasACobrar
     * @param estadoVehiculo
     * @return
     * @throws Exception
     */
    public static LocalDateTime getFHExpiraSaldoPrepago(Long horasACobrar, EstadoVehiculo estadoVehiculo) throws Exception {
        LocalDateTime lDTActual = Utilidades.retirarSegundosNano(getFechaHoraActual());
        if (estadoVehiculo == null) {
            return lDTActual.plusHours(horasACobrar);
        }
        if (estadoVehiculo.getFHExpiraCredito() == null) {
            return lDTActual.plusHours(horasACobrar);
        }
        if (estadoVehiculo.getFHExpiraCredito().isAfter(lDTActual)) {
            return Utilidades.retirarSegundosNano(estadoVehiculo.getFHExpiraCredito()).plusHours(horasACobrar);
        }
        return lDTActual.plusHours(horasACobrar);
    }

    /**
     * Toma la fecha de registro y las horas cobradas para generar la nueva fecha en que
     * @param horasACobrar
     * @param estadoVehiculo
     * @param lDTIniciaRegistro
     * @return
     * @throws Exception
     */
    public static LocalDateTime getFHExpiraSaldo(Long horasACobrar, EstadoVehiculo estadoVehiculo, LocalDateTime lDTIniciaRegistro) throws Exception {
        lDTIniciaRegistro = Utilidades.retirarSegundosNano(lDTIniciaRegistro);
        if (estadoVehiculo == null) {
            return lDTIniciaRegistro.plusHours(horasACobrar);
        }
        if (estadoVehiculo.getFHExpiraCredito() == null) {
            return lDTIniciaRegistro.plusHours(horasACobrar);
        }
        if (estadoVehiculo.getFHExpiraCredito().isAfter(lDTIniciaRegistro.plusHours(horasACobrar))) {
            return Utilidades.retirarSegundosNano(estadoVehiculo.getFHExpiraCredito());
        }
        return lDTIniciaRegistro.plusHours(horasACobrar);
    }

    /**
     * Prmite conocer qué pasos seguir para poder ingresar
     * el vehículo en la zona
     *
     * @param placa
     * @param zona
     * @param estadoVehiculo
     * @return
     * @throws Exception
     */
    public static Estados.ParaIngresar pasosParaIngresar(String placa, Long zona, EstadoVehiculo estadoVehiculo) throws Exception {
        Estados.ParaIngresar resp = new Estados.ParaIngresar();
        if (estadoVehiculo == null) {
            resp.respuesta = Estados.ParaIngresar.PUEDE_INGRESAR;
            return resp;
        }
        if (estadoVehiculo.getEsPrepago()) {
            if (estadoVehiculo.getFHExpiraCredito() == null) {
                resp.respuesta = Estados.ParaIngresar.PUEDE_INGRESAR;
                return resp;
            }
            if (estadoVehiculo.getFHExpiraCredito().isAfter(Utilidades.getLocalDateTime())) {
                resp.respuesta = Estados.ParaIngresar.ESTA_EN_PREPAGO;
                return resp;
            }
            resp.respuesta = Estados.ParaIngresar.PUEDE_INGRESAR;
            return resp;
        }
        if (!estadoVehiculo.getEstaAbierta()) {
            resp.respuesta = Estados.ParaIngresar.PUEDE_INGRESAR;
            return resp;
        }
        if (estadoVehiculo.getZona().equals(zona)) {
            resp.respuesta = Estados.ParaIngresar.YA_SE_ENCUENTRA_EN_ZONA;
            return resp;
        }
        resp.respuesta = Estados.ParaIngresar.DEBE_CERRAR_CUENTA;
        resp.idCuentaDebeCerrar = estadoVehiculo.getIdCuenta();
        return resp;

    }
}
