CREATE TABLE deployment_executions (
    deployment_id UUID PRIMARY KEY,
    status VARCHAR(32) NOT NULL,
    started_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ
);

CREATE INDEX idx_deployment_executions_status
    ON deployment_executions(status);