CREATE OR REPLACE PACKAGE cartonization AS
    PROCEDURE cartonize_orders(p_order_number IN VARCHAR2);
END cartonization;
/

CREATE OR REPLACE PACKAGE BODY cartonization AS

    PROCEDURE cartonize_orders(p_order_number IN VARCHAR2) IS
        v_carton_id NUMBER;
        v_order_quantity NUMBER;
        v_carton_status VARCHAR2(50) := 'PENDING';

CURSOR order_cursor IS
SELECT order_number, sku, quantity
FROM outbound_order
WHERE order_number = p_order_number AND status = 'ALLOCATED';

v_sku VARCHAR2(50);
        v_order_number VARCHAR2(50);

BEGIN
        -- Log procedure start
INSERT INTO cartonization_log (operation, message)
VALUES ('cartonize_orders', 'Procedure started for order number: ' || p_order_number);

-- Loop through the orders
FOR order_record IN order_cursor LOOP
            v_order_number := order_record.order_number;
            v_sku := order_record.sku;
            v_order_quantity := order_record.quantity;

            -- Fetch a new carton_id from the sequence
SELECT carton_seq.NEXTVAL INTO v_carton_id FROM dual;

-- Create the carton
INSERT INTO outbound_cartons (carton_id, order_number, sku, quantity, carton_status, last_updated)
VALUES (v_carton_id, v_order_number, v_sku, v_order_quantity, v_carton_status, SYSDATE);

-- Update the status of the order to CARTONIZED
UPDATE outbound_order
SET status = 'CARTONIZED'
WHERE order_number = v_order_number;
END LOOP;

        -- Handle cases where no orders were found
        IF order_cursor%NOTFOUND THEN
            DBMS_OUTPUT.PUT_LINE('No orders found for cartonization: ' || p_order_number);
INSERT INTO cartonization_log (operation, message)
VALUES ('cartonize_orders', 'No orders found for cartonization: ' || p_order_number);
END IF;

EXCEPTION
        WHEN NO_DATA_FOUND THEN
            INSERT INTO cartonization_log (operation, message)
            VALUES ('cartonize_orders', 'No data found for order: ' || p_order_number);
            DBMS_OUTPUT.PUT_LINE('No data found for order: ' || p_order_number);
            RAISE;
WHEN OTHERS THEN
            -- Log any exceptions that occur
            DECLARE
v_error_message VARCHAR2(4000);
BEGIN
                v_error_message := 'Error: ' || SQLERRM;
INSERT INTO cartonization_log (operation, message)
VALUES ('cartonize_orders', v_error_message);
-- Re-raise the exception after logging
RAISE;
END;
END cartonize_orders;

END cartonization;
/