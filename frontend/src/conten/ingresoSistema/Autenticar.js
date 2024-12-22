/**
 * Retorna un valor aleatorio basado en un máximo dado
 * @param {*} max limite del valor deseado
 */
const getValor = (max) => {
  return parseInt(Math.random() * max, 10);
};

/**
 * Calcula base ^ exp % m de una forma
 * exponente binario
 * @param {*} base el valor de la base
 * @param {*} exp el valor del exponente
 * @param {*} m el valor para el módulo
 */
const moduloPotencia = (base, exp, m) => {
  var result = 1;
  while (exp > 0) {
    if ((exp & 1) > 0) {
      result = (result * base) % m;
    }
    exp >>= 1;
    base = (base * base) % m;
  }
  return result;
};

/**
 * Calcula los valores necesario para diffie
 * @param {*} p
 * @param {*} g
 * @param {*} b
 */
const generarValoresDiff = (p, g, b) => {
  var a = getValor(parseInt(Math.log(p) / Math.log(g), 10) * 10000);
  var A = moduloPotencia(g, a, p);
  var sa = moduloPotencia(b, a, p);

  var hash = require("crypto")
    .createHash("sha256")
    .update(sa + "")
    .digest("hex");
  return { A, hash };
};

/**
 * Genera un token JWT usando RS256
 * @param {*} contenido
 * @param {*} palabraSecreta
 */
const generarWebToken = (contenido, palabraSecreta) => {
  var jwt = require("jsonwebtoken");
  var clavePrivada = {
    key: palabraSecreta,
  };
  var token = jwt.sign(contenido, clavePrivada, {
    algorithm: "RS256",
  });

  return token;
};

const encriptarContenido = (contenido, clave) => {
  let CryptoJS = require("crypto-js");

  var data = contenido;
  var key  = CryptoJS.enc.Latin1.parse(clave.substring(0,32));
  var iv   = CryptoJS.enc.Latin1.parse(clave.substring(32,48));  
  var encrypted = CryptoJS.AES.encrypt(
    data,
    key,
    {iv:iv,mode:CryptoJS.mode.CBC,padding:CryptoJS.pad.ZeroPadding
  });
  var contenUTF8 = encrypted+"";
  var data = [];
  
  for (var i = 0; i < contenUTF8.length; i++){  
      data.push(contenUTF8.charCodeAt(i));
  }
  return data;
};

const encriptarContenido1 = (contenido, clave) => {
  var ciphertext = "";
  var originalText = "";
  try {
    var CryptoJS = require("crypto-js");
    // Encrypt
    ciphertext = CryptoJS.AES.encrypt(
      contenido,
      clave.substring(0, 16)
    ).toString();
    // Decrypt
    var bytes = CryptoJS.AES.decrypt(ciphertext, clave.substring(0, 16));
    originalText = bytes.toString(CryptoJS.enc.Utf8);
  } catch (ex) {
  }
  return ciphertext;
};
/**
 * Módulos que se desea exportar
 */
module.exports = {
  generarValoresDiff,
  generarWebToken,
  encriptarContenido,
};
