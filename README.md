#Scrava

A Scala client for the Strava API built upon ScalaJ-HTTP library and Lift JSON.

[![Build Status](https://travis-ci.org/kiambogo/Scrava.svg?branch=master)](https://travis-ci.org/kiambogo/Scrava)

###Using the getAll higher order function
The Strava API offers the ability to page through result sets when the corresponding result to a request has many results. Scrava offers the ability to fetch all and merge into a single List. To do this, call upon the `getAll()` higher order function, passing into it the function you wish to invoke. You can pass in other required parameters as needed.

For example, if you want to retrieve all the activities of the current athlete
```scala
import scrava.controllers._
import scrava.models._
AutoPagination.getAll(client.listAthleteActivities _)
```

or if you want to list all the athletes you are mutually following with a particular athlete:

```scala
AutoPagination.getAll(client.listMutualFollowing _, athlete_id)
```
where athlete_id is the particular Athlete ID you wish to find mutual followers for, and is a parameter to the `listMutualFollowing()` function.

The underscore at the end marks it as a partially applied function, leaving the rest of the parameters to be curried. 

##TODO
- Activity Upload
- Athlete Update
- Activity Update
- Activity Delete
- Check Activity Upload status



