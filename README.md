![Scrava](https://cloud.githubusercontent.com/assets/4472397/6699144/9a4ee65c-ccd5-11e4-9369-e89eea337b17.jpg)

A lightweight Scala client for the Strava API built upon ScalaJ-HTTP library and Lift JSON.

[![Build Status](https://travis-ci.org/kiambogo/scrava.svg?branch=master)](https://travis-ci.org/kiambogo/scrava)
[![GitHub version](https://badge.fury.io/gh/kiambogo%2Fscrava.svg)](https://badge.fury.io/gh/kiambogo%2Fscrava)

### Installation
Scrava is currently built for Scala 2.11 and 2.12. To use scrava in an sbt project, add the following to your project file:

    libraryDependencies += "com.github.kiambogo" %% "scrava" % "1.3.0"

### Sample Code

    val client = new ScravaClient("[accessToken]")
    val athlete = client.retrieveAthlete()

**Note:** Most functions have optional ID parameters (i.e, `retrieveAthlete()`). If no ID is provided, the function will operate based on the currently authenticated user. Therefore, `retrieveAthlete()` will return the athlete profile of the currently authenticated user (based on the access token provided), and `listAthleteFriends()` will return the list of friends of the currently authenticated athlete.

To retrieve perform these functions for a particular athlete/activity/etc, simply pass in the respective ID: `retrieveAthlete(Some([athleteID]))` or `listAthleteFriends(Some([athleteID]))`.

### Auto-Pagination

The Strava API offers the ability to page through result sets when the corresponding result set to a request is very large. Scrava offers the ability to perform auto-pagination, fetching the entire result set and merging into a single List. To utilize this, simply pass in `retrieveAll = true` into the respective function. Note that this will use additional requests to the API, and may exhaust rate limits in some cases.

-----------------------------
**Note:** The functions requiring Write permissions (`updateAthlete()`, `createActivity()`, `updateActivity()`, etc) are untested at this time.

If any errors or problems are found, feel free to open a pull request or issue.
