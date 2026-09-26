CREATE TABLE outbox_messages (
     id UUID PRIMARY KEY,
     aggregate_id UUID NOT NULL,

     topic VARCHAR(255) NOT NULL,
     message_key VARCHAR(255) NOT NULL,
     message_type VARCHAR(100) NOT NULL,

     payload TEXT NOT NULL,

     created_at TIMESTAMPTZ NOT NULL,
     published_at TIMESTAMPTZ,

     attempt_count INTEGER NOT NULL DEFAULT 0,
     last_error VARCHAR(2000)
);

CREATE INDEX idx_outbox_unpublished
    ON outbox_messages(created_at)
    WHERE published_at IS NULL;