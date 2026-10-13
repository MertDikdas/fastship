ALTER TABLE worker.deployment_executions
    ADD COLUMN service_id UUID;

UPDATE worker.deployment_executions e
SET service_id = d.service_id
FROM public.deployments d
WHERE d.id = e.deployment_id;

ALTER TABLE worker.deployment_executions
    ALTER COLUMN service_id SET NOT NULL;