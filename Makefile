# This Makefile is for top level make of the entire Orac java code
# (OM, EDFREQ, OT and shared classes in GEMINI, ORAC, ODB and OMP).
#
# "gmake" makes GEMINI, ORAC, ODB, OMP, OM, EDFREQ and OT recursively.

CONF_HOME           = conf
include $(CONF_HOME)/make.conf

# Needed for target doc
SOURCE_FILES          = $(shell find GEMINI/src -name "*.java") \
                        $(shell find ORAC/src   -name "*.java") \
                        $(shell find ODB/src    -name "*.java") \
                        $(shell find OMP/src    -name "*.java") \
                        $(shell find EDFREQ/src -name "*.java") \
                        $(shell find OT/src     -name "*.java") \
                        $(shell find OM/src     -name "*.java")

all:
	(cd GEMINI/src; gmake)
	(cd ORAC/src;   gmake)
	(cd ODB/src;    gmake)
	(cd OMP/src;    gmake)
	(cd OM/src;     gmake)
	(cd EDFREQ/src; gmake)
	(cd OT/src;     gmake)

jar:
ifeq ($(JAR_DIR), )
	@echo Usage: gmake JAR_DIR=my_jar_dir jar
else
	gmake _jar
endif

.PHONY: install

# This top level install target assumes default locations in the sub directories
install: all install_dir
	mkdir -p $(INSTALL_ROOT)/lib
	(cd GEMINI/src; gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd ORAC/src;   gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd ODB/src;    gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd OMP/src;    gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd OM/src;     gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd EDFREQ/src; gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd OT/src;     gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)

	mkdir -p $(INSTALL_ROOT)/tools
	cp OT/tools/*.jar ORAC/tools/*.jar OMP/tools/*.jar $(INSTALL_ROOT)/tools

	mkdir -p $(INSTALL_ROOT)/cfg

	rm -rf $(INSTALL_ROOT)/cfg/odb
	rm -rf $(INSTALL_ROOT)/cfg/om
	rm -rf $(INSTALL_ROOT)/cfg/ot
	cp -r ODB/install/cfg $(INSTALL_ROOT)/cfg/odb
	cp -r  OM/install/cfg $(INSTALL_ROOT)/cfg/om
	cp -r  OT/install/cfg $(INSTALL_ROOT)/cfg/ot

	(cd OM/src; gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/om_images)

	mkdir -p $(INSTALL_ROOT)/bin
	(cd ODB/src; gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) \
	                   CFG_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/cfg/odb \
			   OT_CFG_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/cfg/ot \
		           $(shell (cd $(INSTALL_ROOT); pwd))/bin/odb)

#       The om script generation differs slightly form the generation of the ot and odb scripts.
#       It assumes that CFG_DIR is set to the absolute path of a cfg directory.
	(cd OM/src;  gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/bin/om;\
	             gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/bin/os;\
	             gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) $(shell (cd $(INSTALL_ROOT); pwd))/bin/mon)

	(cd OT/src;  gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) \
	                   CFG_DIRS=../cfg/ot \
			   $(shell (cd $(INSTALL_ROOT); pwd))/bin/ot)

	(cd OT/src;  gmake INSTALL_ROOT=$(shell (cd $(INSTALL_ROOT); pwd)) \
	                   INSTALL_BAT_SCRIPT=scripts/ot_bat_install_all_source \
			   $(shell (cd $(INSTALL_ROOT); pwd))/bin/ot.bat)


install_dir:
	@mkdir -p $(INSTALL_ROOT)

doc:
ifeq ($(DOC_DIR), )
	@echo Usage: gmake DOC_DIR=my_doc_root doc
else
	gmake _doc
endif

# Note that if JAR_DIR or DOC_DIR are set to a directory outside INSTALL_ROOT they will not be
# deleted by clean.
clean:
	(cd GEMINI/src; gmake clean)
	(cd ORAC/src;   gmake clean)
	(cd ODB/src;    gmake clean)
	(cd OMP/src;    gmake clean)
	(cd OM/src;     gmake clean)
	(cd EDFREQ/src; gmake clean)
	(cd OT/src;     gmake clean)
	rm -rf $(INSTALL_ROOT)

_jar: $(JAR_DIR)
	(cd GEMINI/src; gmake jar)
	(cd ORAC/src;   gmake jar)
	(cd ODB/src;    gmake jar)
	(cd OMP/src;    gmake jar)
	(cd OM/src;     gmake jar)
	(cd EDFREQ/src; gmake jar)
	(cd OT/src;     gmake jar)


_doc: $(DOC_DIR)
	$(JAVADOC) -J-mx50m -sourcepath $(SOURCEPATH) -classpath $(CLASSPATH) -d $(DOC_DIR) $(SOURCE_FILES)


$(JAR_DIR):
	-mkdir -p $(JAR_DIR)


$(DOC_DIR):
	-mkdir -p $(DOC_DIR)


