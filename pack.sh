#!/bin/bash

# P A R A M   1

dbtag=$1
shift
if [ -z "${dbtag}" ]; then
	echo "$0 <dbtag:XX>"
	exit 1
fi

# S O U R C E S

source define_colors.sh

# M A I N

echo -e "${C}packing ${Y}${dbtag}${Z}"
echo "ant pack with dbtag=${dbtag}*"
ant -f make-dist-sql.xml -Ddbtag=${dbtag}
