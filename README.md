##Scrava

An asynchronous, non-blocking  Scala client for the Strava API built upon Play's WS library.

[![Build Status](https://travis-ci.org/kiambogo/Scrava.svg?branch=master)](https://travis-ci.org/kiambogo/Scrava)

###Using the getAll higher order function
The Strava API offers the ability to page through result sets when the corresponding result to a request has many results. Scrava offers the ability to fetch all and merge into a single List. To do this, call upon the *getAll()* higher order function, passing into it the function you wish to invoke. You can pass in other required parameters as needed.

For example, if you want t retrieve all the activities of the current athlete
```scala
import scrava.controllers._
import scrava.models._
AutoPagination.getAll(client.listAthleteActivities _)
```

The underscore at the end marks it as a partially applied function, leaving the rest of the parameters to be curried. 



