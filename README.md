### Basic ChatBot Demo for Henosisknot.com
### Version 0.1.0

This is a minimalistic chatbot for my blog (henosisknot.com). It does basic semantic parsing and response.

Dependencies:
    - Protobuf
    - StanfordNLP

### Install Guide

1. Make sure you have Maven installed
2. Run this command (osx): mvn clear install compile package;
    - This will install all the dependencies. Look at the pom.xml file for the versions of the libraries.

### Running

#### Unix based Command Line

```
cd Henosisknot-Chatbot
mvn clear install compile package
java -jar <path-to-Chatbot-Henosisknot-0.1.0.jar>
```

** (Note:) This is a simple demo application. It is untested across architectures or alternate environments. **