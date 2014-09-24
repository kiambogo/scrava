package controllers

/**
 * Created by christopher on 2014-09-15.
 */

import models.JsonProtocol._
import models._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Connection (
  accessToken: String,
  athlete: Athlete) {

  val authString = "Bearer " + accessToken

  def updateAthlete(city: String, state: String, country: String, sex: String, weight: Float): Future[Athlete] = {
    WS.url("https://www.strava.com/api/v3/athlete")
      .withHeaders("Authorization" -> authString)
      .put(Map(
        "city" -> Seq(city),
        "state" -> Seq(state),
        "country" -> Seq(country),
        "sex" -> Seq(sex),
        "weight" -> Seq(weight.toString)))
      .map { response =>
      response.json.validate[Athlete](athleteReads).fold(
        errors => throw new RuntimeException("Could not parse athlete"),
        athlete => athlete
      )}
  }

  def findAthlete(id: Int): Future[Athlete] = {
    WS.url(s"https://www.strava.com/api/v3/athlete/$id")
      .get()
      .map { response =>
      response.json.validate[Athlete](athleteReads).fold(
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
      response.json.validate[List[SegmentEffort]](segmentEffortReads).fold(
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
      "name" -> Seq(name),
      "elapsed_time" -> Seq(elapsedTime.toString),
      "distance" -> Seq(distance.get.toString),
      "start_date_local" -> Seq(startDateLocal.toString(ISODateTimeFormat.dateTime())),
      "type" -> Seq(`type`)
    ))
      .map { response =>
      response.json.validate[Activity].fold(
        errors => throw new RuntimeException("Could not parse athlete"),
        activity => activity
      )}
  }

  def retrieveActivity(id: Long, includeEfforts: Option[Boolean]): Future[Activity] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id")
      .withQueryString("include_all_efforts" -> includeEfforts.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[Activity](activityReads).fold(
        errors => throw new RuntimeException("Could not parse activity"),
        activity => activity
      )}
  }

  def updateActivity(
    id: Long,
    name: Option[String],
    `type`: Option[String],
    `private`: Option[Boolean],
    commute: Option[Boolean],
    trainer: Option[Boolean],
    gearId: Option[String],
    description: Option[String]): Future[Activity] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id")
      .withQueryString("name" -> name.iterator.next())
      .withQueryString("type" -> `type`.iterator.next())
      .withQueryString("private" -> `private`.iterator.next().toString)
      .withQueryString("commute" -> commute.iterator.next().toString)
      .withQueryString("trainer" -> trainer.iterator.next().toString)
      .withQueryString("gear_id" -> name.iterator.next())
      .withQueryString("description" -> name.iterator.next())
      .put(Map(
      "id" -> Seq(id.toString),
      "name" -> Seq(name.get),
      "type" -> Seq(`type`.get),
      "private" -> Seq(`private`.get.toString),
      "commute" -> Seq(commute.get.toString),
      "trainer" -> Seq(trainer.get.toString),
      "gearId" -> Seq(gearId.get),
      "description" -> Seq(description.get)))
      .map { response =>
      response.json.validate[Activity](activityReads).fold(
        errors => throw new RuntimeException("Could not parse activity"),
        activity => activity
      )}
  }

  def deleteActivity(id: Long): Future[Boolean] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id")
      .delete()
      .map { response =>
      response.status.equals(204)
    }
  }

  //  def listAthleteActivities(before: Option[Int], after: Option[Int], page: Option[Int], perPage: Option[Int]): List[ActivitySummary] = {
  //
  //  }
}
