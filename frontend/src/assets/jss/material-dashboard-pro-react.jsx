/*!

=========================================================
* Material Dashboard PRO React - v1.7.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-pro-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/

// ##############################
// // // Function that converts from hex color to rgb color
// // // Example: input = #9c27b0 => output = 156, 39, 176
// // // Example: input = 9c27b0 => output = 156, 39, 176
// // // Example: input = #999 => output = 153, 153, 153
// // // Example: input = 999 => output = 153, 153, 153
// #############################
const hexToRgb = (input) => {
  input = input + "";
  input = input.replace("#", "");
  let hexRegex = /[0-9A-Fa-f]/g;
  if (!hexRegex.test(input) || (input.length !== 3 && input.length !== 6)) {
    throw new Error("input is not a valid hex color.");
  }
  if (input.length === 3) {
    let first = input[0];
    let second = input[1];
    let last = input[2];
    input = first + first + second + second + last + last;
  }
  input = input.toUpperCase(input);
  let first = input[0] + input[1];
  let second = input[2] + input[3];
  let last = input[4] + input[5];
  return (
    parseInt(first, 16) +
    ", " +
    parseInt(second, 16) +
    ", " +
    parseInt(last, 16)
  );
};

// ##############################
// // // Variables - Styles that are used on more than one component
// #############################

const drawerWidth = 260;

const drawerMiniWidth = 80;

const transition = {
  transition: "all 0.33s cubic-bezier(0.685, 0.0473, 0.346, 1)",
};

const containerFluid = {
  paddingRight: "15px",
  paddingLeft: "15px",
  marginRight: "auto",
  marginLeft: "auto",
  "&:before,&:after": {
    display: "table",
    content: '" "',
  },
  "&:after": {
    clear: "both",
  },
};

const container = {
  paddingRight: "15px",
  paddingLeft: "15px",
  marginRight: "auto",
  marginLeft: "auto",
  "@media (min-width: 768px)": {
    width: "750px",
  },
  "@media (min-width: 992px)": {
    width: "970px",
  },
  "@media (min-width: 1200px)": {
    width: "1170px",
  },
  "&:before,&:after": {
    display: "table",
    content: '" "',
  },
  "&:after": {
    clear: "both",
  },
};

const defaultFont = {
  fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
  fontWeight: "300",
  lineHeight: "1.5em",
};

//const primaryColor = ["#adaa09", "#bcca15", "#9fa703", "#c0af1e", "#9fa000"];

/*const primaryColor = [
  "#162E46",
  "#4dd0e1",
  "#26c6da",
  "#00bcd4",
  "#00acc1",
  "#0097a7",
  "#00838f",
  "#006064",
];

const successColor = [
  "#9575cd",
  "#7e57c2",
  "#673ab7",
  "#5e35b1",
  "#512da8",
  "#4527a0",
  "#311b92"
]

const infoColor = [
  "#00acc1",
  "#26c6da",
  "#00acc1",
  "#00d3ee",
  "#0097a7",
  "#c4e3f3",
  "#d9edf7",
];

const warningColor = [
  "#ff9800",
  "#ffa726",
  "#fb8c00",
  "#ffa21a",
  "#f57c00",
  "#faf2cc",
  "#fcf8e3",
];

const dangerColor = [
  "#f44336",
  "#ef5350",
  "#e53935",
  "#f55a4e",
  "#d32f2f",
  "#ebcccc",
  "#f2dede",
];

const greenColor = [
  "#81c784",
  "#66bb6a",
  "#4caf50",
  "#43a047",
  "#388e3c",
  "#2e7d32",
  "#1b5e20",
];
*/

const somos_color_verde = "#79A667";
const somos_color_amarillo = "#EBBC44";
const somos_color_rojo = "#DF3241";
const somos_color_azul_claro = "#76AFA1";
const somos_color_azul_oscuro = "#162F4A";

const controlarRango = (valor) => {
  if (valor < 0) {
    return controlarRango(255 + valor);
  }
  if (valor > 255) {
    return controlarRango(255 - valor);
  }
  return valor;
};

const completarCeros = (valor, limite) => {
  var local = (valor + "").trim();
  while (local.length < limite) {
    local = "0" + local;
  }
  return local;
};

