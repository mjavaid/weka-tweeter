weka-tweeter
====

**weka-tweeter** is a project for the [CSI4107 Course](http://www.site.uottawa.ca/~diana/csi4107/) at the University of Ottawa. The goal is to try to provide a viable solution for the problems posed in [Assignment 2](http://www.site.uottawa.ca/~diana/csi4107/A2_2014.htm) for the course.

#### Table of Content
* [Overview](#overview)
* [Project Tasks](#project-tasks)
  * [ARFF Generation](#arff-generation)
  * [ARFF Optimization](#arff-optimization)
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
| [Result Generation](#result-generation) | **Unassigned** | `Not Started` |
| [Report Creation](#report-creation) | **Unassigned** | `Not Started` |

#### ARFF Generation
Task details for ARFF Generation.

#### ARFF Optimization
Task details for ARFF Optimization.

#### Result Generation
Task details for Result Generation.

#### Report Creation
Task details for Report Generation

## Important Links
* [Question Statement](http://www.site.uottawa.ca/~diana/csi4107/A2_2014.htm)
* [Assignment Corpus](http://www.site.uottawa.ca/~diana/csi4107/semeval_twitter_data.txt)
* [Weka Download Center](http://www.cs.waikato.ac.nz/ml/weka/downloading.html)
* [SnowballStemmber Jar](http://weka.wikispaces.com/file/view/snowball-20051019.jar/82917267/snowball-20051019.jar)

