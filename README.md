# contactsOrion
Simple android4+ app parsing JSON

App is downloading and parsing json from http://jsonplaceholder.typicode.com/users data is then 
stored in internal persistent storage via Reservoir. This cached data is then reused while app is used.

Data model is stored within classes (POJO) - retrofit standard.

Material Dialogs (progress/error dialogs)
https://github.com/afollestad/material-dialogs

REST API
https://square.github.io/retrofit/

Storing library (used for caching purposes)
https://github.com/anupcowkur/Reservoir
