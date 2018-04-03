### Basic ChatBot Demo for Henosisknot.com
### Version 0.1.0

This is a minimalistic chatbot for my blog (henosisknot.com). It does basic semantic parsing and response using
Stanford's NLP Toolkit available here: https://stanfordnlp.github.io/CoreNLP/.

Dependencies:

    - Protobuf
    - StanfordNLP
    - Java_JDK 1.8

### Install Guide

1. Make sure you have Maven installed
2. Run this command (osx): mvn clear install compile package;
    - This will install all the dependencies. Look at the pom.xml file for the versions of the libraries.
3. Download the base English NLP model here:
    https://stanfordnlp.github.io/CoreNLP/
    and put it the models directory

### Running the Program

#### Using Unix based Command Line

```
cd Henosisknot-Chatbot
mvn clear install compile package
java -jar <path-to-Chatbot-Henosisknot-0.1.0.jar>
```

** (Note:) This is a simple demo application. It is untested across architectures or alternate environments. **

### Architecture

![Architecture](https://github.com/andorsk/Chatbot-Henosisknot/blob/master/chatbot.jpg?raw=true)

### Resources for Further Learning
Check out http://meta-guide.com/bibliography/100-best-theses-in-ai-nlp-conversational-agents for links on more resources on NLP QA Agents.