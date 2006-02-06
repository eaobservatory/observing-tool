#
# This Makefile is for top level make of the entire Orac java code
# (EDFREQ, OT and shared classes in GEMINI, ORAC and OMP).
#
# "gmake" makes GEMINI, ORAC, OMP, EDFREQ and OT recursively.

CONF_HOME           = conf
include $(CONF_HOME)/make.conf

# Variables for javadoc
SOURCE_FILES          = $(shell cd GEMINI/src; find . -name "*.java") \
                        $(shell cd ORAC/src;   find . -name "*.java") \
                        $(shell cd OMP/src;    find . -name "*.java") \
                        $(shell cd EDFREQ/src; find . -name "*.java") \
                        $(shell cd OT/src;     find . -name "*.java")


SOURCEPATH = GEMINI/src:ORAC/src:OMP/src:EDFREQ/src:OT/src
CLASSPATH  = GEMINI/classes/install:ORAC/classes/install:OMP/classes/install:EDFREQ/classes/install:OT/classes/install:$(shell echo $(EXTERNAL_JAR_FILES) | tr " " ":")

# Get packages.
# Start with a list of each directory containing a source file.
# Get rid of multiple entries of the same directory by using sort.
# Remove "./", replace '/' with '.' and remove ". "
PACKAGES = $(shell echo \
              $(shell echo \
	        $(shell echo \
		  $(sort \
		    $(foreach sourcFile,$(SOURCE_FILES),$(shell dirname $(sourcFile)))) \
               | sed -e 's/\.\///g') \
              | tr "/" ".") \
	    | sed -e 's/\. //g')


all:
	$(shell find . -name versionFile -exec rm {} \;)
	$(shell echo `date '+%Y%m%d'` > OT/cfg/jcmt/versionFile)
	$(shell echo `date '+%Y%m%d'` > OT/cfg/ukirt/versionFile)
	(cd GEMINI/src; gmake)
	(cd ORAC/src;   gmake)
	(cd OMP/src;    gmake)
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
	(cd OMP/src;    gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd EDFREQ/src; gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)
	(cd OT/src;     gmake JAR_DIR=$(shell (cd $(INSTALL_ROOT); pwd))/lib jar)

	mkdir -p $(INSTALL_ROOT)/tools
	cp OT/tools/*.jar ORAC/tools/*.jar OMP/tools/*.jar $(INSTALL_ROOT)/tools

	mkdir -p $(INSTALL_ROOT)/cfg

	rm -rf $(INSTALL_ROOT)/cfg/ot
	cp -r  OT/install/cfg $(INSTALL_ROOT)/cfg/ot

# Copy the frequency editor cfg directory edfreq into the installed cfg ot subdirectory.
	cp -r EDFREQ/install/cfg/edfreq $(INSTALL_ROOT)/cfg/ot/jcmt

	mkdir -p $(INSTALL_ROOT)/bin

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
	(cd OMP/src;    gmake clean)
	(cd EDFREQ/src; gmake clean)
	(cd OT/src;     gmake clean)
	rm -rf $(INSTALL_ROOT)

_jar: $(JAR_DIR)
	(cd GEMINI/src; gmake jar)
	(cd ORAC/src;   gmake jar)
	(cd OMP/src;    gmake jar)
	(cd EDFREQ/src; gmake jar)
	(cd OT/src;     gmake jar)

_doc: $(DOC_DIR)
	$(JAVADOC) -J-mx100m -classpath $(CLASSPATH) -sourcepath $(SOURCEPATH) -d $(DOC_DIR) $(PACKAGES)


$(JAR_DIR):
	-mkdir -p $(JAR_DIR)


$(DOC_DIR):
	-mkdir -p $(DOC_DIR)


