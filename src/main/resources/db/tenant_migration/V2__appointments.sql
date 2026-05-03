-- Cria as tabelas de agendamento no schema do tenant

CREATE TABLE IF NOT EXISTS specialties (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    duration_minutes INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS doctors (
                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    phone VARCHAR(20),
    crm VARCHAR(20) UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    specialty_id UUID NOT NULL REFERENCES specialties(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS slots (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    doctor_id UUID NOT NULL REFERENCES doctors(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS appointments (
                                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_name VARCHAR(200) NOT NULL,
    patient_email VARCHAR(200),
    patient_phone VARCHAR(20),
    notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    cancellation_token VARCHAR(100) UNIQUE,
    slot_id UUID UNIQUE NOT NULL REFERENCES slots(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );