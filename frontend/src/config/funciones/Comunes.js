import {
  NO_TIENE_AUTORIZACION,
  PERMISO_ACCESO,
} from "config/general/Configuracion";

export function formatoDinero(contenido) {
  if(contenido===null){
    contenido=0;
  }
  if(contenido===undefined){
    contenido=0;
  }
  var local = ("" + contenido).replace(/[^\d]*/gi, "");
  var signo = "";
  try {
    if (("" + contenido).includes("-")) {
      signo = "-";
    }
  } catch (Exception) {}
  var resp = "";
  var contPos = 1;
  var tama = local.length - 1;
  for (var pos = tama; pos > -1; pos--) {
    resp = local.charAt(pos) + resp;
    if (contPos === 3 && pos - 1 > -1) {
      resp = "." + resp;
    }
    if (contPos === 6 && pos - 1 > -1) {
      resp = "'" + resp;
      contPos = 0;
    }
    contPos++;
  }

  return signo + "$" + resp;
}

export function soloNumeros(contenido) {
  var numeros = /[^\d]*/;
  var resultado =
    contenido === undefined || contenido === null ? "0" : contenido;
  resultado = (resultado + "")
    .replace(numeros, "")
    .replace(".", "")
    .replace("'", "");
  return resultado === "" ? 0 : parseInt(resultado, 10);
}

export function sumarDias(fecha, dias) {
  fecha.setDate(fecha.getDate() + dias);
  return fecha;
}

export function formatDate(date, format) {
  const map = {
    mm: ("0" + (date.getMonth() + 1)).slice(-2),
    dd: ("0" + date.getDate()).slice(-2),
    /*yy: date
      .getFullYear()
      .toString()
      .slice(-2),*/
    yyyy: date.getFullYear(),
  };

  return format.replace(/mm|dd|yyyy/gi, (matched) => map[matched]);
}

export function formatDateTime(date, format) {
  const map = {
    mm: ("0" + (date.getMonth() + 1)).slice(-2),
    dd: ("0" + date.getDate()).slice(-2),
    /*yy: date
      .getFullYear()
      .toString()
      .slice(-2),*/
    yyyy: date.getFullYear(),
    HH: ("0" + date.getHours()).slice(-2),
    MM: ("0" + date.getMinutes()).slice(-2),
  };

  return format.replace(/mm|dd|yyyy|HH|MM/gi, (matched) => map[matched]);
}

export function formatDateTimeReporte(dateString) {
  if (dateString === null) {
    return "";
  }
  var date = new Date(dateString);
  var format = "yyyy/mm/dd HH:MM";
  const map = {
    mm: ("0" + (date.getMonth() + 1)).slice(-2),
    dd: ("0" + date.getDate()).slice(-2),
    /*yy: date
      .getFullYear()
      .toString()
      .slice(-2),*/
    yyyy: date.getFullYear(),
    HH: ("0" + date.getHours()).slice(-2),
    MM: ("0" + date.getMinutes()).slice(-2),
  };

  return format.replace(/mm|dd|yyyy|HH|MM/gi, (matched) => map[matched]);
}

/**
 * Retorna las horas y minutos transcurridos entre dos fechas
 *
 * @param fechaHoraInicial
 * @param fechaHoraFinal
 * @return
 */
export function getHorasMinutosTranscurridos(fechaHoraInicial, fechaHoraFinal) {
  try {
    if (fechaHoraInicial === null || fechaHoraInicial == undefined) {
      return "";
    }
    if (fechaHoraFinal === null || fechaHoraFinal == undefined) {
      return "";
    }
    var dateInicial = new Date(fechaHoraInicial);
    var dateFinal = new Date(fechaHoraFinal);
    dateInicial.setSeconds(0);
    dateInicial.setMilliseconds(0);
    dateFinal.setSeconds(0);
    dateFinal.setMilliseconds(0);
    var milisegundos = dateFinal.getTime() - dateInicial.getTime();
    var segundos = milisegundos / 1000; //Math.floor(milisegundos / 1000);
    var minutos = segundos / 60; //Math.floor(segundos / 60);
    var horas = minutos / 60; //Math.floor(minutos / 60);
    var horasEntero = Math.floor(horas);
    var minutosEntero = minutos - horasEntero * 60;

    var contenidoResultado = "";

    if (horasEntero > 0) {
      contenidoResultado = horasEntero + " hora";
    }
    if (horasEntero > 1) {
      contenidoResultado += "s";
    }

    if (minutosEntero > 0) {
      contenidoResultado += " " + minutosEntero + " minuto";
    }
    if (minutosEntero > 1) {
      contenidoResultado += "s";
    }
    contenidoResultado = contenidoResultado.trim();
    //si no se han registrado horas y minutos se ponen los segundos
    if (contenidoResultado === "") {
      if (segundos > 0) {
        contenidoResultado = segundos + " segundo";
      }
      if (segundos > 1) {
        contenidoResultado += "s";
      }
    }

    return contenidoResultado;
  } catch (Exception) {
    return "";
  }
}

export function formatNumberDate(dateTexto) {
  var local = dateTexto.replace(/[^\d]*/gi, "");
  return parseInt(local, 10);
}

export function validarSesion(error) {
  try {
    if (error.type.includes("_ERROR")) {
      if (error.errorContenido.response.status === 401) {
        sessionStorage[PERMISO_ACCESO] = NO_TIENE_AUTORIZACION;
      }
    }
  } catch (excep) {}
}

export function controlarAccceso() {
  return sessionStorage[PERMISO_ACCESO] === NO_TIENE_AUTORIZACION;
}
