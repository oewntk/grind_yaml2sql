#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#
# indir (yaml)
# indir2 (yaml2)
# outdir (nidmaps)

indir=$1
if [ -z "${indir}" ]; then
  indir=yaml
fi
indir2=$2
if [ -z "${indir2}" ]; then
  indir2=yaml2
fi
outdir=$1
if [ -z "${outdir}" ]; then
  indir=nidmaps
fi

java -cp oewn-grind-yaml2sql.jar org.oewntk.grind.yaml2sql.Mapper "${indir}" "${indir2}" "${outdir}"