


class Categorias extends React.Component {
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
          <div className="categories">
              <h2>Categories</h2>
              <ul>
                  {this.props.categories.map((cat, i) => <CategoryItem key={`category-${i}`} category={cat} />)}
              </ul>
          </div>
        </GridItem>
        
      );
    }
  }