# Software Engineering Fundamentals Project
Semester long project for the course Software Engineering Fundamentals
# About
This program is a recreational sports league management system. Users can create acounts of 
different tiers, spectator, player, or team manager. All accounts can follow teams, view 
teams/events, and comment on team pages, players can request to join teams, and managers 
can set up teams and events, as well as update their team's scores.
# Installation for IntelliJ
1. Download the zip of this project and extract to desired directory
2. Click Import Project from splash screen or File->New->Project from Existing Source Code
3. Select extracted folder and click OK
4. Click Next->Name Project and set location->Click Next until Finish button appears->Click Finish 
5. Navigate to Main class(src\sample\Main)
6. Right click Main class and click Run 'Main.main()'
# Installation for Eclipse
1. Download the zip of this project and extract to desired directory
2. Click File->Import
3. Select General->Projects from Folder or Archive then click Next
4. Click Directory, select extracted folder, and click OK
5. Click Finish
6. Navigate to Main class(src\sample\Main)
7. Click Run button
# Known Issues/Limitations
- Text files that back up data can be manipulated externally
- No verification for certain actions:
  - Remove player from team
  - Cancel/Deactivate events
  - Changing account tier from Manager to anything else (Deletes Manager account’s team if user goes through with their edits)
- PNGs are the only allowed file type for profile pictures
- Files are only read at the beginning of running and written at the end of running the application
# Credits
Author: Gregorio Lozada</br>
Java Code: Gregorio Lozada, Jake Sherman, Jordan Moses, Jordan Sasek</br>
FXML Code: Guilherme Pereira, Andrew Wilson</br>
CSS Code: Guilherme Pereira
