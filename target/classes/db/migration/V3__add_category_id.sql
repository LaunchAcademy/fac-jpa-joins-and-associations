ALTER TABLE events 
  ADD category_id BIGINT;

CREATE INDEX event_category_id_index ON events(category_id);