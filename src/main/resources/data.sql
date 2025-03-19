-- Create initial test accounts
INSERT INTO account (account_no, holder_name, balance, created_at, updated_at)
VALUES ('ACC001', 'John Doe', 1000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC002', 'Jane Smith', 2000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC003', 'Bob Johnson', 500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC004', 'Alice Brown', 1500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC005', 'Charlie Wilson', 3000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);