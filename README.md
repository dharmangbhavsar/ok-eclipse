
# Ok Eclipse - CSC 510 Software Engineering (Spring 2018) project - Group 'D' <Enter>[](https://github.com/dharmangbhavsar/ok-eclipse) 

## About
Extending the efforts of Group 'O' during the previous iteration of the project. To include other software development life cycle elements in to the mix in Eclipse like unite testing and git integration.

## Installing Ok Eclipse

1. Download & extract this [ok_eclipse.zip](https://drive.google.com/open?id=1mSrwdvPe4_BD487jVamrombuNvbGmr3k), requires Java ( we use java version "1.8.0_151" ) on Windows machine.
1. Run the executable **eclipse.exe** inside the extracted eclipse folder. 
1. Select *Launch* if a workspace dialog appears.
1. You will see a progress dialog initializing **Ok Eclipse**. 
1. Installation is complete, if you get the below image.

![](https://github.com/snaraya7/Ok_Eclipse/blob/master/img/success1.JPG).

**And ya one more thing, please use a dedicated _microphone_ in a noise free environment.**

## Prerequisites  
1. JDK 8 or above  
1. Eclipse IDE  
1. Downloaded .zip file from the above section

## Demo (Recommended)
----------------

## Enhancements and new Features
Voice commands in the plugin can be given by pressing "Shift + Z".
The main features of the plugin are:-   
1) **Code Generation** - Generated classes, for loops and functions accroding to the description provided by the user.  
Use voice command "generate" and then say "a class with four private integer member variables". This will create a class with four private integer member variables. OR. You could write the above command in between "$" signs in the Eclipse environment and press "Shift + G". e.g. - $ generate a function with three string parameters $. This will generate a function with three string parameters.  
2) **Code Conversion** - Converts code from java to python. (*Requires network connection*)  
Write your java program and then give the voice command "convert". This will convert your file from Java to Python. The converted file will not necessarily run. But it will give a warning on the screen about the exact line that might case problems if the file is ran.  
3) **Git Integration** - git status, git commit, git push and git pull are implemented in the plugin.   
For git status - say "status" or press "Shift + S".  
For git commit - say "commit" or press "Shift + C".  
For git push - say "push" or press "Shift + P".  
For git pull - say "pull" or press "Shift + U".  
4) **Unit Testing** - performs unit testing on the function selected.  
First select the whole function in Eclipse environment and then give the voice commands "testing" or press "Shift + T".  
This will generate another function that will call the function to be tested on different parameters. Run the program again to see the results of the function being called on different parameters and the output given by the function. The results are shown in the Eclipse console and the module also shows results if errors or exceptions are thrown by Java.  
5) **Sphinx changes** - some changes to recognise full sentence.  
We added some stop time between each word in a sentece so that CMU Sphinx could recognise different words in a sentence. A user has to take a break of about 0.5 secs between each word spoken for the Sphinx system to understand the word correctly.  
6) **Added Shortcuts** - adding shortcuts for each feature.  
During user surveys some users found it prety difficult to work with the voice recognition system. So shortcuts for each feature were added and are written in the section of the feature itself.  

## @authors
This version: - 
[Dharmang Bhavsar](https://www.github.com/dharmangbhavsar) | [Vismay Golwala](https://github.com/vismay-golwala) | [Srija Ganguly](https://github.com/SrijaGanguly) | [Tushita Roychaudhury](https://github.com/tushitarc)

Previous version:-
[Shrikanth N C](https://www.linkedin.com/in/shrikanthnc/) | [Karthik Medidisiva](https://github.com/kmedidi)   | [Kashyap Sivasubramanian](https://github.com/ksivasu)   | [Charan Ram Vellaiyur Chellaram](https://github.com/cvellai)  




