-- Step 1: Drop the foreign key constraint in 'account_book' table
ALTER TABLE account_book
DROP FOREIGN KEY account_book_ibfk_1;

-- Step 1.1: Drop the foreign key constraint in 'cash_history' table
ALTER TABLE cash_history
DROP FOREIGN KEY cash_history_ibfk_1;

-- Step 1.2: Drop any other foreign key constraints involving 'foreign_account_id'
-- Example: ALTER TABLE your_other_table DROP FOREIGN KEY your_foreign_key_name;

-- Step 2: Add a new temporary column for foreign_account_id without AUTO_INCREMENT
ALTER TABLE foreign_account
    ADD COLUMN temp_foreign_account_id BIGINT;

-- Step 3: Copy data from the existing foreign_account_id to the new column
UPDATE foreign_account SET temp_foreign_account_id = foreign_account_id;

-- Step 4: Drop the original foreign_account_id column
ALTER TABLE foreign_account DROP COLUMN foreign_account_id;

-- Step 5: Rename the temporary column to foreign_account_id
ALTER TABLE foreign_account CHANGE COLUMN temp_foreign_account_id foreign_account_id BIGINT NOT NULL;

-- Step 6: Re-add the primary key constraint
ALTER TABLE foreign_account ADD PRIMARY KEY (foreign_account_id);

-- Step 7: Re-add the foreign key constraints
ALTER TABLE account_book
    ADD CONSTRAINT account_book_ibfk_1
        FOREIGN KEY (foreign_account_id) REFERENCES foreign_account(foreign_account_id)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE cash_history
    ADD CONSTRAINT cash_history_ibfk_1
        FOREIGN KEY (foreign_account_id) REFERENCES foreign_account(foreign_account_id)
            ON DELETE CASCADE ON UPDATE CASCADE;

-- If you have more constraints, re-add them here as well
