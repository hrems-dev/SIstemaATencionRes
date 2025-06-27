package pe.edu.upeu.sisrestaurant.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class SQLiteIdGenerator implements IdentifierGenerator {
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String entityName = object.getClass().getSimpleName();
        String query = String.format("SELECT MAX(id) FROM %s", entityName.toLowerCase());
        
        try {
            Object result = session.createNativeQuery(query, Long.class).uniqueResult();
            if (result == null) {
                return 1L;
            }
            return ((Long) result) + 1;
        } catch (Exception e) {
            return 1L;
        }
    }
} 