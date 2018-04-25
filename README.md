### Basic ChatBot Demo for Henosisknot.com
### Version 0.1.0

This is a minimalistic chatbot for my blog (henosisknot.com). It does basic semantic parsing and response using
Stanford's NLP Toolkit available here: https://stanfordnlp.github.io/CoreNLP/.

Dependencies:

Standard Programming
    - Protobuf
    - Java_JDK 1.8

ML/Deep Learning
    - Deep4J 0.9.1
    - StanfordNLP
    - LibSVM
    - Weka 3.8.0

Logging
    - slf4j
    - nd4j

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

### Presentation!!!

To check out  presentation on the chatbot, check out the link below:
https://docs.google.com/presentation/d/1aFKP6F4bJowuPEdZo4se6ZYTFuWVr0qsWwc5lEjqnYQ/edit?usp=sharing

### Architecture and Notes

![Architecture](https://github.com/andorsk/Chatbot-Henosisknot/blob/master/chatbot.jpg?raw=true)


**Rules Engine**

Rules engine allows users to input Regex into a rules.json file and have it matched. The rules.json file is a
wrapper for the traditional format of Stanfords NLP library. There's a few fields that can be added into the rules.json
file that make it more dynamic than the normal Stanfords NLP library.

Current supported additional fields:
verify_true - statement that should match with the regex provided and registers true. Will be checked upon each time the rules are loaded.
verify_false - statement that should not match with the regex provided and registers false. Will be checked upon each time the rules are loaded.

**NLG Models:**

Retrieval Models:
The training is heavily based off of SMN model by Wu et. al delivered in 2017. It is a sequence to sequence network
and a retrieval agent. Check out the presentaton here:

NLG Models are based heavily off of ____

**Training Data:**


**Prebuilt Models:**


### Resources for Further Learning
Check out http://meta-guide.com/bibliography/100-best-theses-in-ai-nlp-conversational-agents for links on more resources on NLP QA Agents.