#!/usr/bin/csh

foreach javaFile ($*)
  cat $javaFile | sed -e 's/import jsky\.app\.ot\.nsp/import gemini\.sp/' > tmp.java
  mv tmp.java $javaFile
end
