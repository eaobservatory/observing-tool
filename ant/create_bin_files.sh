#!/bin/sh

FILE=install/bin/ot

cat > $FILE <<-END
	#!/bin/csh -f

	cd \`dirname \$0\`

	set CLASSPATH = $1

END

cat OT/src/scripts/ot_script_source >> $FILE
