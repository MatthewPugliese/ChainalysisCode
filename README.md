In order to build and run the program, first you have to download the two folders from the github repository located at: https://github.com/MatthewPugliese/ChainalysisCode/

The two folders to pull are labeled ChainalysisTest and and chainalysis_frontend.

Open both of the folders in their own windows of any code editor, I use Visual Studio (VS) Code.  Before running either programs, run the command "npm install" in the terminal of the chainalysis_frontend window.  This ensures that your machine has the necessary React modules for the program.

Then start the ChainalysisTest program.  The java file that needs to be run is located under src > main > java > com > example > ChainalysisTest > ChainalysisTestApplication.java.  Run the file as you would any other .java file.  Ensure that the program is running, and looks like it is waiting for requests.

Once this has been done, keep that program running, and move to the chainalysis_frontend window.  To run this program, type in the command "npm start".  This starts the React program, which once compiled, will open a localhost server displaying the webpage.  The prices of Bitcoin and Ethereum should automatically begin updating every few seconds.

Make sure that you start the backend server (ChainalysisTestApplication.java) first as not doing so may lead to issues when the frontend is not able to complete the request to the backend for the data.

A simple video demonstrating how to start the program can be found at: https://www.youtube.com/watch?v=3PjG1_mNJgY


If there are any issues with building or running any aspect of the program, feel free to contact me at mfpugliese@colgate.edu or (914) 708-7991.
