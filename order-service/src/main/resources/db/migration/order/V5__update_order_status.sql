DROP CAST IF EXISTS (VARCHAR AS order_status);
DROP CAST IF EXISTS (order_status AS VARCHAR);


ALTER TYPE order_status RENAME TO order_status_old;

CREATE TYPE order_status AS ENUM (
    'CREATED',
    'PAID',
    'REJECTED'
    );

ALTER TABLE order_entity
    ALTER COLUMN status TYPE order_status
        USING status::text::order_status;

DROP TYPE order_status_old;

CREATE CAST (VARCHAR AS order_status)
    WITH INOUT AS IMPLICIT;

CREATE CAST (order_status AS VARCHAR)
    WITH INOUT AS IMPLICIT;