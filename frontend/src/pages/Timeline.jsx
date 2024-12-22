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
import React from "react";

// core components
import GridContainer from "conten/components/node_modules/conten/ingresoSistema/node_modules/components/Grid/GridContainer.jsx.js.js";
import GridItem from "conten/components/node_modules/conten/ingresoSistema/node_modules/components/Grid/GridItem.jsx.js.js";
import Heading from "components/Heading/Heading.jsx";
import Timeline from "components/Timeline/Timeline.jsx";
import Card from "conten/ingresoSistema/node_modules/components/Card/Card.jsx.js";
import CardBody from "conten/ingresoSistema/node_modules/components/Card/CardBody.jsx.js";

import { stories } from "variables/general.jsx";

class TimelinePage extends React.Component {
  render() {
    return (
      <div>
        <Heading title="Timeline" textAlign="center" />
        <GridContainer>
          <GridItem xs={12}>
            <Card plain>
              <CardBody plain>
                <Timeline stories={stories} />
              </CardBody>
            </Card>
          </GridItem>
        </GridContainer>
      </div>
    );
  }
}

export default TimelinePage;
