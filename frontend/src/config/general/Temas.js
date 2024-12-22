import { createTheme } from "@material-ui/core/styles";
import {
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
  cardActions,
  cardHeader,
  defaultBoxShadow,
  tooltip,
  title,
  cardTitle,
  cardSubtitle,
  cardLink,
} from "assets/jss/material-dashboard-pro-react";

function getRoot(color) {
  return {
    color: color,
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    fontSize: "14px",
    fontWeight: "400",
    lineHeight: "1.42857",
    opacity: "1",
  };
}

export const PRINCIPAL = createTheme({
  overrides: {
    MuiTableSortLabel: {
      root: {
        color: successColor[0],

        "&$active": {
          color: primaryColor[0],
          "&& $icon": {
            color: primaryColor[0],
          },
        },
        "&:hover": {
          color: primaryColor[0],
          "&& $icon": {
            color: primaryColor[0],
          },
        },
      },
    },
    MuiTypography: {
      root: {
        color: successColor[0],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
      body1: {
        color: successColor[0],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
    },
    MuiInputAdornment: {
      root: {
        color: successColor[0],
      },
    },
    MuiMenuItem: {
      root: {
        color: grayColor[14],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
        "&:hover": {
          //color: successColor[0],
          backgroundColor: primaryColor[0],
        },
      },
    },
    MuiOutlinedInput: {
      root: {
        color: grayColor[14],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
        borderColor: successColor[0],
        "& $notchedOutline": {
          borderColor: successColor[0],
        },
        "&:hover:not($disabled):not($focused):not($error) $notchedOutline": {
          borderColor: successColor[0],
          // Reset on touch devices, it doesn't add specificity
          "@media (hover: none)": {
            borderColor: successColor[0],
          },
        },
        "&$focused $notchedOutline": {
          borderColor: successColor[0],
        },
      },
    },
    MuiFormLabel: {
      root: {
        color: successColor[0],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
    },
    MuiInput: {
      root: {
        color: grayColor[14],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
      underline: {
        "&:before": { borderBottomColor: successColor[0] },
        "&:hover:not($disabled):not($focused):not($error):before": {
          borderBottomColor: successColor[0],
        },
      },
    },
  },
  palette: {
    primary: { 500: successColor[0] },
  },
});

export const CAMPO_CALENDARIO_TEXTO_BLANCO = //getCreateMuiTheme(whiteColor, whiteColor);

createTheme({
  overrides: {
    MuiFormLabel: {
      root: getRoot(whiteColor),
    },
    MuiInputBase: {
      root: getRoot(whiteColor),
      adornedEnd: {
        color: whiteColor,
      },
      adornedStart: {
        color: whiteColor,
      },
    },
    MuiInput: {
      root: getRoot(whiteColor),
      underline: {
        "&:before": { borderBottomColor: whiteColor },
        "&:hover:not($disabled):not($focused):not($error):before": {
          borderBottomColor: whiteColor,
        },
      },
    },
  },
  palette: {
    primary: {
      500: whiteColor,
      main: whiteColor,
      light: whiteColor,
      dark: whiteColor,
    },
    secondary: {
      500: whiteColor,
      main: whiteColor,
    },
  },
});

function getCreateMuiTheme(colorFondo, colorTexto) {
  return createTheme({
    overrides: {
      MuiTableSortLabel: {
        root: {
          color: colorTexto,

          "&$active": {
            color: colorTexto,
            "&& $icon": {
              color: colorTexto,
            },
          },
          "&:hover": {
            color: colorTexto,
            "&& $icon": {
              color: colorTexto,
            },
          },
        },
      },
      MuiTypography: {
        root: {
          color: colorTexto,
          fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
          fontSize: "14px",
          fontWeight: "400",
          lineHeight: "1.42857",
          opacity: "1",
        },
        body1: {
          color: colorTexto,
          fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
          fontSize: "14px",
          fontWeight: "400",
          lineHeight: "1.42857",
          opacity: "1",
        },
      },
      MuiInputAdornment: {
        root: {
          color: colorTexto,
        },
      },
      MuiMenuItem: {
        root: {
          color: colorTexto,
          fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
          fontSize: "14px",
          fontWeight: "400",
          lineHeight: "1.42857",
          opacity: "1",
          "&:hover": {
            color: colorTexto,//aqui
            backgroundColor: colorFondo,
          },
        },
      },
      MuiOutlinedInput: {
        root: {
          color: grayColor[14],
          fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
          fontSize: "14px",
          fontWeight: "400",
          lineHeight: "1.42857",
          opacity: "1",
          borderColor: colorFondo,
          "& $notchedOutline": {
            borderColor: colorFondo,
          },
          "&:hover:not($disabled):not($focused):not($error) $notchedOutline": {
            borderColor: colorFondo,
            // Reset on touch devices, it doesn't add specificity
            "@media (hover: none)": {
              borderColor: colorFondo,
            },
          },
          "&$focused $notchedOutline": {
            borderColor: colorFondo,
          },
        },
      },
      MuiFormLabel: {
        root: {
          color: colorTexto,
          fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
          fontSize: "14px",
          fontWeight: "400",
          lineHeight: "1.42857",
          opacity: "1",
        },
      },
      MuiInputBase: {
        root: getRoot(colorTexto),
        adornedEnd: {
          color: colorTexto,
        },
        adornedStart: {
          color: colorTexto,
        },
      },
      MuiInput: {
        root: {
          color: grayColor[14],
          fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
          fontSize: "14px",
          fontWeight: "400",
          lineHeight: "1.42857",
          opacity: "1",
        },
        underline: {
          "&:before": { borderBottomColor: colorFondo },
          "&:hover:not($disabled):not($focused):not($error):before": {
            borderBottomColor: colorFondo,
          },
        },
      },
    },
    palette: {
      primary: { 500: colorTexto, 
        main: colorTexto,
        light: colorTexto,
        dark: colorTexto,},
      secudary: { 500: colorTexto, 
        main: colorTexto,
        light: colorTexto,
        dark: colorTexto,},
    },
  });
}



export const BUSCAR_TABLA = createTheme({
  overrides: {
    MuiTableSortLabel: {
      root: {
        color: infoColor[0],

        "&$active": {
          color: infoColor[0],
          "&& $icon": {
            color: infoColor[0],
          },
        },
        "&:hover": {
          color: infoColor[0],
          "&& $icon": {
            color: infoColor[0],
          },
        },
      },
    },
    MuiTypography: {
      root: {
        color: infoColor[0],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "16px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
      body1: {
        color: infoColor[0],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "16px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
    },
    MuiInputAdornment: {
      root: {
        color: infoColor[0],
      },
    },
    MuiMenuItem: {
      root: {
        color: grayColor[14],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "16px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
        "&:hover": {
          //color: successColor[0],
          backgroundColor: infoColor[0],
        },
      },
    },
    MuiOutlinedInput: {
      root: {
        color: grayColor[14],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
        borderColor: infoColor[0],
        "& $notchedOutline": {
          borderColor: infoColor[0],
        },
        "&:hover:not($disabled):not($focused):not($error) $notchedOutline": {
          borderColor: infoColor[0],
          // Reset on touch devices, it doesn't add specificity
          "@media (hover: none)": {
            borderColor: infoColor[0],
          },
        },
        "&$focused $notchedOutline": {
          borderColor: infoColor[0],
        },
      },
    },
    MuiFormLabel: {
      root: {
        color: infoColor[0],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
    },
    MuiInput: {
      root: {
        color: grayColor[14],
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
        fontWeight: "400",
        lineHeight: "1.42857",
        opacity: "1",
      },
      underline: {
        "&:before": { borderBottomColor: infoColor[0] },
        "&:hover:not($disabled):not($focused):not($error):before": {
          borderBottomColor: infoColor[0],
        },
      },
    },
  },
  palette: {
    primary: {
      500: infoColor[0],
      main: infoColor[0],
      light: infoColor[0],
      dark: infoColor[0],
    },
    secondary: {
      500: infoColor[0],
      main: infoColor[0],
    },
  },
});