var rgbToHex = function(rgb) {
  var hex = Number(rgb).toString(16);
  if (hex.length < 2) {
    hex = "0" + hex;
  }
  return hex;
};

function hacerMasClaro(colorRGB, incremento) {
  var color = Number((colorRGB + "").trim());
  color += incremento;
  if (color > 255) {
    color = 255;
  }
  return color;
}

function reducirColor(color, cambio) {
  var colores = hexToRgb(color).split(",");

  //cambio de color vertical
  var result =  
    "#" +
    completarCeros(rgbToHex(hacerMasClaro(colores[0], cambio)), 2) +
    completarCeros(rgbToHex((colores[1])), 2) +
    completarCeros(rgbToHex(colores[2]), 2);

  return result;
  
  //cambio de color diagonal
  /*var result =
    "#" +
    completarCeros(rgbToHex(hacerMasClaro(colores[0], cambio)), 2) +
    completarCeros(rgbToHex(hacerMasClaro(colores[1], cambio)), 2) +
    completarCeros(rgbToHex(hacerMasClaro(colores[2], cambio)), 2);

  return result;
  */
  /*
  var colorInt = parseInt(
    completarCeros(colores[0], 3) +
      completarCeros(colores[1], 3) +
      completarCeros(colores[2], 3)
  );
  colorInt += parseInt(cambio+"00"+cambio+"00"+cambio,10);
  colorInt = completarCeros(colorInt + "", 9);
  var result =
    "#" +
    rgbToHex(colorInt.substring(0, 3)) +
    rgbToHex(colorInt.substring(3, 6)) +
    rgbToHex(colorInt.substring(6));
  return result;*/
}
//-------------------------------------------
//-------------------------------------------
var pasoReduccion = 10;
const greenColor = [
  somos_color_verde,
  reducirColor(somos_color_verde, pasoReduccion),
  reducirColor(somos_color_verde, pasoReduccion * 2),
  reducirColor(somos_color_verde, pasoReduccion * 3),
  reducirColor(somos_color_verde, pasoReduccion * 4),
  reducirColor(somos_color_verde, pasoReduccion * 5),
  reducirColor(somos_color_verde, pasoReduccion * 6),
  reducirColor(somos_color_verde, pasoReduccion * 7),
];

const primaryColor = [
  somos_color_amarillo,
  reducirColor(somos_color_amarillo, pasoReduccion),
  reducirColor(somos_color_amarillo, pasoReduccion * 2),
  reducirColor(somos_color_amarillo, pasoReduccion * 3),
  reducirColor(somos_color_amarillo, pasoReduccion * 4),
  reducirColor(somos_color_amarillo, pasoReduccion * 5),
  reducirColor(somos_color_amarillo, pasoReduccion * 6),
  reducirColor(somos_color_amarillo, pasoReduccion * 7),
];

const successColor = [
  somos_color_azul_oscuro,
  reducirColor(somos_color_azul_oscuro, pasoReduccion),
  reducirColor(somos_color_azul_oscuro, pasoReduccion * 2),
  reducirColor(somos_color_azul_oscuro, pasoReduccion * 3),
  reducirColor(somos_color_azul_oscuro, pasoReduccion * 4),
  reducirColor(somos_color_azul_oscuro, pasoReduccion * 5),
  reducirColor(somos_color_azul_oscuro, pasoReduccion * 6),
  reducirColor(somos_color_azul_oscuro, pasoReduccion * 7),
];

const infoColor = [
  somos_color_azul_claro,
  reducirColor(somos_color_azul_claro, pasoReduccion),
  reducirColor(somos_color_azul_claro, pasoReduccion * 2),
  reducirColor(somos_color_azul_claro, pasoReduccion * 3),
  reducirColor(somos_color_azul_claro, pasoReduccion * 4),
  reducirColor(somos_color_azul_claro, pasoReduccion * 5),
  reducirColor(somos_color_azul_claro, pasoReduccion * 6),
  reducirColor(somos_color_azul_claro, pasoReduccion * 7),
];

const dangerColor = [
  somos_color_rojo,
  reducirColor(somos_color_rojo, pasoReduccion),
  reducirColor(somos_color_rojo, pasoReduccion * 2),
  reducirColor(somos_color_rojo, pasoReduccion * 3),
  reducirColor(somos_color_rojo, pasoReduccion * 4),
  reducirColor(somos_color_rojo, pasoReduccion * 5),
  reducirColor(somos_color_rojo, pasoReduccion * 6),
  reducirColor(somos_color_rojo, pasoReduccion * 7),
];

