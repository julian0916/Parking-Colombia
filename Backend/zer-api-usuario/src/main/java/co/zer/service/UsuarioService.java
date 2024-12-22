package co.zer.service;

import co.zer.model.Paginacion;
import co.zer.model.Usuario;
import co.zer.repository.IUsuarioDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UsuarioService extends BasicService {

    public Usuario guardar(String cuenta,IUsuarioDAO iUsuarioDAO, Usuario usuario) throws Exception {
        return iUsuarioDAO.guardar(this.getConnect(cuenta), usuario);
    }

    public Map<String,Object> listarUsuarios(String cuenta,IUsuarioDAO iUsuarioDAO, Paginacion paginacion) throws Exception {
        Map<String,Object> resultado = new HashMap<>();
        resultado.put("usuarios",iUsuarioDAO.listarUsuarios(this.getConnect(cuenta), paginacion));
        resultado.put("paginacion",paginacion);
        return resultado;
    }

    public Map<String,Object> listarDocumentos(String cuenta,IUsuarioDAO iUsuarioDAO) throws Exception {
        Map<String,Object> resultado = new HashMap<>();
        resultado.put("documentos",iUsuarioDAO.listarDocumentos(this.getConnect(SCHEMA_CUENTAS)));
        return resultado;
    }

    public Map<String,Object> listarPerfiles(String cuenta,IUsuarioDAO iUsuarioDAO) throws Exception {
        Map<String,Object> resultado = new HashMap<>();
        resultado.put("perfiles",iUsuarioDAO.listarPerfiles(this.getConnect(SCHEMA_CUENTAS)));
        return resultado;
    }
}
