--liquibase formatted sql

--changeset filippovma:1 labels:v0.0.1

CREATE TYPE "mkpl_engine_type" AS ENUM (
    'GASOLINE',
    'DIESEL',
    'ELECTRIC',
    'HYBRID',
    'PLUGIN_HYBRID'
);

CREATE TYPE "mkpl_transmission_type" AS ENUM (
    'MANUAL',
    'AUTOMATIC',
    'ROBOTIC',
    'VARIATOR'
);

CREATE TABLE "mkpl_advertisements" (
    "id" BIGSERIAL PRIMARY KEY,
    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    "status" VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    "author_id" BIGINT NOT NULL,

    "brand" VARCHAR(50) NOT NULL,
    "model" VARCHAR(50) NOT NULL,
    "year" INTEGER NOT NULL,

    "engine_type" mkpl_engine_type NOT NULL,
    "transmission" mkpl_transmission_type NOT NULL,

    "mileage" INTEGER NOT NULL,
    "price" DECIMAL(12,2) NOT NULL,
    "location" VARCHAR(100) NOT NULL,

    "title" TEXT,
    "description" TEXT
);

-- Минимальные индексы
CREATE INDEX idx_mkpl_ads_status ON "mkpl_advertisements"("status");
CREATE INDEX idx_mkpl_ads_brand_model ON "mkpl_advertisements"("brand", "model");
CREATE INDEX idx_mkpl_ads_author ON "mkpl_advertisements"("author_id");