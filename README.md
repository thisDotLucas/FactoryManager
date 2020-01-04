# Factory Manager

#### This system is meant to keep track of employees and the work progress in our "factory". It is inspired by a system that i have used myself but with some added features like communication between employees and managers and a user interface that is easier to use. 

## Get started

### Worker
In the login view you need to input a user name and key these are located in users.txt. If logged in as a worker you need to check in and then input a work number which can
be found in worksteps.txt. When the workstep is "done" you input the amount done and trash amount and end the workstep. When the day is over the worker checks out. Workers are able to use the call manager button to send anotification to their manager 
as soon as they log in to the system. Messages from the manager can also be read from the message box.

### Manager
If you log in as a manager you are able to send messages to your employees and you can get their work history for any date. You are able to add and delete worksteps or edit them. You will also get notifications if
any workers needs assistance upon logging in.

## Data storage
All data is stored in a MySQL database running on Amazon RDS (free version). Querys and updates are not that fast but the MySQL dump 
User names and keys can be found in users.txt and worksteps and their id:s in worksteps.txt.
All worksteps, work logs, messages, notifications and employee information is stored in the database.


