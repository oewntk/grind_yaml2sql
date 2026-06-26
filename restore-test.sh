#!/bin/bash
#
# Copyright (c) 2024. Bernard Bou.
#

# 22/11/2021

set -e

# C O N S T S

thisdir=$(dirname $(readlink -m "$0"))
sqldir="${thisdir}/sql"
dbtype=dbtype
modules="wn"
tables="
synsets
words
casedwords
pronunciations
morphs
poses
relations
domains
samples
vframes
vtemplates
adjpositions
lexes
senses
lexes_morphs
lexes_pronunciations
senses_adjpositions
lexrelations
senses_vframes
senses_vtemplates
semrelations
usages
ilis
wikidatas
"

# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

# M A I N

if [ "$1" == "-y" ]; then
  silent=true
  [ "$#" -eq 0 ] || shift
else
  echo -e "${Y}Restore utility for ${dbtype}${Z}"
  read -r -p "Are you sure? [y/N] " response
  case "$response" in
  [yY][eE][sS] | [yY]) ;;
  *)
    exit 1
    ;;
  esac
fi

# D E L E T E (PARAM 1)

dbdelete=
if [ "$1" == "-d" ]; then
  dbdelete=true
  [ "$#" -eq 0 ] || shift
  if [[ "${silent}" != "true" ]]; then
    echo -e "${R}The -d switch will delete an existing database with this name${Z}"
    read -r -p "Are you sure you want to delete an existing database ? [y/N] " response
    case "$response" in
    [yY][eE][sS] | [yY]) ;;
    *)
      exit 2
      ;;
    esac
  fi
fi

# D A T A B A S E (PARAM 2)

db="$1"
if [ -z "${db}" ]; then
  read -p "Enter ${dbtype} database name: " db
fi
export db

# F U N C T I O N S

function process() {
  local sqlfile="$1"
  local op="$2"
  echo -e "${M}process${Z} ${C}${op} ${B}${sqlfile}${Z}"
}

function dbexists() {
  test -e "${db}"
  return $?
}

function deletedb() {
  echo -e "${M}delete ${db}${Z}"
}

function createdb() {
  echo -e "${M}create ${db}${Z}"
}

# R U N

echo -e "${M}restoring ${db}${Z}"

#database
if [[ "${dbdelete}" == "true" ]]; then
  deletedb
fi
if ! dbexists; then
  createdb
fi

# modules
for m in ${modules}; do
  echo -e "${C}${m}${Z}"
  for op in create data index reference; do
    echo -e "${M}${op}${Z}"
    case ${op} in
    data)
      dir="${sqldir}/${op}"
      suffix=
      ;;
    create | index | reference)
      dir="${sqldir}/${dbtype}/${op}"
      suffix="-${op}"
      ;;
    esac
    for table in ${tables}; do
      f="${dir}/${table}${suffix}.sql"
      if [ ! -e "${f}" -a "${op}" == "reference" ]; then
        continue
      fi
      echo -e "sql=${Y}$(basename ${f})${Z}"
      process "${f}" "${op}"
    done
  done
done
