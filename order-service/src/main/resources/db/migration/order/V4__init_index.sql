CREATE INDEX idx_order_user_status
    ON order_entity (user_id, status);

CREATE INDEX idx_order_item_order_id
    ON order_item_entity (order_id);

CREATE INDEX idx_order_item_item_id
    ON order_item_entity (item_id);