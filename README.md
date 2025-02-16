
# Lancaster Employee GUI

This repository is used to store the code for Group 44's team project.

# Project Overview

We are using javaFX to develop the interface, we are also following MVC format (Model, View, Controller).

Currently the project is set up as such:

/src  
📦 main.java.com.lancaster  
 ┣ 📂 model          → Data models (Review, Event)  
 ┣ 📂 view           → GUI components (LoginScreen, BookingScreen)   
 ┣ 📂 controller     → Logic to control views  
 ┣ 📂 api            → Interfaces for other departments (CalendarDataProvider, ReviewDataProvider)  

# Making Changes

Before starting any new feature/addition, make sure your local repo is up to date using:
 ```
git pull
```
It is good practice to develop new features in a separate branch off of main, you can do so with:
```
// Ensure you are on the main branch
git checkout main
// Create a new branch and switch to it
git checkout -b YOUR BRANCH NAME GOES HERE
```
You can then develop your changes on this branch. When you are done, stage and commit your changes:
```
// Stage changes
git add .
// Commit changes
git commit -m "YOUR MESSAGE GOES HERE"
```
When commiting, be descriptive of your changes, explain why you did it, what it changed, etc.

Push these commits onto the remote repository branch:
```
git push origin YOUR BRANCH NAME GOES HERE
```

Create a pull request from your branch to the main branch through the pull requests tab. It will then be reviewed by another member and if approved, can be merged into the main branch.

You can then delete the feature branch if necessary.
