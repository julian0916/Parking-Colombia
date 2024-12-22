import PropTypes from "prop-types";
import React from "react";
import withStyles from "@material-ui/core/styles/withStyles";
import CardIcon from "components/Card/CardIcon.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import Card from "components/Card/Card.jsx";
import CampoBadged from "conten/components/CampoBadged";
import MotorcycleIcon from "@material-ui/icons/Motorcycle";
import DriveEtaIcon from "@material-ui/icons/DriveEta";
import AddCircleIcon from "@material-ui/icons/AddCircle";

function TarjetaDashboard({ ...props }) {
  var {
    classCardIconTitle,
    key,
    icono,
    titulo,
    color,
    textoCarro,
    textoMoto,
    textoTotal,
    mostrarCarro,
    mostrarMoto,
    mostrarTodo,
  } = props;
  if (mostrarCarro === undefined) {
    mostrarCarro = true;
  }
  if (mostrarMoto === undefined) {
    mostrarMoto = true;
  }
  if (mostrarTodo === undefined) {
    mostrarTodo = true;
  }
  if (!textoMoto) {
    textoMoto = "-";
  }
  if (!textoCarro) {
    textoCarro = "-";
  }
  if (!textoTotal) {
    textoTotal = "-";
  }
  const ancho = "150px";
  return (
    <Card key={"Card" + key}>
      <CardHeader color={color} icon>
        <CardIcon color={color}>{icono}</CardIcon>
        <span className={classCardIconTitle}>
          <b />
          {titulo}
        </span>
      </CardHeader>
      <CardBody>
        <center>
          {mostrarCarro && (
            <div style={{marginTop:"1px"}}>
              <CampoBadged
                mostrar={true}
                color={color}
                ancho={ancho}
                texto={textoCarro}
                icono={<DriveEtaIcon />}
              />
            </div>
          )}
          {mostrarMoto && (
            <div style={{marginTop:"1px"}}>
              <CampoBadged
                mostrar={true}
                color={color}
                ancho={ancho}
                texto={textoMoto}
                icono={<MotorcycleIcon/>}
              />
            </div>
          )}
          {mostrarTodo && (
            <div style={{marginTop:"1px"}}>
              <CampoBadged
                mostrar={true}
                color={color}
                ancho={ancho}
                texto={
                  <>
                    <b />
                    {textoTotal}
                  </>
                }
                icono={<AddCircleIcon />}
              />
            </div>
          )}
        </center>
      </CardBody>
    </Card>
  );
}

TarjetaDashboard.propTypes = {
  classCardIconTitle: PropTypes.string,
  key: PropTypes.object.isRequired,
  icono: PropTypes.node,
  titulo: PropTypes.string,
  color: PropTypes.string,
  textoCarro: PropTypes.string,
  textoTotal: PropTypes.string,
  textoMoto: PropTypes.string,
  classes: PropTypes.object.isRequired,
  mostrarCarro: PropTypes.boolean,
  mostrarMoto: PropTypes.boolean,
  mostrarTodo: PropTypes.boolean,
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

export default withStyles()(TarjetaDashboard);
