package co.zer.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CuentasDAO implements ICuentasDAO {
    @Override
    public List<String> getCuentas(Connection connection) throws Exception {
        List<String> res = new ArrayList<>();
        final String SQL_SELECT = "SELECT id \n" +
                "FROM cuenta  \n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            rs = pst.executeQuery();
            while (rs.next()) {
                res.add(rs.getString(1));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return res;
    }
}
