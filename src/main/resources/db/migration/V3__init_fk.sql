ALTER TABLE card_entity
ADD CONSTRAINT fk_card_entity_user_entity
FOREIGN KEY (user_id)
REFERENCES user_entity(user_id);

CREATE INDEX idx_card_user
ON card_entity(user_id);