const warningColor = [
  "#ff9800",
  reducirColor("#ff9800", pasoReduccion),
  reducirColor("#ff9800", pasoReduccion * 2),
  reducirColor("#ff9800", pasoReduccion * 3),
  reducirColor("#ff9800", pasoReduccion * 4),
  reducirColor("#ff9800", pasoReduccion * 5),
  reducirColor("#ff9800", pasoReduccion * 6),
  reducirColor("#ff9800", pasoReduccion * 7),
];

const roseColor = [
  "#e91e63",
  reducirColor("#e91e63", pasoReduccion),
  reducirColor("#e91e63", pasoReduccion * 2),
  reducirColor("#e91e63", pasoReduccion * 3),
  reducirColor("#e91e63", pasoReduccion * 4),
  reducirColor("#e91e63", pasoReduccion * 5),
  reducirColor("#e91e63", pasoReduccion * 6),
  reducirColor("#e91e63", pasoReduccion * 7),
];

const grayColor = [
  "#999",
  "#777",
  "#3C4858",
  "#AAAAAA",
  "#D2D2D2",
  "#DDD",
  "#555555",
  "#333",
  "#eee",
  "#ccc",
  "#e4e4e4",
  "#E5E5E5",
  "#f9f9f9",
  "#f5f5f5",
  "#495057",
  "#e7e7e7",
  "#212121",
  "#c8c8c8",
  "#505050",
  "#777777",
  "#969696",
  "#c8c8c8",
];

const blackColor = "#000";
const whiteColor = "#FFF";
const twitterColor = "#55acee";
const facebookColor = "#3b5998";
const googleColor = "#dd4b39";
const linkedinColor = "#0976b4";
const pinterestColor = "#cc2127";
const youtubeColor = "#e52d27";
const tumblrColor = "#35465c";
const behanceColor = "#1769ff";
const dribbbleColor = "#ea4c89";
const redditColor = "#ff4500";

const boxShadow = {
  boxShadow:
    "0 10px 30px -12px rgba(" +
    hexToRgb(blackColor) +
    ", 0.42), 0 4px 25px 0px rgba(" +
    hexToRgb(blackColor) +
    ", 0.12), 0 8px 10px -5px rgba(" +
    hexToRgb(blackColor) +
    ", 0.2)",
};

const primaryBoxShadow = {
  boxShadow:
    "0 4px 20px 0 rgba(" +
    hexToRgb(blackColor) +
    ",.14), 0 7px 10px -5px rgba(" +
    hexToRgb(primaryColor[0]) +
    ",.4)",
};
const infoBoxShadow = {
  boxShadow:
    "0 4px 20px 0 rgba(" +
    hexToRgb(blackColor) +
    ",.14), 0 7px 10px -5px rgba(" +
    hexToRgb(infoColor[0]) +
    ",.4)",
};
const successBoxShadow = {
  boxShadow:
    "0 4px 20px 0 rgba(" +
    hexToRgb(blackColor) +
    ",.14), 0 7px 10px -5px rgba(" +
    hexToRgb(successColor[0]) +
    ",.4)",
};
const warningBoxShadow = {
  boxShadow:
    "0 4px 20px 0 rgba(" +
    hexToRgb(blackColor) +
    ",.14), 0 7px 10px -5px rgba(" +
    hexToRgb(warningColor[0]) +
    ",.4)",
};
const dangerBoxShadow = {
  boxShadow:
    "0 4px 20px 0 rgba(" +
    hexToRgb(blackColor) +
    ",.14), 0 7px 10px -5px rgba(" +
    hexToRgb(dangerColor[0]) +
    ",.4)",
};
const roseBoxShadow = {
  boxShadow:
    "0 4px 20px 0 rgba(" +
    hexToRgb(blackColor) +
    ",.14), 0 7px 10px -5px rgba(" +
    hexToRgb(roseColor[0]) +
    ",.4)",
};
const greenBoxShadow = {
  boxShadow:
    "0 4px 20px 0 rgba(" +
    hexToRgb(blackColor) +
    ",.14), 0 7px 10px -5px rgba(" +
    hexToRgb(greenColor[0]) +
    ",.4)",
};
const posColor1 = 0;
const posColor2 = 1;

