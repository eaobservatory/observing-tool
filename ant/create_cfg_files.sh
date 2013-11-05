#!/bin/sh

# Copyright (C) 2010 Science and Technology Facilities Council.
# All Rights Reserved.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

# TAU LIST FILE
pushd OT/cfg/jcmt
ls tau*.dat > tau.list
popd

# VERSION FILES
DATE=`date '+%Y%m%d'`
VERSION=`git log | head -n 1 | cut -f2 -d ' '`
echo "$DATE [$VERSION]" > OT/cfg/jcmt/versionFile
echo "$DATE [$VERSION]" > OT/cfg/ukirt/versionFile
