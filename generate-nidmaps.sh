#!/bin/bash

#
# Copyright (c) 2021-2024. Bernard Bou.
#

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
	OUTDIR=nidmaps
fi
mkdir -p "${OUTDIR}"
echo "OUT:   ${OUTDIR}" 1>&2;

jar=target/yaml2sql-2.1.1-uber.jar
java -ea -cp "${jar}" org.oewntk.grind.yaml2sql.Mapper "${IN}" "${IN2}" "${OUTDIR}"
echo "done $?"
