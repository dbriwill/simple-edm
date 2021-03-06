[![Build Status](https://travis-ci.org/xaviermichel/simple-edm.png?branch=master)](https://travis-ci.org/xaviermichel/simple-edm)


Simple EDM
==========


**Summary**

Simple EDM is a simple electronic document managment system. It's thinked to be easy to use but scalable on more server.
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


Example of deployment on my server :

     xavier@server:~/bin% mkdir simple-edm
     xavier@server:~/bin% cd simple-edm
     xavier@server:~/bin/simple-edm% cp ~/work/simple-edm/scripts/simple-edm-*.zip .
     xavier@server:~/bin/simple-edm% unzip simple-edm-*.zip
     xavier@server:~/bin/simple-edm% cd simple-edm
     xavier@server:~/bin/simple-edm% java -jar simple-edm.jar

And access it by network (for me, it means `server:8053`) !


Downloads
---------

**Software**

You can find the [lastest release here](https://github.com/xaviermichel/simple-edm/releases).



**Plugins**

You can also use some plugins with simple-edm. They used the explosed REST API to insert their documents in the core.


You will find examples for plugins development here : https://github.com/xaviermichel/simple-edm-plugins

