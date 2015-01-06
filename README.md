#Scrava

A lightweight 	Scala client for the Strava API built upon ScalaJ-HTTP library and Lift JSON.

[![Build Status](https://travis-ci.org/kiambogo/Scrava.svg?branch=master)](https://travis-ci.org/kiambogo/Scrava)

###Sample Code
---------------------

    val client = new ScravaClient("[accessToken]")
	val athlete = client.retrieveAthlete()
	
**Note:** Most functions have optional ID parameters (i.e, `retrieveAthlete()`). If no ID is provided, the function will operate based on the currently authenticated user. Therefore, `retrieveAthlete()` will return the athlete profile of the currently authenticated user (based on the access token provided), and `listAthleteFriends()` will return the list of friends of the currently authenticated athlete (as an example).

To retrieve perform these functions for a particular athlete/activity/etc, simply pass in the respective ID: `retrieveAthlete(Some([athleteID]))` or `listAthleteFriends(Some([athleteID]))`.
	
###Auto-Pagination
---------------------
The Strava API offers the ability to page through result sets when the corresponding result set to a request is very large. Scrava offers the ability to perform auto-pagination, fetching the entire result set and merging into a single List. To utilize this, invoke the `getAll()` higher order function, passing into it the function you wish to invoke. You can pass in other required parameters as needed.

For example, if you want to retrieve all the activities of the current athlete
```scala
    val client = new ScravaClient("[accessToken]")
    client.getAll(client.listAthleteActivities _)
```

or if you want to list all the athletes you are mutually following with a particular athlete:

```scala
client.getAll(client.listMutualFollowing _, [athlete_id])
```
where athlete_id is the particular Athlete ID you wish to find mutual followers for, and is a parameter to the `listMutualFollowing()` function.

The underscore at the end marks it as a partially applied function, leaving the rest of the parameters to be curried. 

-----------------------------
**Note:** The functions requiring Write permissions (`updateAthlete()`, `createActivity()`, `updateActivity()`, etc) are untested at this time. 

If any errors or problems are found, feel free to open a pull request or issue. 