# This Makefile is for top level make of the entire Orac java code
# (OM, OT and shared classes in GEMINI, ORAC and ODB).
#
# "gmake" makes GEMINI, ORAC, ODB, OM and OT recursively.

CONF_HOME           = conf
include $(CONF_HOME)/make.conf

# Needed for target doc
SOURCE_FILES          = $(shell find GEMINI/src -name "*.java") \
                        $(shell find ORAC/src   -name "*.java") \
                        $(shell find ODB/src    -name "*.java") \
                        $(shell find OT/src     -name "*.java") \
                        $(shell find OM/src     -name "*.java")

all:
	(cd GEMINI/src; gmake)
	(cd ORAC/src;   gmake)
	(cd ODB/src;    gmake)
	(cd OM/src;     gmake)
	(cd OT/src;     gmake)

jar:
ifeq ($(JAR_DIR), )
	@echo Usage: gmake JAR_DIR=my_jar_dir jar
else
	gmake _jar
endif

.PHONY: install

# This top level install target assumes default locations in the sub directories
install: install_dir
	mkdir -p $(INSTALL_ROOT)/lib
	(cd GEMINI/src; gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd ORAC/src;   gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd ODB/src;    gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd OM/src;     gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd OT/src;     gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)

	mkdir -p $(INSTALL_ROOT)/tools
	cp ORAC/tools/*.jar OT/tools/*.jar $(INSTALL_ROOT)/tools

	mkdir -p $(INSTALL_ROOT)/bin
	(cd ODB/src; gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/bin/odb)
	(cd OM/src;  gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/bin/om)
	(cd OT/src;  gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/bin/ot)
	(cd OT/src;  gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/bin/ot.bat)

install_dir:
	@mkdir -p $(INSTALL_ROOT)

doc:
ifeq ($(DOC_DIR), )
	@echo Usage: gmake DOC_DIR=my_doc_root doc
else
	gmake _doc
endif

clean:
	(cd GEMINI/src; gmake clean)
	(cd ORAC/src;   gmake clean)
	(cd ODB/src;    gmake clean)
	(cd OM/src;     gmake clean)
	(cd OT/src;     gmake clean)

_jar: $(JAR_DIR)
	(cd GEMINI/src; gmake jar)
	(cd ORAC/src;   gmake jar)
	(cd ODB/src;    gmake jar)
	(cd OM/src;     gmake jar)
	(cd OT/src;     gmake jar)


_doc: $(DOC_DIR)
	$(JAVADOC) -J-mx50m -sourcepath $(SOURCEPATH) -classpath $(CLASSPATH) -d $(DOC_DIR) $(SOURCE_FILES)


$(JAR_DIR):
	-mkdir -p $(JAR_DIR)


$(DOC_DIR):
	-mkdir -p $(DOC_DIR)


