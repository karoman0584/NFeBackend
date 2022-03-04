# NFE Backend

This project is the backend of the NFe XML processor

### Requirements

- local mysql server
- jdk 11

### How to Run

- clone the project
- download maven dependencies
- create a database with this name: nfeprocess
- change the user and password in lines 2 and 3 of the application.properties
- create in C this folder structure:
  NFE/input | NFE/error | NFE/output
- run the project
- to test the uploading of NFe xml in the frontend here there is an example file(nfe_example.xml) to try
