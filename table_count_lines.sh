#!/bin/bash

#
# Copyright (c) 2024. Bernard Bou.
#

source define_tables.sh

outdir="$1"
if [ "${outdir}" == "" ]; then
  outdir="sql/"
fi
datadir="data/"

for table in ${tables}; do
  file=${outdir}${datadir}${table}.sql
  c=$(grep -c '^(' ${file})
  echo "${table} ${c}"
done