const warningCardHeader = {
  background:
    "linear-gradient(60deg, " +
    warningColor[posColor1] +
    ", " +
    warningColor[posColor2] +
    ")",
  ...warningBoxShadow,
};

const greenCardHeader = {
  background:
    "linear-gradient(60deg, " +
    greenColor[posColor1] +
    ", " +
    greenColor[posColor2] +
    ")",
  ...greenBoxShadow,
};
const successCardHeader = {
  background:
    "linear-gradient(60deg, " +
    successColor[posColor1] +
    ", " +
    successColor[posColor2] +
    ")",
  ...successBoxShadow,
};
const dangerCardHeader = {
  background:
    "linear-gradient(60deg, " +
    dangerColor[posColor1] +
    ", " +
    dangerColor[posColor2] +
    ")",
  ...dangerBoxShadow,
};
const infoCardHeader = {
  background:
    "linear-gradient(60deg, " +
    infoColor[posColor1] +
    ", " +
    infoColor[posColor2] +
    ")",
  ...infoBoxShadow,
};
const primaryCardHeader = {
  background:
    "linear-gradient(60deg, " +
    primaryColor[posColor1] +
    ", " +
    primaryColor[posColor2] +
    ")",
  ...primaryBoxShadow,
};
const roseCardHeader = {
  background:
    "linear-gradient(60deg, " +
    roseColor[posColor1] +
    ", " +
    roseColor[posColor2] +
    ")",
  ...roseBoxShadow,
};
//----
const warningCardHeader1 = {
  background:
    "linear-gradient(60deg, " +
    warningColor[posColor2] +
    ", " +
    warningColor[posColor1] +
    ")",
  ...warningBoxShadow,
};

const greenCardHeader1 = {
  background:
    "linear-gradient(60deg, " +
    greenColor[posColor2] +
    ", " +
    greenColor[posColor1] +
    ")",
  ...greenBoxShadow,
};
const successCardHeader1 = {
  background:
    "linear-gradient(60deg, " +
    successColor[posColor2] +
    ", " +
    successColor[posColor1] +
    ")",
  ...successBoxShadow,
};
const dangerCardHeader1 = {
  background:
    "linear-gradient(60deg, " +
    dangerColor[posColor2] +
    ", " +
    dangerColor[posColor1] +
    ")",
  ...dangerBoxShadow,
};
const infoCardHeader1 = {
  background:
    "linear-gradient(60deg, " +
    infoColor[posColor2] +
    ", " +
    infoColor[posColor1] +
    ")",
  ...infoBoxShadow,
};
const primaryCardHeader1 = {
  background:
    "linear-gradient(60deg, " +
    primaryColor[posColor2] +
    ", " +
    primaryColor[posColor1] +
    ")",
  ...primaryBoxShadow,
};
const roseCardHeader1 = {
  background:
    "linear-gradient(60deg, " +
    roseColor[posColor2] +
    ", " +
    roseColor[posColor1] +
    ")",
  ...roseBoxShadow,
};

const card = {
  display: "inline-block",
  position: "relative",
  width: "100%",
  margin: "25px 0",
  boxShadow: "0 1px 4px 0 rgba(" + hexToRgb(blackColor) + ", 0.14)",
  borderRadius: "6px",
  color: "rgba(" + hexToRgb(blackColor) + ", 0.87)",
  background: whiteColor,
};

const cardActions = {
  margin: "0 20px 10px",
  paddingTop: "10px",
  borderTop: "1px solid " + grayColor[8],
  height: "auto",
  ...defaultFont,
};

const cardHeader = {
  margin: "-20px 15px 0",
  borderRadius: "3px",
  padding: "15px",
};

const defaultBoxShadow = {
  border: "0",
  borderRadius: "3px",
  boxShadow:
    "0 10px 20px -12px rgba(" +
    hexToRgb(blackColor) +
    ", 0.42), 0 3px 20px 0px rgba(" +
    hexToRgb(blackColor) +
    ", 0.12), 0 8px 10px -5px rgba(" +
    hexToRgb(blackColor) +
    ", 0.2)",
  padding: "10px 0",
  transition: "all 150ms ease 0s",
};

