import withStyles from "@material-ui/core/styles/withStyles";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import AccessibilityIcon from "@material-ui/icons/Accessibility";

import React from "react";
import { connect } from "react-redux";

var temporizador = null;
const MILI_SEGUNDOS_REFRESCAR = 1000 * 60 * 5;
class MesaDeAyuda extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textAlertInfo: "Un momento mientras completamos su solicitud ;)",
    };
  }

  componentWillUnmount() {
    if (temporizador) {
      clearInterval(temporizador);
    }
    temporizador = null;
  }

  componentDidMount = () => {
    if (temporizador) {
      clearInterval(temporizador);
      temporizador = null;
    }
    temporizador = setInterval(() => {
      //this.traerDatos();
    }, MILI_SEGUNDOS_REFRESCAR);
  };

  render() {
    const { classes } = this.props;
    const isActivityIndicatorShown = false;
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        <Card color="" >
          <CardHeader color="" icon>
            <CardIcon color="info">
              <AccessibilityIcon />
            </CardIcon>
            <TituloPagina2 texto="Mesa de ayuda" classes={classes}/>
          </CardHeader>
          <CardBody></CardBody>
        </Card>
      </GridItem>
    );
  }
}

const mapStateToProps = () => {
  return {};
};

const mapDispatchToProps = () => {
  return {};
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(MesaDeAyuda)
);
