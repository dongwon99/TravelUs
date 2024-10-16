-- V7__remove_unique_constraint_from_personal_account_id.sql

-- Step 1: Drop the foreign key constraint related to personal_account_id
ALTER TABLE participant
DROP FOREIGN KEY fk_participant_personal_account_id;

-- Step 2: Drop the unique constraint on personal_account_id
ALTER TABLE participant
DROP INDEX u_personal_account_id;

-- Step 3: Recreate the foreign key constraint if needed
ALTER TABLE participant
    ADD CONSTRAINT fk_participant_personal_account_id
        FOREIGN KEY (personal_account_id)
            REFERENCES general_account (general_account_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;