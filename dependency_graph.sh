#!/bin/bash

#
# Copyright (c) 2024. Bernard Bou.
#

graph=grind-yaml2sql
todir=images/

mvn dependency:tree \
-DoutputType=dot \
-DoutputFile=${todir}${graph}.dot

dot -Tpng -o "${todir}${graph}.png" "${todir}${graph}.dot"