const tooltip = {
  padding: "10px 15px",
  minWidth: "130px",
  color: whiteColor,
  lineHeight: "1.7em",
  background: "rgba(" + hexToRgb(grayColor[6]) + ",0.9)",
  border: "none",
  borderRadius: "3px",
  opacity: "1!important",
  boxShadow:
    "0 8px 10px 1px rgba(" +
    hexToRgb(blackColor) +
    ", 0.14), 0 3px 14px 2px rgba(" +
    hexToRgb(blackColor) +
    ", 0.12), 0 5px 5px -3px rgba(" +
    hexToRgb(blackColor) +
    ", 0.2)",
  maxWidth: "200px",
  textAlign: "center",
  fontFamily: '"Helvetica Neue",Helvetica,Arial,sans-serif',
  fontSize: "12px",
  fontStyle: "normal",
  fontWeight: "400",
  textShadow: "none",
  textTransform: "none",
  letterSpacing: "normal",
  wordBreak: "normal",
  wordSpacing: "normal",
  wordWrap: "normal",
  whiteSpace: "normal",
  lineBreak: "auto",
};

const title = {
  color: grayColor[2],
  textDecoration: "none",
  fontWeight: "300",
  marginTop: "30px",
  marginBottom: "25px",
  minHeight: "32px",
  fontFamily: "'Roboto', 'Helvetica', 'Arial', sans-serif",
  "& small": {
    color: grayColor[1],
    fontSize: "65%",
    fontWeight: "400",
    lineHeight: "1",
  },
};

const cardTitle = {
  ...title,
  marginTop: "0",
  marginBottom: "3px",
  minHeight: "auto",
  "& a": {
    ...title,
    marginTop: ".625rem",
    marginBottom: "0.75rem",
    minHeight: "auto",
  },
};

const cardSubtitle = {
  marginTop: "-.375rem",
};

const cardLink = {
  "& + $cardLink": {
    marginLeft: "1.25rem",
  },
};

const tituloPagina2 = (color) => {
  return Object.assign(
    {
      color: whiteColor,
      fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
      fontSize: "15px",
      fontWeight: "400",
      lineHeight: "1.42857",
      opacity: "1",
      borderRadius: 3,
      marginTop: "0px",
    },
    color
  );
};

const roseTituloPagina2 = tituloPagina2(roseCardHeader1);

const primaryTituloPagina2 = tituloPagina2(primaryCardHeader1);

const dangerTituloPagina2 = tituloPagina2(dangerCardHeader1);

const successTituloPagina2 = tituloPagina2(successCardHeader1);

const warningTituloPagina2 = tituloPagina2(warningCardHeader1);

const infoTituloPagina2 = tituloPagina2(infoCardHeader1);

const greenTituloPagina2 = tituloPagina2(greenCardHeader1);

export {
  roseTituloPagina2,
  primaryTituloPagina2,
  dangerTituloPagina2,
  successTituloPagina2,
  warningTituloPagina2,
  infoTituloPagina2,
  greenTituloPagina2,
  hexToRgb,
  //variables
  drawerWidth,
  drawerMiniWidth,
  transition,
  container,
  containerFluid,
  boxShadow,
  card,
  defaultFont,
  greenColor,
  primaryColor,
  warningColor,
  dangerColor,
  successColor,
  infoColor,
  roseColor,
  grayColor,
  blackColor,
  whiteColor,
  twitterColor,
  facebookColor,
  googleColor,
  linkedinColor,
  pinterestColor,
  youtubeColor,
  tumblrColor,
  behanceColor,
  dribbbleColor,
  redditColor,
  primaryBoxShadow,
  infoBoxShadow,
  successBoxShadow,
  warningBoxShadow,
  dangerBoxShadow,
  roseBoxShadow,
  warningCardHeader,
  successCardHeader,
  dangerCardHeader,
  infoCardHeader,
  primaryCardHeader,
  roseCardHeader,
  greenCardHeader,
  warningCardHeader1,
  successCardHeader1,
  dangerCardHeader1,
  infoCardHeader1,
  primaryCardHeader1,
  roseCardHeader1,
  greenCardHeader1,
  cardActions,
  cardHeader,
  defaultBoxShadow,
  tooltip,
  title,
  cardTitle,
  cardSubtitle,
  cardLink,
};
