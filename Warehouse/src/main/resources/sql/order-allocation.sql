CREATE OR REPLACE PROCEDURE allocate_sku_quantity (
    p_sku IN VARCHAR2,
    p_quantity IN NUMBER
) IS
    v_available_quantity NUMBER;
BEGIN
    -- Check if the SKU exists and get the available quantity
SELECT quantity INTO v_available_quantity
FROM sku
WHERE sku = p_sku
    FOR UPDATE;

-- Ensure there's enough quantity available
IF v_available_quantity < p_quantity THEN
        RAISE_APPLICATION_ERROR(-20001, 'Insufficient quantity available for SKU: ' || p_sku);
END IF;

    -- Reduce the available quantity
UPDATE sku
SET quantity = quantity - p_quantity
WHERE sku = p_sku;

-- Insert the allocated quantity as a new line in the sku table
INSERT INTO sku (sku, name, description, quantity, status)
SELECT sku, name, description, p_quantity, 'ALLOCATED'
FROM sku
WHERE sku = p_sku;

COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'SKU not found: ' || p_sku);
WHEN OTHERS THEN
        RAISE;
END allocate_sku_quantity;
