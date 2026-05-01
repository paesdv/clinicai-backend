CREATE TABLE IF NOT EXISTS clinics (
                                       id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   VARCHAR(50) UNIQUE NOT NULL,
    name        VARCHAR(200) NOT NULL,
    email       VARCHAR(200) UNIQUE NOT NULL,
    phone       VARCHAR(20),
    cnpj        VARCHAR(18),
    plan        VARCHAR(20) NOT NULL DEFAULT 'STARTER',
    status      VARCHAR(20) NOT NULL DEFAULT 'TRIAL',
    trial_ends_at TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );