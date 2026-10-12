ALTER TABLE deployment_executions
    ADD COLUMN worker_id VARCHAR(100),
    ADD COLUMN heartbeat_at TIMESTAMPTZ,
    ADD COLUMN attempt_count INTEGER NOT NULL DEFAULT 1;

-- We already have rows from our previous tests.
UPDATE deployment_executions
SET worker_id = 'legacy',
    heartbeat_at = updated_at
WHERE worker_id IS NULL;

ALTER TABLE deployment_executions
    ALTER COLUMN worker_id SET NOT NULL,
    ALTER COLUMN heartbeat_at SET NOT NULL;

CREATE INDEX idx_deployment_executions_heartbeat
    ON deployment_executions(status, heartbeat_at);