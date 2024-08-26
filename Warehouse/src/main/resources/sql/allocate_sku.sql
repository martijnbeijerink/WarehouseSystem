CREATE OR REPLACE PROCEDURE allocate_sku (
    p_order_number IN VARCHAR2
) AS
    v_sku_quantity NUMBER;
    v_order_quantity NUMBER;
    v_sku_code VARCHAR2(50);
    v_sku_name VARCHAR2(50);
    v_sku_description VARCHAR2(255);
BEGIN
    -- Retrieve the order quantity and SKU from the order
    SELECT quantity, sku
    INTO v_order_quantity, v_sku_code
    FROM outbound_order
    WHERE order_number = p_order_number;

    -- Check if sufficient SKU stock is available
    SELECT quantity, name, description
    INTO v_sku_quantity, v_sku_name, v_sku_description
    FROM sku
    WHERE sku = v_sku_code AND status = 'AVAILABLE';

    IF v_sku_quantity >= v_order_quantity THEN
        -- Deduct the quantity from the existing SKU where status is OPEN
        UPDATE sku
        SET quantity = quantity - v_order_quantity
        WHERE sku = v_sku_code AND status = 'AVAILABLE';

        -- Add a new line with the status ALLOCATED
        INSERT INTO sku (sku, name, description, quantity, status)
        VALUES (v_sku_code, v_sku_name, v_sku_description, v_order_quantity, 'ALLOCATED');

        -- Update the order status to ALLOCATED
        UPDATE outbound_order
        SET status = 'ALLOCATED'
        WHERE order_number = p_order_number;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Insufficient SKU quantity for allocation');
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Order or SKU not found');
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'An unexpected error occurred: ' || SQLERRM);
END;