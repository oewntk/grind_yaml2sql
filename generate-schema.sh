#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#

source define_colors.sh
source define_tables.sh

templatesdir="sqltemplates/"
createdir="create/"
constraindir="constrain/"
mysqldir="mysql/"
sqlitedir="sqlite/"
outdir="sql/"

for db in ${mysqldir} ${sqlitedir}; do

  for table in ${tables}; do
    echo "--- ${db}create ${table}"

    sql=${templatesdir}${db}${createdir}${table}-create.sql
    if [ ! -e "${sql}" ]; then
      >&2 echo -e "${R}${sql}${Z}"
     #touch "${sql}"
     continue
    fi
    java -ea -cp oewn-grind-yaml2sql.jar org.oewntk.grind.yaml2sql.SchemaGenerator "${outdir}${db}" "${sql}"
  done

  for table in ${tables}; do
    echo "--- ${db}constrain ${table}"

    sql=${templatesdir}${db}${constraindir}${table}-index.sql
    if [ ! -e "${sql}" ]; then
      >&2 echo -e "${R}${sql}${Z}"
      #touch "${sql}"
      continue
    fi
    java -ea -cp oewn-grind-yaml2sql.jar org.oewntk.grind.yaml2sql.SchemaGenerator "${outdir}${db}" "${sql}"

    sql=${templatesdir}${db}${constraindir}${table}-reference.sql
    if [ ! -e "${sql}" ]; then
      #>&2 echo -e "${R}${sql}${Z}"
      #touch "${sql}"
      continue
    fi
    java -ea -cp oewn-grind-yaml2sql.jar org.oewntk.grind.yaml2sql.SchemaGenerator "${outdir}${db}" "${sql}"
  done

done
