CREATE ROLE user_name PASSWORD 'password' SUPERUSER CREATEDB INHERIT LOGIN;
ALTER ROLE user_name SET search_path TO amicus,olisuite,public;
