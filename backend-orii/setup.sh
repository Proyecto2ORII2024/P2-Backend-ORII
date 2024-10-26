#!/bin/bash

# Configuración completa del entorno Oracle
export ORACLE_HOME=/opt/oracle/product/21c/dbhome_1  # o 19c según tu versión
export ORACLE_BASE=/opt/oracle
export PATH=$ORACLE_HOME/bin:$PATH
export LD_LIBRARY_PATH=$ORACLE_HOME/lib:$LD_LIBRARY_PATH
export TNS_ADMIN=$ORACLE_HOME/network/admin
export ORACLE_SID=ORCLCDB

# Verificar que las variables de entorno estén definidas
if [ -z "$ORACLE_USER" ] || [ -z "$ORACLE_USER_PWD" ]; then
    echo "Error: ORACLE_USER y ORACLE_USER_PWD deben estar definidas"
    exit 1
fi

echo "Configuración del entorno:"
echo "ORACLE_HOME: $ORACLE_HOME"
echo "ORACLE_BASE: $ORACLE_BASE"
echo "ORACLE_SID: $ORACLE_SID"

echo "Ejecutando script de inicialización..."
echo "Usuario a crear: c##${ORACLE_USER}"

# Ejecutar sqlplus con la configuración completa
sqlplus -S / as sysdba << EOF
SET ECHO OFF
SET VERIFY OFF
SET FEEDBACK OFF
SET SERVEROUTPUT ON
WHENEVER SQLERROR EXIT SQL.SQLCODE;
DEFINE usuario = "${ORACLE_USER}"
DEFINE password = "${ORACLE_USER_PWD}"
@/opt/oracle/scripts/setup/init.sql
EOF

STATUS=$?
if [ $STATUS -eq 0 ]; then
    echo "Script de inicialización completado exitosamente"
else
    echo "Error durante la ejecución del script SQL. Código de salida: $STATUS"
    exit $STATUS
fi