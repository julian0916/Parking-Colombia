import withStyles from "@material-ui/core/styles/withStyles";
import DriveEtaIcon from "@material-ui/icons/DriveEta";
import MotorcycleIcon from "@material-ui/icons/Motorcycle";
import PieChartIcon from "@material-ui/icons/PieChart";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import { FILA_BLANCA, FILA_GRIS } from "config/general/Configuracion";
import PropTypes from "prop-types";
import React from "react";
import ChartistGraph from "react-chartist";
import { pieChart } from "variables/charts.jsx";
import TituloPagina2 from "conten/components/TituloPagina2";
import {
  grayColor,
  roseColor,
  primaryColor,
  infoColor,
  successColor,
  warningColor,
  dangerColor,
  whiteColor,
  blackColor,
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
  hexToRgb,
} from "assets/jss/material-dashboard-pro-react.jsx";
function pintarFila(key, color, colorTexto, campo1, campo2, campo3, campo4) {
  if (color == undefined) {
    color = FILA_BLANCA;
  }
  if (colorTexto == undefined) {
    colorTexto = FILA_BLANCA;
  }
  return (
    <>
      <GridItem
        key={"campo1" + key}
        xs={12}
        sm={6}
        md={3}
        style={{ backgroundColor: color ,color:colorTexto}}
      >
        {campo1}
      </GridItem>
      <GridItem
        key={"campo2" + key}
        xs={12}
        sm={6}
        md={3}
        style={{ backgroundColor: color ,color:colorTexto}}
      >
        {campo2}
      </GridItem>
      <GridItem
        key={"campo3" + key}
        xs={12}
        sm={6}
        md={3}
        style={{ backgroundColor: color ,color:colorTexto}}
      >
        {campo3}
      </GridItem>
      <GridItem
        key={"campo4" + key}
        xs={12}
        sm={6}
        md={3}
        style={{ backgroundColor: color ,color:colorTexto}}
      >
        {campo4}
      </GridItem>
    </>
  );
}

function ocupacionDescripcion(element, campo) {
  var capacidad = 0;
  if (element["Capacidad" + campo] !== undefined) {
    capacidad = element["Capacidad" + campo];
  }

  var actual = element[campo];
  if (actual === undefined) {
    actual = 0;
  }

  var disponible = capacidad - actual;
  if (disponible < 0) {
    disponible = "Sobrecupo";
  }
  var resp = (
    <>
      {"Ocupado: "}
      {actual}
      <br />
      {" Disponible: "}
      {disponible}
    </>
  );
  return resp;
}

function getDataForPieVehiculo(element, campo) {
  const CAPACIDAD_VEHICULO = "Capacidad" + campo;
  var vehiculo = 0;
  var disponible = 0;
  if (element !== undefined) {
    if (element[campo] !== undefined) {
      vehiculo = element[campo];
    }
    if (element[CAPACIDAD_VEHICULO] !== undefined) {
      disponible = element[CAPACIDAD_VEHICULO];
    }
  }
  /*if (vehiculo > disponible || disponible === 0) {
    disponible = vehiculo;
  }*/
  
  if (disponible === 0 && vehiculo>0) {
    disponible = vehiculo;
  }

  if (disponible === 0 && vehiculo<1) {
    disponible = 1;
  }

  var vehiculoPorcentaje = parseInt(
    ((vehiculo / disponible) * 100).toFixed(0),
    10
  );
  var disponiblePorcentaje = 100 - vehiculoPorcentaje;
  var data = {
    labels: [disponiblePorcentaje + "%", vehiculoPorcentaje + "%"],
    series: [disponiblePorcentaje, vehiculoPorcentaje],
    titulo: (
      <>
        {vehiculo} {campo + "s"} {vehiculoPorcentaje}
        {"% "}
        <br />
        {disponible}
        {" Capacidad: "}
        {disponiblePorcentaje}
        {"% disponible"}
      </>
    ),
  };
  return data;
}

