-- Insert test accounts
INSERT INTO account (account_no, holder_name, balance, created_at, updated_at)
VALUES ('ACC001', 'John Doe', 1000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC002', 'Jane Smith', 2000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC003', 'Bob Johnson', 500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC004', 'Alice Brown', 1500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ACC005', 'Charlie Wilson', 3000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample transactions
INSERT INTO transaction (trx_reference_no, amount, from_account_no, to_account_no, status, type, description, deleted, created_at, updated_at)
VALUES
    ('TRX001', 100.00, 'ACC001', 'ACC002', 'COMPLETED', 'TRANSFER', 'First transfer', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TRX002', 50.00, 'ACC002', 'ACC003', 'COMPLETED', 'TRANSFER', 'Second transfer', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TRX003', 200.00, null, 'ACC001', 'COMPLETED', 'DEPOSIT', 'Deposit to John', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TRX004', 150.00, 'ACC004', null, 'COMPLETED', 'WITHDRAW', 'ATM withdrawal', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TRX005', 300.00, 'ACC005', 'ACC001', 'COMPLETED', 'TRANSFER', 'Rent payment', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);