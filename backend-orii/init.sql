-- init.sql
WHENEVER SQLERROR EXIT SQL.SQLCODE;

-- Definir las variables antes de usarlas
DEFINE usuario = "&1"
DEFINE password = "&2"

ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;

DECLARE
    v_username VARCHAR2(30) := 'c##' || '&&usuario';
    v_password VARCHAR2(30) := '&&password';
BEGIN
    -- Crear el usuario
    EXECUTE IMMEDIATE 'CREATE USER ' || v_username || ' IDENTIFIED BY ' || v_password;
    
    -- Otorgar permisos
    EXECUTE IMMEDIATE 'GRANT CONNECT TO ' || v_username;
    EXECUTE IMMEDIATE 'GRANT RESOURCE TO ' || v_username;
    EXECUTE IMMEDIATE 'GRANT CREATE SESSION TO ' || v_username;
    EXECUTE IMMEDIATE 'GRANT CREATE TABLE TO ' || v_username;
    EXECUTE IMMEDIATE 'GRANT DROP ANY TABLE TO ' || v_username;
    EXECUTE IMMEDIATE 'GRANT CREATE SEQUENCE TO ' || v_username;
    
    DBMS_OUTPUT.PUT_LINE('Usuario ' || v_username || ' creado exitosamente');
END;
/

exit;