function getDataForPie(element) {
  const MOTO = "Moto";
  const CARRO = "Carro";
  const CAPACIDAD_MOTO = "CapacidadMoto";
  const CAPACIDAD_CARRO = "CapacidadCarro";
  var motos = 0;
  var carros = 0;
  var disponible = 0;
  if (element !== undefined) {
    if (element[MOTO] !== undefined) {
      motos = element[MOTO];
    }
    if (element[CARRO] !== undefined) {
      carros = element[CARRO];
    }
    if (element[CAPACIDAD_MOTO] !== undefined) {
      disponible += element[CAPACIDAD_MOTO];
    }
    if (element[CAPACIDAD_CARRO] !== undefined) {
      disponible += element[CAPACIDAD_CARRO];
    }
  }
  //if (motos + carros > disponible || disponible === 0) {
  //  disponible = motos + carros;
  //}

  if (disponible === 0) {
    disponible = motos + carros;
  }

  var motosPorcentaje = parseInt(((motos / disponible) * 100).toFixed(0), 10);
  var carrosPorcentaje = parseInt(((carros / disponible) * 100).toFixed(0), 10);
  var disponiblePorcentaje = 100 - (motosPorcentaje + carrosPorcentaje);
  var data = {
    labels: [
      motosPorcentaje + "%",
      carrosPorcentaje + "%",
      disponiblePorcentaje + "%",
    ],
    series: [motosPorcentaje, carrosPorcentaje, disponiblePorcentaje],
    titulo: (
      <>
        {carros}
        {" Carros: "}
        {carrosPorcentaje}
        {"% "}
        <br />
        {motos}
        {" Motos: "}
        {motosPorcentaje}
        {"% "}
        <br />
        {disponible}
        {" Capacidad: "}
        {disponiblePorcentaje}
        {"% disponible"}
      </>
    ),
  };
  return data;
}

function hacerTorta(data, info) {
  if (info === undefined) {
    info = "";
  }
  return (
    <>
      <ChartistGraph
        className="ct-chart"
        data={data}
        type="Pie"
        options={pieChart.options}
      />
      <span>{info}</span>
    </>
  );
}

function procesarListado(key, listado) {
  if (listado === undefined) {
    return <></>;
  }
  var resp = [];
  try {
    listado.forEach((element, index) => {
      var dataCarro = getDataForPieVehiculo(element, "Carro");
      var dataMoto = getDataForPieVehiculo(element, "Moto");
      var dataTotal = getDataForPie(element);
      resp.push(
        pintarFila(
          "fila" + index + "-" + key,
          index % 2 === 0 ? FILA_GRIS : FILA_BLANCA,
          blackColor,
          <h3>{element.zona}</h3>,
          hacerTorta(dataCarro, dataCarro.titulo),
          hacerTorta(dataMoto, dataMoto.titulo),
          hacerTorta(dataTotal, dataTotal.titulo)
        )
      );
    });
  } catch (exp) {}
  return resp;
}

function Ocupacion({ ...props }) {
  var { 
    classCardIconTitle, 
    key, 
    icono, 
    titulo, 
    color, 
    contenido,
  fechaControl } = props;
  if (contenido === undefined) {
    contenido = [];
  }
  return (
    <Card key={"Card" + key}>
      <CardHeader color={color} icon>
        <CardIcon color={color}>{icono}{fechaControl}</CardIcon>
        <TituloPagina2
        color={color}
                  texto={titulo}
                />
      </CardHeader>
      <CardBody>
        <center>
          <GridContainer>
            {pintarFila(
              "titulo" + key,
              successColor[0],
              whiteColor,
              <h5>Zona</h5>,
              <DriveEtaIcon />,
              <MotorcycleIcon />,
              <PieChartIcon />
            )}
            {procesarListado(key, contenido)}
          </GridContainer>
        </center>
      </CardBody>
    </Card>
  );
}

Ocupacion.propTypes = {
  contenido: PropTypes.array,
  classCardIconTitle: PropTypes.string,
  key: PropTypes.object.isRequired,
  icono: PropTypes.node,
  titulo: PropTypes.string,
  color: PropTypes.string,
  classes: PropTypes.object.isRequired,
  fechaControl: PropTypes.object.isRequired,
  color: PropTypes.oneOf([
    "primary",
    "warning",
    "danger",
    "success",
    "info",
    "rose",
    "gray",
  ]),
};

export default withStyles()(Ocupacion);
