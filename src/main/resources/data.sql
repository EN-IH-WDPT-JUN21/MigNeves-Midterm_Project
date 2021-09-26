-- Passwords: admin
INSERT IGNORE INTO admin (id, role, name, password) VALUES
(1, 'ADMIN', 'Admin', '$2a$15$BAE26pJ4sF0IWDNakDEDc.rUlp.LM4Y6XbZ0pKZ4wh.9/xbgQXXAK');

INSERT IGNORE INTO third_party (id, name, hashed_key) VALUES
(1, 'David Wallace', '-1519330195'),
(2, 'Michael Scott', '1842218853');

--Passwords: 1 - Beesly, 2 - Jimbo, 3 - woof, 4 - beets, 5 - Erin
INSERT IGNORE INTO account_holder (id, name, password, role, age, mailing_address, mailing_address_city, mailing_address_country, mailing_address_postal_code, primary_address, primary_address_city, primary_address_country, primary_address_postal_code) VALUES
(1, 'Jim Halpert', '$2a$15$chAx7rtvNWrP3Fe4eMr/j.EthVnjatMoCS8AELV8wBjkmWPa77UQW', 'ACCOUNT_HOLDER', 42, 'Some Street', 'Philadelphia', 'USA', '2222-123', 'Another Street', 'Scranton', 'USA', '1234-123'),
(2, 'Pam Beesly', '$2a$15$DoGoIrXySg3li3v2MYlztu93D6m7Bat3VjADD.PG8pwJd/4BOmALq', 'ACCOUNT_HOLDER', 40, NULL, NULL, NULL, NULL, 'Another Street', 'Scranton', 'USA', '1234-123'),
(3, 'Ryan Howard', '$2a$15$vDCpA4/HNXfa133VJiAh.epNS9R0woK14cR7vshv48zo5auXm26Wm', 'ACCOUNT_HOLDER', 30, NULL, NULL, NULL , NULL, 'Ryan Castle', 'Scranton', 'USA', '1234-222'),
(4, 'Dwight Schrute', '$2a$15$uS6ZdXVvKlSQ8pJWok0vu.Fsjd7JgO7cG8GTVMEdNZDK6QqTJDBRq', 'ACCOUNT_HOLDER', 51, 'Mose Street', 'Scranton', 'USA', '1234-123', 'Office', 'Scranton' ,'USA', '1234-444'),
(5, 'Pete Miller', '$2a$15$AGnScbfibstXsLD8miSid.JCOOw9LsB616D3p8.E2/Mz9QdrpC0sC', 'ACCOUNT_HOLDER', 22, NULL, NULL, NULL, NULL, 'That Street', 'Scranton', 'USA', '1234-999');

INSERT IGNORE INTO credit_card (id, balance_amount, balance_currency, creation_date, penalty_fee_amount, penalty_fee_currency, secret_key, status, primary_owner, secondary_owner, credit_limit_amount, credit_limit_currency, interest_rate, last_update_date) VALUES
('CC_1', 1000.00, 'EUR', '2000-01-01', 40.00, 'EUR', '$2a$13$RFqms8bfaoMrCjKx9togCulladxRmx6acQn8xWFQ5jKhT.RNqzdKK', 'ACTIVE', 1, 2, 300.00, 'EUR', 0.20, '2021-01-01'),
('CC_2', -50.00, 'EUR', '2021-08-25', 40.00, 'EUR', '$2a$13$GxKif5Y0hdyJ/LYfDV4uueqI3qrkTKcOp2jUYLXCYYuxS.Om8V64a', 'ACTIVE', 3, NULL, 100.00, 'EUR', 0.15, '2021-09-25');

INSERT IGNORE INTO savings (id, balance_amount, balance_currency, creation_date, penalty_fee_amount, penalty_fee_currency, secret_key, status, primary_owner, secondary_owner, interest_rate, last_update_date, minimum_balance_amount, minimum_balance_currency) VALUES
('SA_3', 500.00, 'EUR', '2015-05-26', 40.00, 'EUR', '$2a$13$rz4M0Pc/BURRI.5Tt1rYousEL6KhRIVBvrUpYlT59nYtPGjvpyEIO', 'ACTIVE', 4, NULL, 0.40, '2021-05-01', 200.00, 'EUR');

INSERT IGNORE INTO checking (id, balance_amount, balance_currency, creation_date, penalty_fee_amount, penalty_fee_currency, secret_key, status, primary_owner, secondary_owner, minimum_balance_amount, minimum_balance_currency, monthly_maintenance_fee_amount, monthly_maintenance_fee_currency, last_update_date) VALUES
('CH_4', 800.00, 'EUR', '2019-12-30', 40.00, 'EUR', '$2a$13$W6EQiGylREYKqRfCEWk3Ae35RFZbvcK4qG2vJcbgFxlT25bk4MbaC', 'ACTIVE', 2, 1, 250.00, 'EUR', 12.00, 'EUR', '2021-08-01');

INSERT IGNORE INTO student_checking (id, balance_amount, balance_currency, creation_date, penalty_fee_amount, penalty_fee_currency, secret_key, status, primary_owner, secondary_owner) VALUES
('SC_5', 250.00, 'EUR', '2021-08-29', 40.00, 'EUR', '$2a$13$MurDI.XsAl79i0Ecfp7sZu5k2NY73yc.LH8OTwbs7oKxXRy2xIZF6', 'ACTIVE', 5, NULL);

INSERT IGNORE INTO transaction (id, transfer_amount, transfer_currency, transfer_date, from_account_id, to_account_id) VALUES
(1, 150.00, 'EUR', '2010-01-01T10:00', 'CC_1', 'SA_3'),
(2, 50.00, 'EUR', '2010-01-01T15:00', 'CC_1', 'CH_4'),
(3, 55.00, 'EUR', '2020-12-12T12:12', 'SA_3', 'CC_1'),
(4, 500.00, 'EUR', '2021-09-25T16:00', 'CC_2', 'CH_4'),
(5, 500.00, 'EUR', '2021-09-25T16:02', 'CC_2', 'CC_1'),
(6, 30.00, 'EUR', '2020-09-25T12:12', 'CC_1', 'CH_4');

UPDATE account_seq SET next_val = 6 WHERE next_val < 6;