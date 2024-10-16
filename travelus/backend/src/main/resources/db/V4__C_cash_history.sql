CREATE TABLE cash_history (
    cash_history_id     BIGINT NOT NULL PRIMARY KEY,
    foreign_account_id  BIGINT NOT NULL,
    amount              FLOAT(53) NOT NULL,
    transaction_type    ENUM('G', 'P', 'A') NOT NULL,
    transaction_at      DATETIME NOT NULL,
    balance             FLOAT(53),
    store               varchar(32),
    FOREIGN KEY (foreign_account_id) REFERENCES foreign_account(foreign_account_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
