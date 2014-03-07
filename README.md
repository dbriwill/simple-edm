[![Build Status](https://travis-ci.org/xaviermichel/simple-edm.png?branch=master)](https://travis-ci.org/xaviermichel/simple-edm)


Simple EDM
==========


**Summary**

Simple EDM is a simple software of electronic document managment. It's thinked to be easy to use but scalable on more server. So you can use it for yourself, or extend it !
For more details, you can see http://www.xaviermichel.info/tips/read/17/Simple-GED-logiciel-open-source-de-gestion-lectronique-de-documents [FR].

**Features**

- easy to use (it's like a web application)
- powerful search engine
- scalability (Not tested yet, but thinked to be used on three-tier architecture)
- exposed REST api

Not supported yet (but you can help me if you wan't !) :

- right managment
- documents versionning (documents informations are versionned by ES, but not documents themself)
- comments on documents


Solution stack
--------------

![Solution stack](https://docs.google.com/drawings/d/1TRDdSgP6r0zwp2dezgcPhncy-NdKfb9r6bKF52U0QUE/pub?w=939&amp;h=643)


Wanna compile and run it in two minutes ?

     git clone https://github.com/xaviermichel/simple-edm.git
     cd simple-edm
     mvn install -Dmaven.test.skip=true
     cd edm-webapp
     mvn package -Dmaven.test.skip=true
     java -jar target/edm-webapp-*.jar


Example of deployment on my raspberry pi :

     xavier@raspberrypi:~/bin% mkdir simple-edm
     xavier@raspberrypi:~/bin% cd simple-edm
     xavier@raspberrypi:~/bin/simple-edm% cp ~/work/simple-edm/scripts/simple-edm-5.0-RC1.zip .
     xavier@raspberrypi:~/bin/simple-edm% unzip simple-edm-*.zip
     xavier@raspberrypi:~/bin/simple-edm% cd simple-edm
     xavier@raspberrypi:~/bin/simple-edm% java -jar edm-webapp-5.0-RC1.jar
     
And access it by network (for me, it means raspberrypi:8053) !


Downloads
---------

**Software**

You can find the [lastest release here](https://github.com/xaviermichel/simple-edm/releases). You've just to download the zip file, extract-it and launch the jar file.
For more explanations, you can take a look at [the installation wiki](https://github.com/xaviermichel/simple-edm/wiki/Comment-installer-simple-ged) [FR].



**Plugins**

You can also use some plugins with simple-edm. They used the explosed REST API to insert their documents in the core.


You will find examples for plugins development here : https://github.com/xaviermichel/simple-edm-plugins

