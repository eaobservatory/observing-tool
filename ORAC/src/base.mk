#
# The Makefile is created by "gmake -f base.mk Makefile"
# If you add any new source files or change the Makefile, re-run this
# command (or "make Makefile").
#
# Don't edit Makefile directly or your changes will be lost.

CONF_HOME = ../../../conf
include $(CONF_HOME)/make.conf

# Where the "gen_dep" script is found
GEN_DEP = `pwd`/../../../conf/gen_dep

# Where to install files locally, before they are working and tested
# well enough to be installed in $(INSTALL_ROOT)
LOCAL_INSTALL_ROOT = ..

# Where the API docs should go in "make doc"
DOC_DIR         = $(LOCAL_INSTALL_ROOT)/html/api

#GEMINI_CLASSES_DIR = $(LOCAL_INSTALL_ROOT)/classes
BASE_DIR	   = $(LOCAL_INSTALL_ROOT)/apps/ot
CLASSES_DIR	   = $(LOCAL_INSTALL_ROOT)/classes

# directory for third party libaries (jar files)
LIB_DIR            = ../../../lib

ATC_UTIL_CP        = ../../ATC_UTIL/classes
GEMINI_CP          = ../../GEMINI/classes
ODB_CP             = ../../ODBServer/classes
OM_CP              = ../../OM/classes
ORAC_CP            = ../../ORAC/classes
OT_CP              = ../../OT/classes

#INSTALL_BASE_DIR    = $(INSTALL_ROOT)/apps/ot
#INSTALL_CLASSES_DIR = $(INSTALL_BASE_DIR)/classes

# Right now, this variable is edited by hand for use with "make doc"
#GEMPCKGS        =  gemini.util gemini.util.sort gemini.gui gemini.gui.util gemini.gui.image gemini.sp gemini.sp.ipc gemini.sp.obsComp gemini.sp.iter gemini.fits.coord gemini.fits.gui gemini.fits.image gemini.cat gemini.job
PACKAGES	= jsky.app.ot jsky.app.ot.editor jsky.app.ot.fits.gui jsky.app.ot.gui jsky.app.ot.gui.image jsky.app.ot.gemini.inst jsky.app.ot.gemini.inst.editor jsky.app.ot.gemini.iter jsky.app.ot.gemini.iter.editor jsky.app.ot.gemini.tpe jsky.app.ot.job jsky.app.ot.tpe jsky.app.ot.tpe.feat jsky.app.ot.util gemini.sp gemini.sp.ipc gemini.sp.iter gemini.sp.obsComp gemini.corba.namesutil gemini.job gemini.util gemini.util.sort ScienceProgram ScienceProgram.ServerPackage ot.phase1

#$(GEMPCKGS) ot ot.editor ot.tpe ot.tpe.feat ot_ukirt.inst ot_ukirt.inst.editor ot_ukirt.iter ot_ukirt.iter.editor ot_ukirt.tpe ot_ukirt.util ot_ukirt.util.validation

#GEMBASE = ../../geminiPackages
#GEMSRCS = $(GEMBASE)/util:$(GEMBASE)/gui:$(GEMBASE)/sp:$(GEMBASE)/fits:$(GEMBASE)/cat:$(GEMBASE)/job
SOURCEPATH = $(JAVA_ROOT)/src:.


CLASSPATH	= $(CLASSES_DIR):$(GEMINI_CP):$(ATC_UTIL_CP)

JFLAGS		= -deprecation -d $(CLASSES_DIR) -sourcepath .

# Naming the main class (class that depends on all the other classes, typically the one with the main method) first
# allows more efficient compiling from scratch when all the classes have to be compiled.
# This is because the java compiler will compile all the classes that it find to be needed by the main class
# and not up to date.
# Note that the java compiler will normally not find all dependencies. And it cannot find classes that that are
# loaded at run time. That's why addidionally all the classes have to be named as targets as well.
# Naming the main class first, however, will cause the bulk of the classes to be compiled in one go
# wiht just one call to the java compiler.


# Note that this rule does NOT check whether all the images and cfg files are up to date
# Call "make reinstall" (or "make copy-images" or "make copy-cfg") to copy images and
# cfg files if they have been changed.
all: $(CLASSES_DIR) gen_prereq


$(CLASSES_DIR):
	@ -mkdir -p $(CLASSES_DIR)

Makefile:
	-rm -f $@
	cp base.mk $@
	$(GEN_DEP) $(GEN_DEP) $(CLASSES_DIR) >> $@


# default rule 
$(CLASSES_DIR)/%.class: %.java
	$(JAVAC) $(JFLAGS) -classpath $(CLASSPATH) $<


# Re-install

reinstall: all copy-cfg copy-images


# Bundle everything into jar file
jar: all $(CLASSES_DIR)/MANIFEST.MF
	jar cfm $(LIB_DIR)/ot_atc.jar $(CLASSES_DIR)/MANIFEST.MF -C $(CLASSES_DIR) .

$(CLASSES_DIR)/MANIFEST.MF: $(CONF_HOME)/MANIFEST.MF
	cp $(CONF_HOME)/MANIFEST.MF $(CLASSES_DIR)/MANIFEST.MF

# Setup

