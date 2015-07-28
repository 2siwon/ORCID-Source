# Development Environment Setup

##Prerequisites 

1. Install [Java](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html). Add an environment variable JAVA_HOME. (Verify Java. Go to cmd and type "java -version". It should display the version of Java)
    
* Install [Maven](http://maven.apache.org/index.html). Add an environment variable M2_HOME. (Verify Maven. Go to cmd and type "mvn -version". It should display the version of Maven)

* Install [Postgres](http://www.postgresql.org/download/). (Verify Postgres. Go to cmd. Navigate to /postgres/xx/bin and execute the command "psql -U postgres". Type the password entered during the installation, if prompted. It should show a postgres console.)

* Install [Tomcat 7](http://tomcat.apache.org/). (Verify Tomcat. Go to the directory /apache-tomcat-7.x/bin and run the batch "startup.bat". It should start the server and display a message "Server startup in xxxx ms".)


## Setup Postgres DB
We'll set up postgres using the default settings in 
[staging-persistence.properties](https://github.com/ORCID/ORCID-Source/blob/master/orcid-persistence/src/main/resources/staging-persistence.properties).
 Please change usernames and passwords for any production environments.

1. Become postgres user

    ```
    sudo su - postgres
    ```
    
* Set up database

    ```
    psql -c "CREATE DATABASE orcid;"     
    psql -c "CREATE USER orcid WITH PASSWORD 'orcid';" 
    psql -c "GRANT ALL PRIVILEGES ON DATABASE orcid to orcid;"
    
    psql -c "CREATE DATABASE statistics;" 
    psql -c "CREATE USER statistics WITH PASSWORD 'statistics';" 
    psql -c "GRANT ALL PRIVILEGES ON DATABASE statistics to statistics;"
    
    psql -c "CREATE USER orcidro WITH PASSWORD 'orcidro';"
    psql -c "GRANT CONNECT ON DATABASE orcid to orcidro;"
    psql -c "GRANT SELECT ON ALL TABLES IN SCHEMA public to orcidro;" orcid
    ```
    
* Exit postgres user prompt
    
    ```
    exit
    ```

* Verify user login and database exist

    ```
    psql -U orcid -d orcid -c "\list" -h localhost
    psql -U statistics -d statistics -c "\list" -h localhost
    ```


## Clone the git repository

1. Make a git directory if one doesn't exist


    ```
    mkdir ~/git
    ```


* Clone the repository

	```
	cd ~/git
    git clone git@github.com:ORCID/ORCID-Source.git
    ```


## Clone the git ORCID-Fonts-Dot-Com repository
Do to licensing issues this is only available to ORCID.org employees.

1. Clone the ORCID-Fonts-Dot-Com repository into the static directory

	```
    git clone git@github.com:ORCID/ORCID-Fonts-Dot-Com.git ~/git/ORCID-Source/orcid-web/src/main/webapp/static/ORCID-Fonts-Dot-Com
    ```


## Run Maven Task - First Time Only
1. maven clean install

    ```
    cd ~/git/ORCID-Source
    mvn clean install -Dmaven.test.skip=true
    ```
    
Tip: If you experience the following error: 

    ```
    Caused by: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
    ```
   
You can find the solution [here](http://stackoverflow.com/questions/25911623/problems-using-maven-and-ssl-behind-proxy)    
    
Tip: use the same command for rebuilding.

## Create the Database Schema - First Time Only

Intialize the database schema (runs as orcid the first time, but then you need to run it again as postgres because some tasks require superuser).

    ```
    cd ~/git/ORCID-Source/orcid-core
    
    mvn exec:java -Dexec.mainClass=org.orcid.core.cli.InitDb
    
    mvn exec:java -Dexec.mainClass=org.orcid.core.cli.InitDb -Dorg.orcid.persistence.db.username=postgres -Dorg.orcid.persistence.db.password=postgres
    
    ```

## Eclipse Setup (Spring Tool Suite Eclipse)
These instructions are for Spring Tool Suite for Eclipse. 

1. Download and install Spring Tool Suite for Eclipse:
http://www.springsource.org/downloads/sts-ggts

* Select File-> Import -> Git -> Project from Git, Click Next.

* Select "Existing local repository", Click Next

* Select Add, once ORCID-Source has been added, select it and click Next

* Select "Import as general project", click Next.

* Click Finish

* In package Explorer, right click ORCID-Source.

* Select Configure (at the bottom) -> Select "Convert to Maven Project"

* In package Explorer Right click on ORCID-Sourc 

* Select Import -> "Existing Maven Projects"

* Unselect the first pom.xml (orcid-parent)

* Select all pom.xml(s) after.

* Click Finish

* Select Window -> Preferences -> Servers(Expand) -> Runtime Environments

* Click on Add.

* Expand the folder Apache, select Apache Tomcat v7.0 and click Next.

* Browse to the directory of apache tomcat in the file system and click Finish.

* Click OK.

* Go to File -> New -> Other.

* Filter for 'Server', select and click Next.

* Expand the folder Apache, select Apache Tomcat v7.0.

* Field 'Server Runtime Environment' should point to the newly added server runtime for tomcat.

* Click Next and Finish.

* Select Window -> Show View -> Servers

* Double Click "Apache Tomcat Server 7.0"

* Select Open launch configuration

* Select Arguments 

* In VM Arguments add the following (changing the /Users/rcpeters/git/ORCID-Source path to your repo checkout)

    ```
    -Dsolr.solr.home=/Users/rcpeters/git/ORCID-Source/orcid-solr-web/src/main/webapp/solr -Dorg.orcid.config.file=classpath:staging-persistence.properties -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true -XX:MaxPermSize=1024m
    ```
* Click Ok

* In Timeouts, increase the time limit of Start to 600 seconds and Stop to 100.

* Save and close the server configuration view.

* Right click on "Apache Tomcat Server 7.0".

* Select "Add and Remove" Add orcid-api-web, orcid-pub-web, orcid-scheduler-web, orcid-solr-web, orcid-integration-test and orcid-web

* Right click on "Apache Tomcat Server 7.0"

* Select Debug

* Point your browser to http://localhost:8080/orcid-web/my-orcid

* You should see a login page.

* Click OK.

### Setting up Eclipse to use ORCID formatting rules 
1. Select Eclipse (or Spring Tool Suit) -> Preferences -> Java -> Code style -> Formatter -> Import

* Navigate to ~/git/ORCID-Source and select eclipse_formatter.xml

* Click "Apply"

2. Select Eclipse (or Spring Tool Suit) -> Preferences -> JavaScript -> Code style -> Formatter -> Import

* Navigate to ~/git/ORCID-Source and select eclipse_javascript_formatter.xml

* Click "Apply"

# Testing
## Maven test

1. cd to [ORCID-Source]

2. run maven test ```mvn test```

## Integration tests

See [orcid-integrations-test/README.md](https://github.com/ORCID/ORCID-Work-in-Progress/blob/master/orcid-integrations-test/README.md)

* Finally help out by improving these instructions!    
   
