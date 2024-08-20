package com.example.warehouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional // Add this annotation here to wrap the entire operation in a transaction
    public void run(String... args) {
        try {
            // Execute the database reset in separate transactions
            resetDatabase();
        } catch (Exception e) {
            logger.error("Error resetting database", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetDatabase() {
        try {
            dropTables(); // Drop tables in one transaction
            createTables(); // Create tables in another transaction
            createTriggers(); // Create triggers in another transaction
        } catch (Exception e) {
            logger.error("Error resetting database", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dropTables() {
        String dropTablePLSQL =
                "BEGIN " +
                        "  BEGIN " +
                        "    EXECUTE IMMEDIATE 'DROP TABLE outbound_order'; " +
                        "  EXCEPTION " +
                        "    WHEN OTHERS THEN " +
                        "      IF SQLCODE != -942 THEN " + // ORA-00942: table or view does not exist
                        "        RAISE; " +
                        "      END IF; " +
                        "  END; " +
                        "  BEGIN " +
                        "    EXECUTE IMMEDIATE 'DROP TABLE sku'; " +
                        "  EXCEPTION " +
                        "    WHEN OTHERS THEN " +
                        "      IF SQLCODE != -942 THEN " + // ORA-00942: table or view does not exist
                        "        RAISE; " +
                        "      END IF; " +
                        "  END; " +
                        "  BEGIN " +
                        "    EXECUTE IMMEDIATE 'DROP TABLE monitoring_orders'; " +
                        "  EXCEPTION " +
                        "    WHEN OTHERS THEN " +
                        "      IF SQLCODE != -942 THEN " + // ORA-00942: table or view does not exist
                        "        RAISE; " +
                        "      END IF; " +
                        "  END; " +
                        "  BEGIN " +
                        "    EXECUTE IMMEDIATE 'DROP TABLE outbound_cartons'; " +
                        "  EXCEPTION " +
                        "    WHEN OTHERS THEN " +
                        "      IF SQLCODE != -942 THEN " + // ORA-00942: table or view does not exist
                        "        RAISE; " +
                        "      END IF; " +
                        "  END; " +
                        "  BEGIN " +
                        "    EXECUTE IMMEDIATE 'DROP TABLE inbound_orders'; " +
                        "  EXCEPTION " +
                        "    WHEN OTHERS THEN " +
                        "      IF SQLCODE != -942 THEN " + // ORA-00942: table or view does not exist
                        "        RAISE; " +
                        "      END IF; " +
                        "  END; " +
                        "END;";
        try {
            entityManager.createNativeQuery(dropTablePLSQL).executeUpdate();
        } catch (Exception e) {
            logger.error("Error executing drop tables SQL", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTables() {
        String createOrderTable =
                "CREATE TABLE outbound_order (" +
                        "  id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "  order_number VARCHAR2(50), " +
                        "  sku VARCHAR2(50), " +
                        "  quantity NUMBER," +
                        "  status VARCHAR2(50))";

        String createSkuTable =
                "CREATE TABLE sku (" +
                        "  id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "  sku VARCHAR2(50), " +
                        "  name VARCHAR2(50), " +
                        "  description VARCHAR2(255), " +
                        "  quantity NUMBER NOT NULL, " +
                        "  status VARCHAR2(50))";

        String createMonitoringOrdersTable =
                "CREATE TABLE monitoring_orders (" +
                        "  order_number VARCHAR2(50) PRIMARY KEY, " +
                        "  sku VARCHAR2(50), " +
                        "  allocated_quantity NUMBER, " +
                        "  order_status VARCHAR2(50), " +
                        "  last_updated TIMESTAMP)";

        String createOutboundCartons =
                "CREATE TABLE outbound_cartons (" +
                        "  carton_id NUMBER PRIMARY KEY, " +
                        "  order_number VARCHAR2(50), " +
                        "  sku VARCHAR2(50), " +
                        "  quantity NUMBER, " +
                        "  carton_status VARCHAR2(50), " +
                        "  last_updated TIMESTAMP)";

        String createInboundOrders =
                "CREATE TABLE inbound_order (" +
                        "  inbound_order_id NUMBER GENERATED BY DEFAULT AS IDENTITY, " +
                        "  sku VARCHAR2(255) NOT NULL, " +
                        "  quantity NUMBER(10) NOT NULL, " +
                        "  receive_date DATE DEFAULT SYSDATE NOT NULL, " +
                        "  country_of_origin VARCHAR2(255) NOT NULL, " +
                        "  PRIMARY KEY (inbound_order_id))";

        try {
            entityManager.createNativeQuery(createOrderTable).executeUpdate();
            entityManager.createNativeQuery(createSkuTable).executeUpdate();
            entityManager.createNativeQuery(createMonitoringOrdersTable).executeUpdate();
            entityManager.createNativeQuery(createOutboundCartons).executeUpdate();
            entityManager.createNativeQuery(createInboundOrders).executeUpdate();
        } catch (Exception e) {
            logger.error("Error executing create tables SQL", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTriggers() {
        String createTrigger =
                "BEGIN " +
                        "    EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER trg_order_update " +
                        "FOR UPDATE OF status ON outbound_order " +
                        "COMPOUND TRIGGER " +
                        "    l_order_numbers SYS.ODCINUMBERLIST; " +
                        "    BEFORE EACH ROW IS " +
                        "    BEGIN " +
                        "        IF :NEW.status = ''ALLOCATED'' THEN " + // Escaped quotes properly
                        "            l_order_numbers.EXTEND; " +
                        "            l_order_numbers(l_order_numbers.COUNT) := :NEW.order_number; " +
                        "        END IF; " +
                        "    END BEFORE EACH ROW; " +
                        "    AFTER STATEMENT IS " +
                        "    BEGIN " +
                        "        FOR i IN 1 .. l_order_numbers.COUNT LOOP " +
                        "            CARTONIZATION.cartonize_orders(l_order_numbers(i)); " +
                        "        END LOOP; " +
                        "    END AFTER STATEMENT; " +
                        "END trg_order_update'; " +
                        "END;";

        try {
            entityManager.createNativeQuery(createTrigger).executeUpdate();
        } catch (Exception e) {
            logger.error("Error executing create trigger SQL", e);
            throw e;
        }
    }
}