setup-gemini: setup-base
	-mkdir -p $(BASE_DIR)/cfg
	cp -r ../cfg_gemini/* $(BASE_DIR)/cfg

setup-ukirt: setup-base
	-mkdir -p $(BASE_DIR)/cfg
	cp -r ../cfg_ukirt/* $(BASE_DIR)/cfg

setup-base:
	-mkdir -p $(BASE_DIR)
	for d in audio bin gui images; \
	do \
		cp -r ../$$d $(BASE_DIR); \
	done

clean:
	for f in *.class ot ot_cfg; \
	do \
		rm -rf $(CLASSES_DIR)/$$f; \
	done

help:
	-mkdir -p ../cfg_ukirt/hsjar
	cd ../cfg_ukirt/help;${JAR} -cvf ../hsjar/othelp.jar ./

doc:
	-mkdir -p $(DOC_DIR)/ot
#	$(JAVADOC) -classpath $(CLASSPATH) -author -d $(DOC_DIR)/ot $(PACKAGES)
	$(JAVADOC) -J-mx50m -classpath $(CLASSPATH):$(JSKY_RELATED_CP):$(ODB_CP) -sourcepath $(SOURCEPATH) -d $(DOC_DIR)/ot $(PACKAGES)

# Installation

install: all install-base
	-mkdir -p $(INSTALL_BASE_DIR)/cfg
	cp -r ../cfg_gemini/* $(INSTALL_BASE_DIR)/cfg

install-ukirt: all setup-ukirt install-base
	-mkdir -p $(INSTALL_BASE_DIR)/cfg
	cp -r ../cfg_ukirt/* $(INSTALL_BASE_DIR)/cfg

install-base: install-clean
	-mkdir -p $(INSTALL_BASE_DIR)
	for d in audio bin gui images; \
	do \
		cp -r ../$$d $(INSTALL_BASE_DIR); \
	done
	cp -r $(CLASSES_DIR) $(INSTALL_CLASSES_DIR)

install-clean:
	rm -rf $(INSTALL_BASE_DIR)

linrel: install-ukirt
	-mkdir -p $(LINUX_RELEASE_ROOT)
	cp -r $(INSTALL_ROOT)/classes/gemini $(LINUX_RELEASE_ROOT)
	cp -r $(INSTALL_BASE_DIR) $(LINUX_RELEASE_ROOT)/ot
	-mkdir -p $(LINUX_RELEASE_ROOT)/support
	cp -r $(FREEBONGO_CP) $(LINUX_RELEASE_ROOT)/support
	cp -r $(HOTJAVA_CP) $(LINUX_RELEASE_ROOT)/support
	cp -r $(JHCLASSES)/jhtools.jar $(LINUX_RELEASE_ROOT)/support
	cp -r $(JHCLASSES)/jhall.jar $(LINUX_RELEASE_ROOT)/support
	cp -r $(JHSWING) $(LINUX_RELEASE_ROOT)/support
	cp -r $(ODB_CP) $(LINUX_RELEASE_ROOT)/ODBServer
	-mkdir -p $(LINUX_RELEASE_ROOT)/bin
	cp -r $(INSTALL_BASE_DIR)/bin/ot $(LINUX_RELEASE_ROOT)/bin
	cp -r $(LINUX_JRE_ROOT) $(LINUX_RELEASE_ROOT)
solrel: install-ukirt
	cp -r $(INSTALL_ROOT)/classes/gemini $(SOL_RELEASE_ROOT)/gemClasses
	cp -r $(INSTALL_BASE_DIR) $(SOL_RELEASE_ROOT)/ot
	-mkdir -p $(SOL_RELEASE_ROOT)/support
	cp -r $(FREEBONGO_CP) $(SOL_RELEASE_ROOT)/support
	cp -r $(HOTJAVA_CP) $(SOL_RELEASE_ROOT)/support
	cp -r $(JHCLASSES)/jhtools.jar $(SOL_RELEASE_ROOT)/support
	cp -r $(JHCLASSES)/jhall.jar $(SOL_RELEASE_ROOT)/support
	cp -r $(JHSWING) $(SOL_RELEASE_ROOT)/support
	cp -r $(ODB_CP) $(SOL_RELEASE_ROOT)/ODBServer
	-mkdir -p $(SOL_RELEASE_ROOT)/bin
	cp -r $(INSTALL_BASE_DIR)/bin/ot $(SOL_RELEASE_ROOT)/bin
	cp -r $(SOL_JRE_ROOT) $(SOL_RELEASE_ROOT)
winrel: install-ukirt
	cp -r $(INSTALL_ROOT)/classes/gemini $(WIN_RELEASE_ROOT)/gemClasses
	cp -r $(INSTALL_BASE_DIR) $(WIN_RELEASE_ROOT)/ot
	-mkdir -p $(WIN_RELEASE_ROOT)/support
	cp -r $(FREEBONGO_CP) $(WIN_RELEASE_ROOT)/support
	cp -r $(HOTJAVA_CP) $(WIN_RELEASE_ROOT)/support
	cp -r $(JHCLASSES)/jhtools.jar $(WIN_RELEASE_ROOT)/support
	cp -r $(JHCLASSES)/jhall.jar $(WIN_RELEASE_ROOT)/support
	cp -r $(JHSWING) $(WIN_RELEASE_ROOT)/support
	cp -r $(ODB_CP) $(WIN_RELEASE_ROOT)/ODBServer
	-mkdir -p $(WIN_RELEASE_ROOT)/bin
	cp -r $(INSTALL_BASE_DIR)/bin/ot $(WIN_RELEASE_ROOT)/bin
	cp -r $(WIN_JRE_ROOT) $(WIN_RELEASE_ROOT)

force:
