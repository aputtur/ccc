#! /bin/bash
# bash script for JUnit


# *******************************************************
#    SCRIPT TO START JUNIT
#    Chris Collier 11/1/2006
# *******************************************************


# *******************************************************
#    Variables that will change per developer machine
#    Customize this script here
# *******************************************************

CCC_IS_MOUNT_POINT=/mnt/ccc/information_services
LOCAL_PROJECTS_HOME=$HOME/projects/ccc
JAVA_HOME=/usr/java/j2sdk1.4.2_05
JUNIT_RUNNER=junit.swingui.TestRunner


# *******************************************************
#    Test mode and database connection settings - developers
#    may want to change values here on occasion
# *******************************************************

TEST_MODE=junit

TEST_DB_URL=jdbc:oracle:thin:@dev2db1:1531:CCCD2
TEST_DB_USERNAME=CC_USER
TEST_DB_PASSWORD=CC_USER
TEST_DATA_DIR=$CC_HOME/test/data
TEST_SCHEMA_OWNER=CCINTEG

SHARED_REG_DB_URL=jdbc:oracle:thin:@dev2db1:1530:OAD2
SHARED_REG_DB_USERNAME=CUST
SHARED_REG_DB_PASSWORD=CUST

# *******************************************************
#    Global script variables built from previously
#    settings defined above.
# *******************************************************

JAVA_BIN=$JAVA_HOME/bin/java

CC_HOME=$LOCAL_PROJECTS_HOME/apps/CC2
CC_CONFIG_DIR=$CC_HOME/config

SHARED_HOME=$CCC_IS_MOUNT_POINT/builds/shared/latest_trunk/bin/DEBUG
COMMON_LIB_HOME=$LOCAL_PROJECTS_HOME/3rdpty/common-lib

ARGS=$*

USE_SWINGUI=FALSE
RUNNER=junit.textui.TestRunner

CC_CLASSPATH=$CC_HOME/test/junit/scripts:$CC_HOME/classes:$CC_HOME/config

# *******************************************************
#    Start of main script logic
# *******************************************************

CC_CLASSPATH=$CC_CLASSPATH:$SHARED_HOME/ccc-shared-base.jar
CC_CLASSPATH=$CC_CLASSPATH:$SHARED_HOME/ccc-shared-data.jar
CC_CLASSPATH=$CC_CLASSPATH:$SHARED_HOME/ccc-shared-internal.jar
CC_CLASSPATH=$CC_CLASSPATH:$SHARED_HOME/ccc-shared-public.jar
CC_CLASSPATH=$CC_CLASSPATH:$SHARED_HOME/ccc-shared-testing.jar

for lib in `cat ./cc2-deps.txt`
do
	CC_CLASSPATH=$CC_CLASSPATH:$COMMON_LIB_HOME/$lib
done

CMD=$JAVA_BIN
CMD="$CMD -server -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=4000"
#CMD="$CMD -ojvm -XXdebug,port4000,detached,quiet"
CMD="$CMD -classpath $CC_CLASSPATH"

CMD="$CMD -Dconfig.dir=$CC_CONFIG_DIR"
CMD="$CMD -Dcc2.test.mode=$TEST_MODE"
CMD="$CMD -Dcc2.test.db.url=$TEST_DB_URL"
CMD="$CMD -Dcc2.test.db.username=$TEST_DB_USERNAME"
CMD="$CMD -Dcc2.test.db.password=$TEST_DB_PASSWORD"
CMD="$CMD -Dcc2.test.data=$TEST_DATA_DIR"
CMD="$CMD -Dcc2.schema.owner=$TEST_SCHEMA_OWNER"

CMD="$CMD -Dshared.test.mode=$TEST_MODE"
CMD="$CMD -Dshared.test.reg.url=$SHARED_REG_DB_URL"
CMD="$CMD -Dshared.test.reg.username=$SHARED_REG_DB_USERNAME"
CMD="$CMD -Dshared.test.reg.password=$SHARED_REG_DB_PASSWORD"
CMD="$CMD $JUNIT_RUNNER"

echo $CMD
$CMD


