#!/bin/sh

# nom du répertoire ou seront deployés les fichiers générés
RELEASE_TARGET=scripts/files-to-release

# ou se situe le repertoire ou seront déposés les fichiers de mise à jour
FINAL_RELEASE_DIRECTORY=../ged90

# the repository which contains documentation
#DOC_RELEASE_DIRECTORY=../simple-ged-doc


# @params neutral message
show_neutral_message(){
	echo -e "\033[0;36;40m$@\033[0m"
}
# @params information message
show_information_message(){
	echo -e "\033[7;32;40m$@\033[0m"
}
# @params error message
show_error_message(){
	echo -e "\033[7;31;47m$@\033[0m"
}


#
# skip tests ?
#
MAVEN_TEST=
if [ "$1" == "skip-test" ]
then
	MAVEN_TEST=-Dmaven.test.skip=true
fi



# proxy
if [ ! -z "${HTTP_PROXY}" ]
then
	X_PROXY="`echo '-x'` `echo ${HTTP_PROXY}`"
	echo "Proxy configuré : ${X_PROXY}"
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
		show_error_message "Les versions définies entre maven et l'application sont différentes pour $3"
		show_error_message "Merci de les ajuster avant de lancer le build de la release"
		exit
	fi
}

#
# Vérifie que la version en ligne n'est pas la même que celle que l'on veut générer
#
# @param 1 Version du code
# @param 2 Version de maven
# @param 3 Module concerné
#
# Quitte en cas d'erreur
#
check_maven_online_version() {
	if [ "$1" == "$2" ]
	then
		show_error_message "La version que vous essayez de générer est déjà en ligne pour $3"
		show_error_message "Merci de passer à la version suivante avant de lancer le build de la release"
		exit
	fi
}

#
# retour à la source
#
cd ..
pwd


#
# Suppresion du dossier release précédent
#
rm -fr ${RELEASE_TARGET}


#
# verification de l'existence du repertoire ou seront poses les releases
#
if [ ! -d "${FINAL_RELEASE_DIRECTORY}" ]
then
	show_error_message "Le repertoire ${FINAL_RELEASE_DIRECTORY} où devrait être déposée la version finale des fichiers n'existe pas"
	exit 0
fi



#
# Verification de la coherence des versions
#

CORE_CODE_VERSION=$(grep 'APPLICATION_VERSION' 'ged-core/src/main/resources/properties/constants.properties' | sed 's/^.*=\(.*\)$/\1/')
CORE_MAVEN_VERSION=$(grep 'ged.core.version' 'pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')

UPDATER_CODE_VERSION=$(grep 'UPDATER_VERSION =' 'ged-update/src/main/java/com/simple/ged/update/DoUpdate.java' | sed 's/^.*"\(.*\)".*$/\1/')
UPDATER_MAVEN_VERSION=$(grep 'ged.updater.version' 'pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')

CORE_ONLINE_VERSION=$(curl ${X_PROXY} 'http://www.xaviermichel.info/data/ged/last_version.xml' | grep 'number' | sed 's/^.*>\(.*\)<.*$/\1/')
UPDATER_ONLINE_VERSION=$(curl ${X_PROXY} 'http://www.xaviermichel.info/data/ged/updater_last_version.xml' | grep 'number' | sed 's/^.*>\(.*\)<.*$/\1/')

echo "Version de ged-core selon le code     : ${CORE_CODE_VERSION}"
echo "Version de ged-core selon maven       : ${CORE_MAVEN_VERSION}"

echo "Version de ged-updater selon le code  : ${UPDATER_CODE_VERSION}"
echo "Version de ged-updater selon maven    : ${UPDATER_MAVEN_VERSION}"

echo "Version en ligne de ged-core          : ${CORE_ONLINE_VERSION}"
echo "Version en ligne de ged-updater       : ${UPDATER_ONLINE_VERSION}"

check_maven_code_version ${CORE_CODE_VERSION} ${CORE_MAVEN_VERSION} ged-core
check_maven_code_version ${UPDATER_CODE_VERSION} ${UPDATER_MAVEN_VERSION} ged-updater

check_maven_online_version ${CORE_MAVEN_VERSION} ${CORE_ONLINE_VERSION} ged-core


# doit-on mettre l'updater à jour ?
UPDATER_IS_TO_UPDATE=0
if [ ${UPDATER_ONLINE_VERSION} != ${UPDATER_MAVEN_VERSION} ]
then
	echo "ged-update doit être mis à jour"
	UPDATER_IS_TO_UPDATE=1
fi


# Résumé des informations
show_neutral_message "Résumé des informations"

show_information_message "ged-core   ${CORE_ONLINE_VERSION} -> ${CORE_MAVEN_VERSION}"

if [ "${UPDATER_IS_TO_UPDATE}" == "1" ]
then
	show_information_message "ged-update ${UPDATER_ONLINE_VERSION} -> ${UPDATER_MAVEN_VERSION}"
fi

show_neutral_message "Appuyez sur entrer pour valider"
read


#
# Compilation
#

mvn clean install ${MAVEN_TEST}

if [ $? -ne 0 ]
then
	show_error_message 'Le build global du projet a échoué !'
	exit 1
fi


#
# Regrouppement des fichiers pour la release
#

if [ ! -e "${RELEASE_TARGET}" ]
then
	mkdir ${RELEASE_TARGET}
fi


RELEASE_DIR_TARGET="${RELEASE_TARGET}/simple_ged"


