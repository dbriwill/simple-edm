#!/bin/sh

. ./logger.sh

# path where are placed files before zipping them.
# /!\ The name will be root in the final zip
TMP_RELEASE_DIR=./simple-edm

# the final zip file, which is to release
RELEASE_FINAL_FILE=./simple-edm-{{VERSION}}.zip

# always skip tests
MAVEN_TEST=-Dmaven.test.skip=true


# proxy
if [ ! -z "${HTTP_PROXY}" ]
then
	X_PROXY="`echo '-x'` `echo ${HTTP_PROXY}`"
	echo "Proxy is configured : ${X_PROXY}"
fi


#
# Vérifie que les version entre le code et maven sont identiques
#
# @param 1 Version du code
# @param 2 Version de maven
# @param 3 Module concerné
#
# Quitte en cas d'erreur
#
check_maven_code_version() {
	if [ "$1" != "$2" ]
	then
		show_error_message "Versions are different beetwen code and maven plugin : ${3}"
		show_error_message "You have to fix that before releasing file ! Arboring now"
		exit 1
	fi
}



cd ..


# version check
EDM_CONSTANT_VERSION=$(grep 'APPLICATION_VERSION' 'edm-webapp/src/main/resources/properties/constants.properties' | sed 's/^.*=\(.*\)$/\1/')

EDM_PARENT_VERSION=$(grep '<edm.version>' 'pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')
EDM_CONTRACT_VERSION=$(grep 'version' 'edm-contracts/pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')
EDM_WEBAPP_VERSION=$(grep '<edm.version>' 'edm-webapp/pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')

echo "Parent version        : ${EDM_PARENT_VERSION}"
echo "Contracts version     : ${EDM_CONTRACT_VERSION}"
echo "Webapp version        : ${EDM_WEBAPP_VERSION}"
echo "Constant code version : ${EDM_CONSTANT_VERSION}"

check_maven_code_version ${EDM_CONSTANT_VERSION} ${EDM_PARENT_VERSION} "simple-edm parent"
check_maven_code_version ${EDM_CONSTANT_VERSION} ${EDM_CONTRACT_VERSION} "simple-edm contracts"
check_maven_code_version ${EDM_CONSTANT_VERSION} ${EDM_WEBAPP_VERSION} "simple-edm webapp"

show_information_message "Versions sounds pretty good !"

# replace version in vars
TMP_RELEASE_DIR=$(echo "${TMP_RELEASE_DIR}" | sed "s/{{VERSION}}/${EDM_CONSTANT_VERSION}/g")
RELEASE_FINAL_FILE=$(echo "${RELEASE_FINAL_FILE}" | sed "s/{{VERSION}}/${EDM_CONSTANT_VERSION}/g")

show_neutral_message "Temporary release dir : ${TMP_RELEASE_DIR}"
show_neutral_message "Released file : ${RELEASE_FINAL_FILE}"


mvn clean install ${MAVEN_TEST}
if [ $? -eq 0 ]
then
	show_information_message "maven compilation success"
else
	show_error_message "maven compilation has failed. Please fix errors before release"
	exit 2
fi

cd edm-webapp
mvn package -Dmaven.test.skip=true # now we're sur we wanna skip tests
if [ $? -eq 0 ]
then
	show_information_message "maven packaging success"
else
	show_error_message "maven packaging has failed. Please fix errors before release"
	exit 3
fi


cd ../scripts # back on root

mkdir -p ${TMP_RELEASE_DIR}

# main jar
cp ../edm-webapp/target/*.jar ${TMP_RELEASE_DIR}

# webapp resources
mkdir -p ${TMP_RELEASE_DIR}/src/main/webapp
cp -r ../edm-webapp/src/main/webapp ${TMP_RELEASE_DIR}/src/main/

# create release file
zip -r ${RELEASE_FINAL_FILE} ${TMP_RELEASE_DIR}

# remove tmp dir
rm -fr ${TMP_RELEASE_DIR}

show_information_message "${RELEASE_FINAL_FILE} is ready to be released"
show_information_message "Do not forget to update your poms now !"


