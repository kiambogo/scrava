package controllers

/**
 * Created by christopher on 2014-09-15.
 */

import models._
import net.liftweb.json._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.Logger._
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ScravaClient(accessToken: String) {

  implicit val formats = DefaultFormats
  val authString = "Bearer " + accessToken

  // Athlete-Related Functions

  def retreiveCurrentAthlete(): Future[Athlete] = {
    WS.url(s"https://www.strava.com/api/v3/athlete")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      parse(response.body).extract[Athlete]
    }
  }

  def retreiveAthlete(id: String): Future[AthleteSummary] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      parse(response.body).extract[AthleteSummary]
    }
  }

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
      parse(response.body).extract[Athlete] }
  }

  def listAthleteKOMs(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[SegmentEffort]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/athletes/$id/koms").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "resultsPerPage" -> resultsPerPage)
    tempMap.map(params => params._2 map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
      request.get()
        .map { response =>
          parse(response.body).extract[List[SegmentEffort]]
    }
  }

  def listCurrentAthleteFriends(page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    val params = StringBuilder.newBuilder
    if (page.isDefined || resultsPerPage.isDefined) params.append("?")
    if (page.isDefined) params.append("page=").append(page.get.toString)
    if (page.isDefined && resultsPerPage.isDefined) params.append("&per_page=").append(resultsPerPage.get.toString)
    else if (resultsPerPage.isDefined) params.append("per_page").append(resultsPerPage.get.toString)

    WS.url(s"https://www.strava.com/api/v3/athlete/friends$params")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      parse(response.body).extract[List[AthleteSummary]]
    }
  }

  def listAthleteFriends(id: String, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    val params = StringBuilder.newBuilder
    if (page.isDefined || resultsPerPage.isDefined) params.append("?")
    if (page.isDefined) params.append("page=").append(page.get.toString)
    if (page.isDefined && resultsPerPage.isDefined) params.append("&per_page=").append(resultsPerPage.get.toString)
    else if (resultsPerPage.isDefined) params.append("per_page").append(resultsPerPage.get.toString)

    WS.url(s"https://www.strava.com/api/v3/athletes/$id/friends$params")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      parse(response.body).extract[List[AthleteSummary]]
    }
  }

  def listCurrentAthleteFollowers(page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    val params = StringBuilder.newBuilder
    if (page.isDefined || resultsPerPage.isDefined) params.append("?")
    if (page.isDefined) params.append("page=").append(page.get.toString)
    if (page.isDefined && resultsPerPage.isDefined) params.append("&per_page=").append(resultsPerPage.get.toString)
    else if (resultsPerPage.isDefined) params.append("per_page").append(resultsPerPage.get.toString)

    WS.url(s"https://www.strava.com/api/v3/athlete/followers$params")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      parse(response.body).extract[List[AthleteSummary]]
    }
  }

  def listAthleteFollowers(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    val params = StringBuilder.newBuilder
    if (page.isDefined || resultsPerPage.isDefined) params.append("?")
    if (page.isDefined) params.append("page=").append(page.get.toString)
    if (page.isDefined && resultsPerPage.isDefined) params.append("&per_page=").append(resultsPerPage.get.toString)
    else if (resultsPerPage.isDefined) params.append("per_page").append(resultsPerPage.get.toString)

    WS.url(s"https://www.strava.com/api/v3/athletes/$id/followers$params")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      parse(response.body).extract[List[AthleteSummary]]
    }
  }

  def listMutualFollowing(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/both-following")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      parse(response.body).extract[List[AthleteSummary]]
    }
  }

  // Activity-Related Functions

  def listActivityComments(id: String, page: Option[Int], resultsPerPage: Option[Int]): Future[List[ActivityComments]] = {
    val params = StringBuilder.newBuilder
    if (page.isDefined || resultsPerPage.isDefined) params.append("?")
    if (page.isDefined) params.append("page=").append(page.get.toString)
    if (page.isDefined && resultsPerPage.isDefined) params.append("&per_page=").append(resultsPerPage.get.toString)
    else if (resultsPerPage.isDefined) params.append("per_page").append(resultsPerPage.get.toString)

    WS.url(s"https://www.strava.com/api/v3/activities/$id/comments$params")
    .get()
    .map { response =>
      parse(response.body).extract[List[ActivityComments]]
    }
  }

  def listActivityKudoers(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    val params = StringBuilder.newBuilder
    if (page.isDefined || resultsPerPage.isDefined) params.append("?")
    if (page.isDefined) params.append("page=").append(page.get.toString)
    if (page.isDefined && resultsPerPage.isDefined) params.append("&per_page=").append(resultsPerPage.get.toString)
    else if (resultsPerPage.isDefined) params.append("per_page").append(resultsPerPage.get.toString)

    WS.url(s"https://www.strava.com/api/v3/activities/$id/kudos$params")
      .get()
      .map { response =>
      parse(response.body).extract[List[AthleteSummary]]
    }
  }

  def listActivityPhotos(id: String): Future[List[Photo]] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/photos")
      .withQueryString()
      .get()
      .map { response =>
        parse(response.body).extract[List[Photo]]
    }
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
      println(response.json)
      parse(response.body).extract[Activity]
    }
  }

  def retrieveActivity(id: Long, includeEfforts: Option[Boolean]): Future[Either[Activity, ActivitySummary]] = {
    val params = StringBuilder.newBuilder
    if (includeEfforts.isDefined) params.append("?").append(includeEfforts.get.toString)

    WS.url(s"https://www.strava.com/api/v3/activities/$id$params")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
        println(response.body)
        val activityOpt = parse(response.body).extractOpt[Activity]
        if (activityOpt.isDefined) {
          Left(activityOpt.get)
        } else {
          Right(parse(response.body).extract[ActivitySummary])
        }
    }
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
      parse(response.body).extract[Activity]
    }
  }

  def deleteActivity(id: Long): Future[Boolean] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id")
      .delete()
      .map { response =>
      response.status.equals(204)
    }
  }

  def listCurrentAthleteActivities(before: Option[String], after: Option[String], page: Option[String], per_page: Option[String]): Future[List[PersonalActivitySummary]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/athlete/activities").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[String]]("before" -> before, "after" -> after, "page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get) }))
    debug(request.queryString.toString)
      request.get()
      .map { response =>
        parse(response.body).extract[List[PersonalActivitySummary]]
    }
  }

  def listFriendsActivities(page: Option[String], per_page: Option[String]): Future[List[ActivitySummary]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/activities/following").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[String]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get) }))
    request.get()
      .map { response =>
      parse(response.body).extract[List[ActivitySummary]]
    }
  }

  def listActivityZones(id: String): Future[ActivityZones] = {
    val request = WS.url(s"https://www.strava.com/api/v3/activities/$id/zones").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
        parse(response.body).extract[ActivityZones]
    }
  }

  def listActivityLaps(id: String): Future[List[LapEffort]] = {
    val request = WS.url(s"https://www.strava.com/api/v3/activities/$id/laps").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[List[LapEffort]]
    }
  }

  def retrieveClub(id: String): Future[Club] = {
    val request = WS.url(s"https://www.strava.com/api/v3/clubs/$id").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[Club]
    }
  }

  def listAthleteClubs(id: String): Future[List[ClubSummary]] = {
    val request = WS.url(s"https://www.strava.com/api/v3/athlete/clubs").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[List[ClubSummary]]
    }
  }

































  def getTimeStream(id: String): Future[TimeStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/time")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
        TimeStream((parse(response.body))(0).extract[Time], (parse(response.body))(1).extract[Distance])
    }
  }

  def getLatLngStream(id: String): Future[LatLngStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/latlng")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val latlngData = parse(response.body).children(0).extract[LatLng]
        val distanceData = parse(response.body).children(1).extract[Distance]
        LatLngStream(latlngData, distanceData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        LatLngStream(LatLng(), distanceData)
      }
    }
  }

  def getAltitudeStream(id: String): Future[AltitudeStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/altitude")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val altitudeData = parse(response.body).children(1).extract[Altitude]
        AltitudeStream(distanceData, altitudeData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        AltitudeStream(distanceData, Altitude())
      }
    }
  }

  def getVelocityStream(id: String): Future[VelocityStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/velocity_smooth")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val velocityData = parse(response.body).children(1).extract[Velocity]
        VelocityStream(distanceData, velocityData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        VelocityStream(distanceData, Velocity())
      }    }
  }

  def getHeartRateStream(id: String): Future[HeartrateStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/heartrate")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val heartData = parse(response.body).children(1).extract[Heartrate]
        HeartrateStream(distanceData, heartData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        HeartrateStream(distanceData, Heartrate())
      }
    }
  }

  def getCadenceStream(id: String): Future[CadenceStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/cadence")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val cadenceData = parse(response.body).children(1).extract[Cadence]
        CadenceStream(distanceData, cadenceData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        CadenceStream(distanceData, Cadence())
      }
    }
  }

  def getWattsStream(id: String): Future[WattsStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/watts")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val wattsData = parse(response.body).children(1).extract[Watts]
        WattsStream(distanceData, wattsData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        WattsStream(distanceData, Watts())
      }
    }
  }

  def getTempStream(id: String): Future[TempStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/temp")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("low")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val tempData = parse(response.body).children(1).extract[Temp]
        TempStream(distanceData, tempData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        TempStream(distanceData, Temp())
      }
    }
  }

  def getMovingStream(id: String): Future[MovingStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/moving")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val movingData = parse(response.body).children(1).extract[Moving]
        MovingStream(distanceData, movingData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        MovingStream(distanceData, Moving())
      }
    }
  }

  def getGradeStream(id: String): Future[GradeStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/grade_smooth")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      if (parse(response.body).children.size > 1) {
        val distanceData = parse(response.body).children(0).extract[Distance]
        val gradeData = parse(response.body).children(1).extract[Grade]
        GradeStream(distanceData, gradeData)
      } else {
        val distanceData = parse(response.body).children(0).extract[Distance]
        GradeStream(distanceData, Grade())
      }
    }
  }
}
