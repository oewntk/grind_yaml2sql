#!/bin/bash

#
# Copyright (c) 2024. Bernard Bou.
#

# P A R A M S

source ../define_build.sh

dbdir=$1
shift
if [ -z "${dbdir}" ]; then
  dbdir=sql
fi

# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

# M A I N

echo -e "${C}packing ${Y}${TAG}${Z}"
echo "ant pack with dbtag=${TAG}"
ant -f make-dist-sql.xml -Ddbdir=${dbdir} -Ddbtag=${TAG} -Dversion="${BUILD}"

