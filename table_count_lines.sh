#!/bin/bash

source define_tables.sh

outdir="sql/"
datadir="data/"

for table in ${tables}; do
  file=${outdir}${datadir}${table}.sql
  c=$(grep -c '^(' ${file})
  echo "${table} ${c}"
done