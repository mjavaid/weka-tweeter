weka-tweeter
====

**weka-tweeter** is a project for the [CSI4107 Course](http://www.site.uottawa.ca/~diana/csi4107/) at the University of Ottawa. The goal is to try to provide a viable solution for the problems posed in [Assignment 2](http://www.site.uottawa.ca/~diana/csi4107/A2_2014.htm) for the course.

#### Table of Content
* [Overview](#overview)
* [Project Tasks](#project-tasks)
  * [ARFF Generation](#arff-generation)
  * [ARFF Optimization](#arff-optimization)
    * [Original](#original)
    * [Stopword Removal & Stemming](#stopword-removal--stemming)
    * [Regex Handling](#regex-handling)
    * [Polarity Instance Weighing](#polarity-instance-weighing) 
    * [Neutral Instance Weighing](#neutral-instance-weighing)
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
| Task | Status |
| ---- |:------:|:-----:|
| [ARFF Generation](#arff-generation) | `Complete` |
| [ARFF Optimization](#arff-optimization) | `Complete` |
| [Result Generation](#result-generation) | `Complete` |
| [Report Creation](#report-creation) | `Not Started` |

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
$ # Mac OSX
$
$ java -jar weka-tweeter.jar <data_source_path> [<relation_name>]
```

The output file, `test.arff`, is created and stored in the same directory as the `weka-tweeter.jar` file.

====

#### ARFF Optimization
This section details the various steps taken to optimize the ARFF file generation to improve the overall classification results. The following optimization steps were taken, details of which can be found in the subsequent sections:
* [Original](#original)
* [Stopword Removal & Stemming](#stopword-removal--stemming)
* [Regex Handling](#regex-handling)
* [Polarity Instance Weighing](#polarity-instance-weighing) 
* [Neutral Instance Weighing](#neutral-instance-weighing)

###### Original
This is the initial classification result. The data has not be modified in any way and has been converted to an ARFF file as is.

The following table lists the Weka Classification results generated for the unaltered data. Note that the **J48** classifier data is unavailable as for 10-fold Cross-Validation, the tree created was too large for Weka to process in a timely manner. By comparing with the rest of the results generated, we observe an approximate relation between the different classifiers and can hence predict the **Correct Classifications** to be around **55 %**.

| Classifier | Correct Classifications | Incorrect Classifications | Model Build Time |
| ---- |:----:|:----:|:----:|
| **NavieBayes** | 52.1024 % | 47.8976 % | 4.37 s |
| **SMO** | 59.6957 % | 40.3043 % | 332.05 s |
| **J48** | - | - | - |
| **ZeroR (Baseline)** | 45.4219 % | 54.5781 % | 0.01 s |

###### Stopword Removal & Stemming
The original implementation simply removed **StopWords** from each tweet and **Stemmed** words to convert them to their basic form.

Both the StopWord implementation and Stemming application were leveraged from **Weka** core. For Stemming, there were multiple implementations available that could be integrated with Weka's stemmer tool but we opted for the **SnowballStemmer**.

The following table lists the results of Weka Classification for the ARFF file generated for this optimization implementation. Note that the **ZeroR (Baseline)** data is not available. This is because the decision to collect baseline results was taken after this implementation was already replaced by subsequent implementations.

| Classifier | Correct Classifications | Incorrect Classifications | Model Build Time |
| ---- |:----:|:----:|:----:|
| **NavieBayes** | 52.0194 % | 47.9806 % | 5.05 s |
| **SMO** | 58.8935 % | 41.1065 % | 180.07 s |
| **J48** | 54.9378 % | 45.0622 % | 407.3 s |
| **ZeroR (Baseline)** | - | - | - |

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
| **ZeroR (Baseline)** | 45.4219 % | 54.5781 % | 0 s |

###### Polarity Instance Weighing
For this optimization step, the preprocessing algorithm as modified to calculate the weights of the data instances. The weights were dependent on the category of the instance. For instances that were declared **positive** or **negative**, the number of the positive and negative words (respectively) present in the instance determined its weight. A higher number of positive or negative words resulted in a higher weight. The `Default Weight` of an instance was set to **1**.

For this step, **neutral** instances were not considered and were assigned the default weight. Also, a list of positive and negative terms were obtained from `General Inquirer` provided by [Prof. Diana Inkpen](http://www.site.uottawa.ca/~diana/) of University of Ottawa. The files can be obtained directly from the following link:
* [General Inquirer Polarity Data](http://www.site.uottawa.ca/~diana/csi4107/tweet/GI.zip)

The following table lists the result of the Weka Classification for the ARFF file generated for this optimization implementation:

| Classifier | Correct Classifications | Incorrect Classifications | Model Build Time |
| ---- |:----:|:----:|:----:|
| **NavieBayes** | 62.7078 % | 37.2922 % | 4.63 s |
| **SMO** | 69.0511 % | 30.9489 % | 166.37 s |
| **J48** | 65.6066 % | 34.3934 % | 502.15 s |
| **ZeroR (Baseline)** | 59.9966 % | 40.0034 % | 0 s |

###### Neutral Instance Weighing
For this optimization step, the weighing algorithm was extended to handle neutral cases. The implementation is such that instances that contain that same number of positive and negative terms receive a higher weightage when their **category** attribute is classified as **neutral**. Instances that have more negative or positive terms have lower weights associated with them, and the magnitude of the weight depends on how far the polarity deviates from a neutral one.

The following table lists the result of the Weka Classification for the arff file generated for this optimization implementation:

| Classifier | Correct Classifications | Incorrect Classifications | Model Build Time |
| ---- |:----:|:----:|:----:|
| **NavieBayes** | 74.9304 % | 25.0696 % | 3.71 s |
| **SMO** | 77.9335 % | 22.0665 % | 51.31 s |
| **J48** | 75.8607 % | 24.1393 % | 169.45 s |
| **ZeroR (Baseline)** | 64.0139 % | 35.9861 % | 0 s |

====

#### Result Generation
This section compares and discusses the results generated by this program. The overall results for each classifier for different levels of optimizations are listed in the [ARFF Optimization](#arff-optimization) section above. For more detailed results, directly from the Weka, please go the [weka-tweeter/Results](https://github.com/mjavaid/weka-tweeter/tree/master/Results) directory.

====

#### Report Creation
Task details for Report Generation

## Important Links
* [Question Statement](http://www.site.uottawa.ca/~diana/csi4107/A2_2014.htm)
* [Assignment Corpus](http://www.site.uottawa.ca/~diana/csi4107/semeval_twitter_data.txt)
* [Weka Download Center](http://www.cs.waikato.ac.nz/ml/weka/downloading.html)
* [SnowballStemmer Jar](http://weka.wikispaces.com/file/view/snowball-20051019.jar/82917267/snowball-20051019.jar)

