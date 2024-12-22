import PropTypes from "prop-types";
import React from "react";

function CampoTablaTexarea({ ...props }) {
  const { texto } = props;

  return (
    <pre
      style={{
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: "14px",
      }}
    >
      {texto}
    </pre>
  );
}

CampoTablaTexarea.propTypes = {
  texto: PropTypes.string.isRequired,
};

export default CampoTablaTexarea;
