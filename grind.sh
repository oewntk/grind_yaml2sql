#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#
# indir (yaml)
# indir2 (yaml2)
# outdir (sql/data)

IN="$1"
if [ -z "$1" ]; then
	IN=yaml
fi
echo "YAML:  ${IN}" 1>&2;

IN2="$2"
if [ -z "$2" ]; then
	IN2=yaml2
fi
echo "YAML2: ${IN2}" 1>&2;

OUTDIR="$3"
if [ -z "$3" ]; then
	OUTDIR=sql/data
fi
mkdir -p "${OUTDIR}"
echo "OUT:   ${OUTDIR}" 1>&2;

java -ea -jar oewn-grind-yaml2sql.jar "${IN}" "${IN2}" "${OUTDIR}"
echo "done $?"
