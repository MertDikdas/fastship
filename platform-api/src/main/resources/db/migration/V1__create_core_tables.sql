CREATE TABLE projects (
  id UUID PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  repository_url VARCHAR(1000) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL,

  CONSTRAINT uk_projects_name
      UNIQUE (name)
);


CREATE TABLE deployable_services (
             id UUID PRIMARY KEY,
             project_id UUID NOT NULL,
             name VARCHAR(120) NOT NULL,
             config_path VARCHAR(500) NOT NULL,
             created_at TIMESTAMPTZ NOT NULL,

             CONSTRAINT fk_deployable_services_project
                 FOREIGN KEY (project_id)
                     REFERENCES projects(id),

             CONSTRAINT uk_deployable_services_project_name
                 UNIQUE (project_id, name)
);


CREATE TABLE deployments (
     id UUID PRIMARY KEY,
     service_id UUID NOT NULL,

     status VARCHAR(32) NOT NULL,

     failure_stage VARCHAR(32),
     failure_code VARCHAR(100),
     failure_message VARCHAR(4000),

     created_at TIMESTAMPTZ NOT NULL,
     started_at TIMESTAMPTZ,
     finished_at TIMESTAMPTZ,

     CONSTRAINT fk_deployments_service
         FOREIGN KEY (service_id)
             REFERENCES deployable_services(id)
);


CREATE INDEX idx_deployments_service_created_at
    ON deployments(service_id, created_at DESC);