# author: Martin Folger (M.Folger@roe.ac.uk)

# Making
#     class files          :  "gmake"         (output: CLASSES_DIR, set in CONF_HOME/make.conf)
#     jar file with classes:  "gmake jar"     (output: JAR_DIR,     set in CONF_HOME/make.conf)
#     javadoc html files   :  "gmake doc"     (output: DOC_DIR,    set in CONF_HOME/make.conf)
#
#     install              :  "gmake install" (output: see CONF_HOME/make.conf)
#     clean                :  "gmake clean"   (deletes class files, jar files, api docs)
#
# The output directories are by default subdirectories of LOCAL_INSTALL_ROOT.
# They can be edited in CONF_HOME/make.conf or changed on the command line (e.g. make JAR_DIR=my_jar_dir jar)

CONF_HOME           = ../conf
include $(CONF_HOME)/make.conf


CLASSES_DIR_FOR_SED = $(shell echo $(CLASSES_DIR) | sed -e 's/\./\\\./g' | sed -e 's/\//\\\//g')
CLASS_FILES         = $(shell find . -name "*.java" | sed -e 's/\.\//$(CLASSES_DIR_FOR_SED)\//g' | sed -e 's/\.java/\.class/g')

CLASSPATH           = $(CLASSES_DIR):$(GEMINI_CP):$(ORAC_CP)

# Should NOT be changed as other packages might rely on the this name!
JAR_FILE            = odb.jar


all: $(CLASSES_DIR) $(CLASS_FILES)

$(CLASSES_DIR):
	@ -mkdir -p $(CLASSES_DIR)


# default rule 
$(CLASSES_DIR)/%.class: %.java
	$(JAVAC) $(JFLAGS) -d $(CLASSES_DIR) -classpath $(CLASSPATH) -sourcepath . $<


# Bundle everything into jar file
jar: all
	jar cf $(JAR_DIR)/$(JAR_FILE) -C $(CLASSES_DIR) .

$(CLASSES_DIR)/MANIFEST.MF: $(CONF_HOME)/MANIFEST.MF
	cp $(CONF_HOME)/MANIFEST.MF $(CLASSES_DIR)/MANIFEST.MF

doc:
	-mkdir -p $(DOC_DIR)
	$(JAVADOC) -J-mx50m -d $(DOC_DIR) `find . -name "*.java"`

install:
	@echo Use \"\gmake JAR_DIR=my_jar_dir jar\" to create $(JAR_FILE) in my_jar_dir.
	@echo Use \"\gmake DOC_DIR=my_doc_dir doc\" to create html api javadocs in my_doc_dir.


clean:
	rm -f  $(CLASS_FILES)
	rm -f  $(JAR_DIR)/$(JAR_FILE)
	rm -rf $(DOC_DIR)/*

