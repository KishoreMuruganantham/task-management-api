-- Sample data for the Postman end-to-end job.
--
-- The job starts the application first, so Hibernate has already created the
-- schema by the time this file runs. These rows back the customer-policy
-- assertions in TaskSystem.postman_collection.json:
--
--   Health Shield / ONE_YEAR  -> Alice, Bob (the happy path expects exactly two)
--   Health Shield / THREE_YEARS -> Carol (must stay out of the ONE_YEAR filter)

INSERT INTO customer (id, name, email) VALUES
    (1, 'Alice', 'alice@example.com'),
    (2, 'Bob',   'bob@example.com'),
    (3, 'Carol', 'carol@example.com');

INSERT INTO policy (policy_title, tenure, type, customer_id) VALUES
    ('Health Shield', 'ONE_YEAR',    'COMPREHENSIVE', 1),
    ('Travel Guard',  'THREE_YEARS', 'THIRD_PARTY',   1),
    ('Health Shield', 'ONE_YEAR',    'THIRD_PARTY',   2),
    ('Auto Protect',  'FIVE_YEARS',  'COMPREHENSIVE', 2),
    ('Health Shield', 'THREE_YEARS', 'COMPREHENSIVE', 3);
