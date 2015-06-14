# TwitterMap
Author: Suraj Iyer (surajiyer96)

# Description
An application for mapping real-time tweets on google maps and also to write tweets to CSV file and a MySQL databases for later analysis. Great tool for enabling young Big Data analysts to jump right into the analysis part without worrying about the technical aspects of retrieving twitter data. Input any combination of keywords, select language in which to track said keywords and start. As a bonus, the keyword will also be translated into the selected languages to expand tweets to scan regional languages.

# Manual
- Clone the repository to Desktop.
- Create a "lib" folder in the project folder.
- Add all the libraries stated in the following Requirements section to the "lib" folder.

# Requirements
- JxBrowser Library with the license evaluation (http://www.teamdev.com/jxbrowser)
  - jxbrowser-5.1.jar
  - jxbrowser-linux32-5.1.jar
  - jxbrowser-linux64-5.1.jar
  - jxbrowser-mac-5.1.jar
  - jxbrowser-win-5.1.jar
  - evaluation.jar (license file)
- Stanford CoreNLP library (http://nlp.stanford.edu/software/corenlp.shtml)
  - stanford-corenlp-3.5.2.jar
  - stanford-corenlp-3.5.2-models.jar
  - ejml-0.23.jar
- Apache Common Langs library (http://commons.apache.org/proper/commons-lang/download_lang.cgi)
  - commons-lang3-3.4.jar
- Twitter4j Library (http://twitter4j.org/en/index.html)
  - twitter4j-async-4.0.3.jar
  - twitter4j-core-4.0.3.jar
  - twitter4j-examples-4.0.3.jar
  - twitter4j-media-support-4.0.3.jar
  - twitter4j-stream-4.0.3.jar
- JDBC Driver for MySQL (Connector/J) (https://www.mysql.com/products/connector/)
  - mysql-connector-java-5.1.35-bin.jar

# Screenshots
- Main window with a tweet marker + infowindow with clickable links, mentions and hashtags.
  [screenshot] (screenshots/tweet marker info window.PNG?raw=true)
- Marker clustering
  [screenshot](screenshots/marker clustering.PNG?raw=true)
