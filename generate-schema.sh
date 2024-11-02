#!/bin/bash

#
# Copyright (c) 2021-2024. Bernard Bou.
#

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

if [ "$1" == "-compat" ]; then
  compatswitch="-compat"
  shift
  echo -e "${Y}compat mode${Z}"
else
  compatswitch=
fi

outdir="$1"
shift
if [ "${outdir}" == "" ]; then
  outdir="sql"
fi

m=wn

jar=target/yaml2sql-2.2.2-uber.jar
if [ "$*" != "" ]; then
  indir="$1"
  shift
  for sql in $*; do
    base=$(basename ${sql})
    java -ea -cp "${jar}" org.oewntk.sql.out.SchemaGenerator ${compatswitch} ${m} "${outdir}" "${indir}" "${sql}"
  done
else
  echo -e "${C}$(readlink -f ${outdir})${Z}"
  for db in mysql sqlite; do
    for type in create index reference views; do
      echo -e "${M}${db}/${type}${Z}"
      java -ea -cp "${jar}" org.oewntk.sql.out.SchemaGenerator ${compatswitch} ${m} "${outdir}/${db}/${type}" "${db}/${type}" $*
    done
  done
fi
