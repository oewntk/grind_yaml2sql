#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#

pragmas_quick="PRAGMA synchronous=OFF;
PRAGMA count_changes=OFF;
PRAGMA journal_mode=MEMORY;
PRAGMA temp_store=MEMORY;
PRAGMA auto_vacuum=NONE;
PRAGMA automatic_index=OFF;"

pragmas_default="PRAGMA synchronous=FULL;
PRAGMA count_changes=OFF;
PRAGMA journal_mode=DELETE;
PRAGMA temp_store=OFF;
PRAGMA auto_vacuum=NONE;
PRAGMA automatic_index=OFF;"

begin="BEGIN TRANSACTION;"

commit="COMMIT TRANSACTION;"

sqlfile="sql/data/*.sql"
sqlfile="$1"

tempdir=$(mktemp -d /tmp/sqlite.XXXXXXXXX)

function to_temp()
{
	local sqlfile="$1"
	local base="$(basename "${sqlfile}")"
	echo "${tempdir}/${base}"
}

function fast()
{
	local sqlfile="$1" # can be or include *
	local base="$(basename "${sqlfile}")"
	local sqlfile2="${tempdir}/${base}"
  printf '%s\n%s\n%s\n%s\n%s' "${pragmas_quick}" "${begin}" "$(cat ${sqlfile})" "${commit}" "${pragmas_default}"
}

function fast_to_temp()
{
	local sqlfile="$1" # can be or include *
  tempfile=$(to_temp "${sqlfile}")
  fast "${sqlfile}" > "${tempfile}"
  echo "${tempfile}"
}

sqlfile2=$(fast_to_temp "${sqlfile}")
echo "${sqlfile2}"