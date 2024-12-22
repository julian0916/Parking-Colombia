package co.zer.service;

import co.zer.model.Cuenta;
import co.zer.model.Paginacion;
import co.zer.repository.ICuentaDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CuentaService extends BasicService {

    public Cuenta guardar(ICuentaDAO iCuentaDAO, Cuenta cuenta) throws Exception {
        return iCuentaDAO.guardar(this.getConnect(SCHEMA_CUENTAS), cuenta);
    }

    public Map<String,Object> listarCuentas(ICuentaDAO iCuentaDAO, Paginacion paginacion) throws Exception {
        Map<String,Object> resultado = new HashMap<>();
        resultado.put("cuentas",iCuentaDAO.listarCuentas(this.getConnect(SCHEMA_CUENTAS), paginacion));
        resultado.put("paginacion",paginacion);
        return resultado;
    }

    public Map<String,Object> listarCuentasUsuario(ICuentaDAO iCuentaDAO,String usuario) throws Exception {
        Map<String,Object> resultado = new HashMap<>();
        resultado.put("cuentas",iCuentaDAO.listarCuentasUsuario(this.getConnect(SCHEMA_CUENTAS), usuario));
        return resultado;
    }
}
