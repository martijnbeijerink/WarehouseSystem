CREATE OR REPLACE PACKAGE cartonization AS
    PROCEDURE cartonize_orders(p_order_number IN VARCHAR2);
END cartonization;
/

CREATE OR REPLACE PACKAGE BODY cartonization AS

    PROCEDURE cartonize_orders(p_order_number IN VARCHAR2) IS
        v_carton_id NUMBER;
        v_order_quantity NUMBER;
        v_remaining_quantity NUMBER;
        v_carton_status VARCHAR2(50) := 'PENDING';

CURSOR order_cursor IS
SELECT order_number, sku, quantity
FROM outbound_order
WHERE order_number = p_order_number
  AND status = 'ALLOCATED';

CURSOR carton_cursor IS
SELECT carton_id
FROM outbound_cartons
WHERE carton_status = 'PENDING'
ORDER BY carton_id;

v_sku_quantity NUMBER;
        v_order_number VARCHAR2(50);
        v_sku VARCHAR2(50);

BEGIN
        -- Loop through the orders
FOR order_rec IN order_cursor LOOP
            v_order_number := order_rec.order_number;
            v_sku := order_rec.sku;
            v_order_quantity := order_rec.quantity;

            -- Allocate to cartons
FOR carton_rec IN carton_cursor LOOP
                v_carton_id := carton_rec.carton_id;

                -- Check if the carton can hold the order quantity
SELECT quantity INTO v_sku_quantity
FROM sku
WHERE sku = v_sku;

IF v_sku_quantity >= v_order_quantity THEN
                    -- Allocate order to carton
                    INSERT INTO outbound_cartons (carton_id, order_number, sku, quantity, carton_status, last_updated)
                    VALUES (v_carton_id, v_order_number, v_sku, v_order_quantity, v_carton_status, SYSDATE);

                    -- Update the status of the order
UPDATE outbound_order
SET status = 'CARTONIZED'
WHERE order_number = v_order_number;

-- Update SKU quantity
UPDATE sku
SET quantity = quantity - v_order_quantity
WHERE sku = v_sku;

EXIT; -- Exit the loop once the order is assigned to a carton
ELSE
                    -- If not enough space, move to the next carton
                    CONTINUE;
END IF;
END LOOP;
END LOOP;

        -- Handle cases where no orders were found
        IF order_cursor%NOTFOUND THEN
            DBMS_OUTPUT.PUT_LINE('No orders found for cartonization: ' || p_order_number);
END IF;

EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('No data found for order: ' || p_order_number);
WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('An error occurred during cartonization: ' || SQLERRM);
END cartonize_orders;

END cartonization;
/
