Feature: Rest information to json file

@jenkins
Scenario: Get information from REST service and write it to json file
Given send request to rest service
And write the information to json file
And read the information from the file and save it
Then check the jenkins DB and verify the records with the saved information

