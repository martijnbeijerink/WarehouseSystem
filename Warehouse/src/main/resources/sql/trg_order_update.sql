CREATE OR REPLACE TRIGGER TRG_ORDER_UPDATE
FOR UPDATE OF status ON outbound_order
    COMPOUND TRIGGER

    -- Declare a collection to hold the order numbers to be processed
    TYPE t_order_numbers IS TABLE OF outbound_order.order_number%TYPE;
v_order_numbers t_order_numbers := t_order_numbers();

    BEFORE EACH ROW IS
BEGIN
        -- Only process the order if the status is changing to 'ALLOCATED'
        IF :NEW.status = 'ALLOCATED' THEN
            v_order_numbers.EXTEND;
            v_order_numbers(v_order_numbers.COUNT) := :NEW.order_number;
END IF;
END BEFORE EACH ROW;

    AFTER STATEMENT IS
BEGIN
        -- Loop through the collected order numbers
FOR i IN 1..v_order_numbers.COUNT LOOP
BEGIN
                -- Attempt to log the start of cartonization
INSERT INTO cartonization_log (operation, message)
VALUES ('TRG_ORDER_UPDATE', 'Cartonization started for order: ' || v_order_numbers(i));

-- Call the cartonization procedure
cartonization.cartonize_orders(v_order_numbers(i));

                -- Log successful cartonization
INSERT INTO cartonization_log (operation, message)
VALUES ('TRG_ORDER_UPDATE', 'Cartonization completed for order: ' || v_order_numbers(i));
EXCEPTION
                WHEN OTHERS THEN
                    -- Prepare the error message
                    DECLARE
v_error_message VARCHAR2(200);
BEGIN
                        v_error_message := 'Error: ' || SUBSTR(SQLERRM, 1, 200);

                        -- Log the error message
INSERT INTO cartonization_log (operation, message)
VALUES ('TRG_ORDER_UPDATE', v_error_message);
END;
                    RAISE;
END;
END LOOP;
END AFTER STATEMENT;

END TRG_ORDER_UPDATE;
/
