#!/bin/sh

# Copyright (C) 2010-2012 Science and Technology Facilities Council.
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

for FILESPEC in ot:ot_script_source ompvalidate:ompvalidate_source
do
    IFS=: read -r FILE FILESRC <<< "$FILESPEC"

    cat > output/bin/$FILE <<-END
#!/bin/csh -f

cd \`dirname \$0\`

set CLASSPATH = $1

END

    cat src/main/scripts/$FILESRC >> output/bin/$FILE
done
