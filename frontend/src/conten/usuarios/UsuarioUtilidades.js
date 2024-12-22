const encriptarContenido = (contenido, clave) => {
  let CryptoJS = require("crypto-js");

  var data = contenido;
  var key = CryptoJS.enc.Latin1.parse(clave.substring(0, 32));
  var iv = CryptoJS.enc.Latin1.parse(clave.substring(32, 48));
  var encrypted = CryptoJS.AES.encrypt(data, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.ZeroPadding,
  });

  return encrypted + "";
};

const dencriptarContenido = (contenUTF8, clave) => {
  let CryptoJS = require("crypto-js");

  var key = CryptoJS.enc.Latin1.parse(clave.substring(0, 32));
  var iv = CryptoJS.enc.Latin1.parse(clave.substring(32, 48));
  var decrypted = CryptoJS.AES.decrypt(contenUTF8, key, {
    iv: iv,
    padding: CryptoJS.pad.ZeroPadding,
  });

  return decrypted.toString(CryptoJS.enc.Utf8) + "";
};

const getHash = (contenido) => {
  var hash = require("crypto")
    .createHash("sha256")
    .update(contenido + "")
    .digest("hex");
  return hash;
};

module.exports = {
    getHash,
    dencriptarContenido,
    encriptarContenido,
  };