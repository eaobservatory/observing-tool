#!/usr/bin/csh

foreach javaFile ($*)
  cat $javaFile | sed -e 's/import gemini\.sp\.obscomp/import gemini\.sp\.obsComp/' > tmp.java
  mv tmp.java $javaFile
end
