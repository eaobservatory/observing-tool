#!/bin/sh

FILE=install/bin/ot
echo "#!/bin/csh -f" >> $FILE
echo >> $FILE
echo "cd \`dirname \$0\`" >> $FILE
echo >> $FILE
echo "set CLASSPATH = $1" >> $FILE
echo >> $FILE
cat OT/src/scripts/ot_script_source >> $FILE
