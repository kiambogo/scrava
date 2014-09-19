package controllers

/**
 * Created by christopher on 2014-09-15.
 */

import models.{Activity, AthleteSummary, SegmentEffort, Athlete}
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.ws.WS

import scala.concurrent.Future


case class Connection(
                       accessToken: String,
                       athlete: Athlete) {

  val authString = "Bearer " + accessToken

  def updateAthlete(parameters: Map): Future[Athlete] = {
    WS.url("https://www.strava.com/api/v3/athlete")
      .withHeaders("Authorization" -> authString)
      .put(parameters)
      .map { response =>
      response.json.validate[Athlete].fold(
        errors => throw new RuntimeException("Could not parse athlete"),
        athlete => athlete
      )}
  }

  def findAthlete(id: Int): Future[Athlete] = {
    WS.url(s"https://www.strava.com/api/v3/athlete/$id")
      .get()
      .map { response =>
      response.json.validate[Athlete].fold(
        errors => throw new RuntimeException("Could not parse athlete"),
        athlete => athlete
      )}
  }

  def listAthleteKOMs(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[SegmentEffort]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/koms")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      response.json.validate[List[SegmentEffort]].fold(
        errors => throw new RuntimeException("Could not parse list of KOMs"),
        koms => koms
      )}
  }

  def listCurrentAthleteFriends(page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url("https://www.strava.com/api/v3/athlete/friends")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of friends (AthleteSummary)"),
        friends => friends
      )}
  }

  def listAthleteFriends(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/friends")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of friends (AthleteSummary)"),
        friends => friends
      )}
  }

  def listCurrentAthleteFollowers(page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url("https://www.strava.com/api/v3/athlete/followers")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of followers (AthleteSummary)"),
        followers => followers
      )}
  }

  def listAthleteFollowers(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/followers")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of followers (AthleteSummary)"),
        followers => followers
      )}
  }

  def listMutualFollowing(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/both-following")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of followers (AthleteSummary)"),
        followers => followers
      )}
  }

  def createActivity(name: String, `type`: String, startDateLocal: DateTime, elapsedTime: Int, description: Option[String], distance: Option[Float]): Future[Activity] = {
    WS.url("https://www.strava.com/api/v3/activities")
      .withHeaders("Authorization" -> authString)
      .post(Map(
      "name" -> name,
      "elapsed_time" -> elapsedTime,
      "distance" -> distance,
      "start_date_local" -> startDateLocal,
      "type" -> `type`
    ))
      .map { response =>
      response.json.validate[Activity].fold(
        errors => throw new RuntimeException("Could not parse athlete"),
        activity => activity
      )}
  }

  def retrieveActivity(id: Int, includeEfforts: Option[Boolean]): Future[Activity] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id")
      .withQueryString("include_all_efforts" -> includeEfforts.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[Activity].fold(
        errors => throw new RuntimeException("Could not parse activity"),
        activity => activity
      )

    }
  }
}
