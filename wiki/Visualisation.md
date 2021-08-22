# Visulaisation

## Goal
To provide a graphical user interface that gives live visual feedback of the task scheduler in an interesting way as 
the algorithm exhaustively searches all the possible states.

## Brainstorming
We first brainstormed a few ideas about what information we wanted to present and what the layout of the GUI should 
look like. We came up with a few low-fidelity prototypes. By the end of the brainstorming phase, we decided to develop 
the user interface based on the prototype showing below:

![Prototype Design](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/guiPrototype.png)
## Implementation
We used the JavaFX library to develop the GUI alongside the fxml file. Scene Builder was used to help create the fxml file for the GUI which is a more convenient and intuitive way for GUI layout development. The Controller class is responsible for the logic of the GUI, while a Visualiser class, which extends the Application class in javafx, loads “visualisation.fxml” and passes in arguments to the Controller class when it gets called by the Main class. We used the AnimationTimer class to keep the GUI up to date with the algorithm to show real time changes of the best partial schedule in the GUI.

We have a class called scheduleThread which extends Thread. It is used to enable running the algorithm on another thread alongside the main thread that runs the GUI. One instance of the scheduleThread will be passed into the Controller class. When the start button is clicked, the Controller will call the start() method in scheduleThread to start the algorithm. The Controller will then periodically poll information from the scheduleThread class for updating the GUI.

## Final Outcome
After the logic of the GUI has been finalised. We focused more on the aesthetics of the GUI. We decided to lower the variety of colors on screen and have more images to make the GUI look more interesting to the users as shown below:
![Final Design](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/guiFinal.png)

## Issues
One issue would be adding javafx dependency to the project. When the project first initialised, there was no javafx dependency added. When we tried to add javafx to for milestone 2, we encountered a lot of incompatibility issues.
One other issue would be translating live data fetched from...
