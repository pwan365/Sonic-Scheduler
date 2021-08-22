#Visualisation

## Goal
For this project, we were required to provide a graphical user interface that gives live visual feedback of the task 
scheduler. Furthermore, we want to present it in an interesting way as the algorithm exhaustively searches all the 
possible states.

## Brainstorming
We first brainstormed a few ideas about what information we wanted to present and what the layout of the GUI should 
look like. We came up with a colour scheme and a few low-fidelity prototypes. This process was completed swiftly to
ensure we spent less time on the design now and rather later when we had accomplished the implementation. By the end 
of the brainstorming phase, we decided to develop the user interface based on the prototype showing below:
![Prototype Design](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/guiPrototype.png)

## Implementation
We used the JavaFX library alongside the FXML file to develop the GUI. Scene Builder was utilised to help create the FXML 
markup, which is more convenient and intuitive for GUI layout development. The `Controller` class is responsible for 
the logic of the GUI. While a `Visualiser` class, which extends the Application class in javafx, loads `visualisation.fxml` 
and passes in arguments to the `Controller` when it gets called by the `Main` class. We used the `AnimationTimer` class 
to keep the GUI up to date with the algorithm to show real time changes of the best partial schedule in the GUI.

Additionally, we have a class called `scheduleThread` which extends `Thread`. It is used to enable the algorithm on 
another thread alongside the main thread that runs the GUI. One instance of the scheduleThread will be passed into the 
Controller. When the start button is clicked, the Controller will call the `start()` method in scheduleThread 
to start the algorithm. The Controller will then periodically poll for information from the scheduleThread class to 
updating the GUI.

## Final Outcome
After the logic of the GUI was finalised, we focused more on the aesthetics of the GUI. We decided to lower the 
variety of colors on screen and have more images to make the GUI look more interesting and give off a professional
tone. By adding more visual elements, captures the users' attention and differentiates our design from others.
The final user interface developed is shown below:
![Final Design](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/guiFinal.png)

## Issues
An issue we encountered was attempting to add javafx dependency to the project. When the project was first initialised, 
there was no javafx dependency added. When we tried to add javafx to for milestone 2, we encountered a lot of 
incompatibility issues.

Another issue that arose, was translating live data fetched from the `BestSchedule` instance.

## References
All illustrations are by <a href="https://storyset.com/">Storyset</a> and are free of use provided that they are attributed.