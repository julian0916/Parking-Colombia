package co.zer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class BasicService {
    public static final String SCHEMA_CUENTAS = "cuentas";
    public final static String SCHEMA_COMMON = "schemaCommon";

    private static final Logger logger = LoggerFactory.getLogger(BasicService.class);
    private static Map<String, Connection> mapConnection = null;
    protected ObjectMapper objectMapper;
    @PersistenceContext
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Encargado de entablar la conexion con la base de datos usando el componente de hybernate
     */
    protected Connection getConnect(String schemaAccount) {
        //logger.info(PRINT_1_P, "--getConnect");

        if (mapConnection == null) {
            mapConnection = new HashMap<>();
        }

        try {

            Session session = (Session) entityManager.getDelegate();

            //Session session = entityManager.unwrap(org.hibernate.Session.class);
            session.doWork(new Work() {
                @Override
                public void execute(Connection connectionToUse) throws SQLException {

                    connectionToUse.setSchema(schemaAccount);
                    mapConnection.put(schemaAccount, connectionToUse);
                }
            });

        } catch (Exception e) {
            //logger.error(PRINT_2_P, EXCEPTION_TAG, e.getMessage());
        }
        return mapConnection.get(schemaAccount);
    }
}