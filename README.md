weka-tweeter
====

**weka-tweeter** is a project for the [CSI4107 Course](http://www.site.uottawa.ca/~diana/csi4107/) at the University of Ottawa. The goal is to try to provide a viable solution for the problems posed in [Assignment 2](http://www.site.uottawa.ca/~diana/csi4107/A2_2014.htm) for the course.

#### Table of Content
* [Overview](#overview)
* [Project Tasks](#project-tasks)
  * [ARFF Generation](#arff-generation)
  * [ARFF Optimization](#arff-optimization)
    * [Original](#original)
    * [Regex Handling](#regex-handling)
    * [Instance Weighing](#instance-weighing) 
  * [Result Generation](#result-generation)
  * [Report Creation](#report-creation)
* [Important Links](#important-links)

## Overview
This project develops an [arff file](http://www.cs.waikato.ac.nz/ml/weka/arff.html) generator that can be used with [Weka](http://www.cs.waikato.ac.nz/ml/weka) to perform Message Polarity Classification. The data corpus is comprised of approximately 8000 twitter messages that have either a **Postive**, **Negative**, **Neutral** or **Objective** opinion. For the purposes of this assignment, the **Neutral** and **Objective** opinions are classified collectively as **Neutral**.

Once the arff file has been generated, it shall be passed to the **Weka** application to generate classifications. The following classifiers from Weka shall be used:
* [Support Vector Machine (SMO in Weka)](http://en.wikipedia.org/wiki/Support_vector_machine)
* [NaiveBayes](http://en.wikipedia.org/wiki/Naive_Bayes_classifier)
* [Decision Trees (J48 in Weka)](http://en.wikipedia.org/wiki/C4.5_algorithm)

## Project Tasks
| Task | Assignment | Status |
| ---- |:------:|:-----:|
| [ARFF Generation](#arff-generation) | **Assigned:** [Sajawal Javaid](https://github.com/mjavaid) | `Complete` |
| [ARFF Optimization](#arff-optimization) | **Assigned:** [Sajawal Javaid](https://github.com/mjavaid) | `In Progress` |
| [Result Generation](#result-generation) | **Assigned:** [Sajawal Javaid](https://github.com/mjavaid) | `In Progress` |
| [Report Creation](#report-creation) | **Unassigned** | `Not Started` |

====

#### ARFF Generation
The `ARFFGenerator` class reads the corpus file from the filepath provided as an argument. An optional argument of the `Instance Name/Relation` can also be passed to the class.

The corpus is parsed and preprocessed and an arff format file is created from the preprocessed data. Following is the format of the arff file generated:

```
% <relation_name> DEFAULT = opinion
@relation <relation_name>

@attribute sentence string
@attribute category {positive,negative,neutral}

@data
'I\'m GSP fan, hate Nick Diaz. can\'t wait february.',negative
```

The code can be run with the following command through the command line interface or by importing the project into Eclipse. Please note that to run the code from CLI, you must add the **weka.jar** and **snowballstemmer.jar** files to the `CLASSPATH`. Both these files can be obtained from links in the [Important Links](#important-links) section:

```
$ java ARFFGenerator.java <data_source_path> [<relation_name>]
```

The output file, `test.arff`, is created and stored in the `Resources/` directory.

====

#### ARFF Optimization
This section details the various steps taken to optimize the ARFF file generation to improve the overall classification results. The following optimization steps were taken, details of which can be found in the subsequent sections:
* [Original](#original)
* [Regex Handling](#regex-handling)
* [Instance Weighing](#instance-weighing)

###### Original
The original implementation simply removed **StopWords** from each tweet and **Stemmed** words to convert them to their basic form.

Both the StopWord implementation and Stemming application were leveraged from **Weka** core. For Stemming, there were multiple implementations available that could be integrated with Weka's stemmer tool but we opted for the **SnowballStemmer**.

The following table lists the results of Weka Classification for the ARFF file generated for this optimization implementation:

| Classifier | Correct Classifications | Incorrect Classifications | Model Build Time |
| ---- |:----:|:----:|:----:|
| **NavieBayes** | 52.0194 % | 47.9806 % | 5.05 s |
| **SMO** | 58.8935 % | 41.1065 % | 180.07 s |
| **J48** | 54.9378 % | 45.0622 % | 407.3 s |

###### Regex Handling
For this optimization step, additional **Regex Handling** was added. Specifically, the following regex filters were implemented:

| Filter | Description | Expression |
| ---- | ---- |:----:|
| Numbers | Remove numbers from the data set | `[0-9]` |
| Special Characters | Remove special characters such as `{ !, @, #}` etc. | `[\\W&&[^\\s{1}]]` |

Additional Regex Handlers may be added as the project progress, and the current ones may be updated.

The following table lists the result of Weka Classification for the ARFF file generated for this optimization implementation:

| Classifier | Correct Classifications | Incorrect Classifications | Model Build Time |
| ---- |:----:|:----:|:----:|
| **NavieBayes** | 53.3057 % | 46.6943 % | 3.64 s |
| **SMO** | 59.3776 % | 40.6224 % | 126.26 s |
| **J48** | 54.7165 % | 45.2835 % | 417.33 s |

###### Instance Weighing
Instance Weighing Optimization

====

#### Result Generation
Task details for Result Generation.

====

#### Report Creation
Task details for Report Generation

## Important Links
* [Question Statement](http://www.site.uottawa.ca/~diana/csi4107/A2_2014.htm)
* [Assignment Corpus](http://www.site.uottawa.ca/~diana/csi4107/semeval_twitter_data.txt)
* [Weka Download Center](http://www.cs.waikato.ac.nz/ml/weka/downloading.html)
* [SnowballStemmer Jar](http://weka.wikispaces.com/file/view/snowball-20051019.jar/82917267/snowball-20051019.jar)

