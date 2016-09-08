CREATE SCHEMA IF NOT EXISTS HOUSING;

CREATE TABLE IF NOT EXISTS HOUSING.RAW_LISTINGS (
  key  VARCHAR(16)   NOT NULL PRIMARY KEY,
  url  VARCHAR(256)  NOT NULL,
  data VARCHAR(4096) NOT NULL
);