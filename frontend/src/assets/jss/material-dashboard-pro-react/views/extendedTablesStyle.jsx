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
import buttonGroupStyle from "assets/jss/material-dashboard-pro-react/buttonGroupStyle.jsx";
import customCheckboxRadioSwitch from "assets/jss/material-dashboard-pro-react/customCheckboxRadioSwitch.jsx";
import {
  cardTitle,
  grayColor,
  roseTituloPagina2,
  primaryTituloPagina2,
  dangerTituloPagina2,
  successTituloPagina2,
  warningTituloPagina2,
  greenTituloPagina2,
  infoTituloPagina2
} from "assets/jss/material-dashboard-pro-react.jsx";

const extendedTablesStyle = {
  ...customCheckboxRadioSwitch,
  ...buttonGroupStyle,
  right: {
    textAlign: "right"
  },
  center: {
    textAlign: "center"
  },
  description: {
    maxWidth: "150px"
  },
  actionButton: {
    margin: "0 0 0 5px",
    padding: "5px",
    "& svg,& .fab,& .fas,& .far,& .fal,& .material-icons": {
      marginRight: "0px"
    }
  },
  icon: {
    verticalAlign: "middle",
    width: "17px",
    height: "17px",
    top: "-1px",
    position: "relative"
  },
  imgContainer: {
    width: "120px",
    maxHeight: "160px",
    overflow: "hidden",
    display: "block"
  },
  img: {
    width: "100%",
    height: "auto",
    verticalAlign: "middle",
    border: "0"
  },
  tdName: {
    minWidth: "200px",
    fontWeight: "400",
    fontSize: "1.5em"
  },
  tdNameAnchor: {
    color: grayColor[2]
  },
  tdNameSmall: {
    color: grayColor[0],
    fontSize: "0.75em",
    fontWeight: "300"
  },
  tdNumber: {
    textAlign: "right",
    minWidth: "145px",
    fontWeight: "300",
    fontSize: "1.3em !important"
  },
  tdNumberSmall: {
    marginRight: "3px"
  },
  tdNumberAndButtonGroup: {
    lineHeight: "1 !important"
  },
  positionAbsolute: {
    position: "absolute",
    right: "0",
    top: "0"
  },
  customFont: {
    fontSize: "16px !important"
  },
  actionButtonRound: {
    width: "auto",
    height: "auto",
    minWidth: "auto"
  },
  cardIconTitle: {
    ...cardTitle,
    marginTop: "15px",
    marginBottom: "0px"
  },
  roseTituloPagina2:roseTituloPagina2,
  primaryTituloPagina2:primaryTituloPagina2,
  dangerTituloPagina2:dangerTituloPagina2,
  successTituloPagina2:successTituloPagina2,
  warningTituloPagina2:warningTituloPagina2,
  infoTituloPagina2:infoTituloPagina2,
  greenTituloPagina2:greenTituloPagina2,
};

export default extendedTablesStyle;