IMAGE_TARGET="${RELEASE_DIR_TARGET}/images"

# la version globale : pour une installation sans rien avant

mkdir -p ${IMAGE_TARGET}
cp ged-core/src/main/resources/images/icon.ico "${IMAGE_TARGET}"
cp ged-core/dll/AspriseJTwain.dll "${RELEASE_DIR_TARGET}"

#cp ged-connector/target/ged-connector*.jar "${RELEASE_DIR_TARGET}/SimpleGedConnector.jar"

cp ged-update/target/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar "${RELEASE_DIR_TARGET}/simpleGedUpdateSystem.jar"

cp ged-core/target/ged-core-${CORE_MAVEN_VERSION}.jar "${RELEASE_DIR_TARGET}/simple_ged.jar"


mkdir -p ${RELEASE_DIR_TARGET}/lib/
for dir_resource in ged-core/target/lib
do
	cp -r "${dir_resource}" "${RELEASE_DIR_TARGET}"
done


# les versions pour mise a jour (que les jars qui ont changes)

if [ "${UPDATER_IS_TO_UPDATE}" == "1" ]
then
	cp ged-update/target/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar "${RELEASE_TARGET}"
fi

cp ged-core/target/ged-core-${CORE_MAVEN_VERSION}.jar "${RELEASE_TARGET}"



# javadoc

#cd ged-core
#mvn javadoc:javadoc
#cd ..

#if [ $? -ne 0 ]
#then
#	show_error_message 'La génération de la javadoc a échouée'
#	exit 1
#fi



#
# Generation des descripteurs (xml) de mise a jour
#

cat > "${RELEASE_TARGET}/last_version.xml" <<EOL
<?xml version="1.0" encoding="UTF-8"?>
<version>
	<number>CURRENT_VERSION</number>
	<files>
		<file>
			<url>http://master.dl.sourceforge.net/project/simpleged/updates/ged-core-${CORE_MAVEN_VERSION}.jar</url>
			<destination>simple_ged.jar</destination>
		</file>
EOL



FINAL_RELEASE_DIRECTORY_LIB="${FINAL_RELEASE_DIRECTORY}/lib"
if [ ! -d "${FINAL_RELEASE_DIRECTORY_LIB}" ]
then
	mkdir -p "${FINAL_RELEASE_DIRECTORY_LIB}"
fi


for dependency in $(ls ${RELEASE_DIR_TARGET}/lib)
do
	echo "Checking for dependency : $dependency"
	if [ ! -e "${FINAL_RELEASE_DIRECTORY_LIB}/${dependency}" ]
	then
		cp  "${RELEASE_DIR_TARGET}/lib/${dependency}" "${FINAL_RELEASE_DIRECTORY_LIB}"
	fi
		cat >> "${RELEASE_TARGET}/last_version.xml" <<EOL
		<file>
			<url>http://ged90.googlecode.com/git/lib/${dependency}</url>
			<destination>lib/${dependency}</destination>
		</file>
EOL
done


cat >> "${RELEASE_TARGET}/last_version.xml" <<EOL
	</files>
</version>
EOL

sed -i -e "s/CURRENT_VERSION/${CORE_MAVEN_VERSION}/g" "${RELEASE_TARGET}/last_version.xml"



cat > "${RELEASE_TARGET}/updater_last_version.xml" <<EOL
<?xml version="1.0" encoding="UTF-8"?>
<version>
	<number>CURRENT_VERSION</number>
	<files>
		<file>
			<url>http://master.dl.sourceforge.net/project/simpleged/updates/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar</url>
			<destination>simpleGedUpdateSystem.jar</destination>
		</file>
	</files>
</version>
EOL

sed -i -e "s/CURRENT_VERSION/${UPDATER_MAVEN_VERSION}/g" "${RELEASE_TARGET}/updater_last_version.xml"



#
# Creation des archives (zip)
#

cd "${RELEASE_TARGET}"
zip -r "simple_ged_${CORE_MAVEN_VERSION}.zip" "simple_ged"
rm -fr "simple_ged"
cd -



#
# On mets les droits a tous (probleme de mon windows ?)
#
chmod -R 777 ${RELEASE_TARGET}/*
chmod -R 777 ${FINAL_RELEASE_DIRECTORY}/*



#
# Accept upload
#
TIMEOUT_IN_SECOND=10
show_neutral_message "L'upload commencera dans ${TIMEOUT_IN_SECOND} secondes si l'action n'est pas annulée via controle-c"
sleep ${TIMEOUT_IN_SECOND}


#
# Envoi des zip genere
#

scp "${RELEASE_TARGET}/ged-core-${CORE_MAVEN_VERSION}.jar" xaviermichel@frs.sourceforge.net:/home/frs/project/simpleged/updates

if [ "${UPDATER_IS_TO_UPDATE}" == "1" ]
then
	 scp "${RELEASE_TARGET}/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar" xaviermichel@frs.sourceforge.net:/home/frs/project/simpleged/updates
fi



scp "${RELEASE_TARGET}/simple_ged_${CORE_MAVEN_VERSION}.zip" xaviermichel@frs.sourceforge.net:/home/frs/project/simpleged/releases



#
# Retour dans le repertoire de travail
#
cd scripts
pwd


#
# fini
#

show_information_message "Packages générés, il faut envoyer et mettre à jour les pom maintenant"
show_error_message "Note : les descripteurs de mise à jour ne sont pas déployés automatiquement, à vous de le faire après les vérifications d'usage"

