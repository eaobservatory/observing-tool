# This file specifies running configuration for the Observing Manager.
# Configurable elements are specified by key/value pairs in the format:
#
#	key: value
#
# Some elements have more than one key/value pair.
# Only one configuration file for running.

# Location for Ot configuration files to find orac ot objects.
# It is not in use now.
ot.cfgdir:/ukirt_sw/oracrel/apps/ot/cfg/

# Location for EXEC_PATH to find a directory where has a lot of exec files
# This one should be the same which is defined in login.orac or systemsetup
# script.
EXEC_PATH:/ukirtdata/orac_data/sequences

# CONF_PATH specifies path to the configuration files to run the exec
# translation class.  Make sure the read-write protection is correct,
# otherwise the translation task could fail.
CONF_PATH:/ukirtdata/orac_data/configs

# CONT_PATH specifies the location of the Drama sequencer.  It will fail
# to start if the JVM can not find the file.  The OM would not run the 
# Drama task fails to start.
CONT_CODE:/ukirt_sw/ukroot/install/orac/bin/solaris/oos

# Location for images files; there are a number of gif files.
IMAG_PATH:/ukirt_sw/oracom/image/

# SOCK_CODE specifies the location of the Drama Socket task and code.
# It will fail to start if the JVM can not find the file.  The OM would
# not run the Drama task fails to start.
SOCK_CODE:/ukirt_sw/ukroot/install/orac/bin/solaris/dsocket

# MONI_CODE specifies the location of the monitor code, and hence the
# the Drama monitor task.  It will fail to start if the JVM can not find
# the file.  The OM would not run the Drama task fails to start.
MONI_CODE:/ukirt_sw/ukroot/install/orac/bin/solaris/monitor

# Specify the location for each instrument's control code and task.
# It will fail to start if the JVM can not find the file.  The OM
# would not run the Drama task fails to start.
#CGS4_CODE:/net/alba/sw4/mt/cgs4/bin/cgs4
UFTI_CODE:/ukirt_sw/ukroot/install/orac/bin/solaris/ufti

# Name of tasks to monitor for parameters of instruments. If not specified here
# then the name defaults to the same as the instrument.
CGS4:ALFD@IRTCON
IRCAM3:ALFD@IRTCON

# Location of script for load dhs
LOAD_DHSC:/ukirt_sw/bin/loadORAC
#LOAD_DHSC:/bin/true

# Script debugging. Turn this on for messages from the load script and
# associated tasks to appear
SCR_MESS:ON

# RMI debugging, a switch for RMI-related debugging print outs
RMIS_MESS:ON

# Normal debugging, a switch for normal debugging print outs e.g. the
# JVM path settings.
DBUG_MESS:ON

# Socket message debugging, a switch for all the string messgae from the
# socket.
SOCK_MESS:ON

# RMI SERVER OBJECT, the RMI server object name for CGS4.
CGS4_OBJE:rmi://kiki.ukirt.jach.hawaii.edu:4321/cgs4Console

# RMI SERVER OBJECT, the RMI server object name for IRCAM3.
IRCAM3_OBJE:rmi://kiki.ukirt.jach.hawaii.edu:4321/ircam3Console

# RMI SERVER OBJECT, the RMI server object name for Michelle.
MICH_OBJE:rmi://kiki.ukirt.jach.hawaii.edu:4321/michelleConsole

# RMI SERVER OBJECT, the RMI server object name for UFTI.
UFTI_OBJE:rmi://kiki.ukirt.jach.hawaii.edu:4321/uftiConsole

# RMI SERVER PORT NUMBER, the post numer which rmi server uses.
RMIS_PORT:4321

