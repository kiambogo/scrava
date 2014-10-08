package controllers

/**
 * Created by christopher on 2014-09-15.
 */

import models._
import net.liftweb.json._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ScravaClient(accessToken: String) {

  implicit val formats = DefaultFormats
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
        parse(response.body).extract[Athlete] }
  }

  def findAthlete(id: String): Future[Athlete] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
        parse(response.body).extract[Athlete]
    }
  }

  def listAthleteKOMs(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[SegmentEffort] = {
    val params = StringBuilder.newBuilder
    if (page.isDefined || resultsPerPage.isDefined) params.append("?")
    if (page.isDefined) params.append("page=").append(page.get.toString)
    if (page.isDefined && resultsPerPage.isDefined) params.append("&per_page=").append(resultsPerPage.get.toString)
    else if (resultsPerPage.isDefined) params.append("per_page").append(resultsPerPage.get.toString)

    WS.url(s"https://www.strava.com/api/v3/athletes/$id/koms$params")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      println(response.json)
      (parse(response.body) \ "id").extract[List[Long]]
        parse(response.body).extract[SegmentEffort]
      //println(koms.head)
      //koms
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

  def listAthleteFriends(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
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
        parse(response.body).extract[Activity]
    }
  }

  def retrieveActivity(id: Long, includeEfforts: Option[Boolean]): Future[Activity] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id")
      .withQueryString("include_all_efforts" -> includeEfforts.iterator.next().toString)
      .get()
      .map { response =>
        parse(response.body).extract[Activity]
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
      LatLngStream((parse(response.body))(0).extract[LatLng], (parse(response.body))(1).extract[Distance])
    }
  }

  def getAltitudeStream(id: String): Future[AltitudeStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/altitude")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      AltitudeStream((parse(response.body))(0).extract[Distance], (parse(response.body))(1).extract[Altitude])
    }
  }

  def getVelocityStream(id: String): Future[VelocityStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/velocity_smooth")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      VelocityStream((parse(response.body))(0).extract[Distance], (parse(response.body))(1).extract[Velocity])
    }
  }

  def getHeartRateStream(id: String): Future[HeartrateStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/velocity_smooth")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      HeartrateStream((parse(response.body))(0).extract[Heartrate], (parse(response.body))(1).extract[Distance])
    }
  }

  def getCadenceStream(id: String): Future[CadenceStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/velocity_smooth")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      CadenceStream((parse(response.body))(0).extract[Cadence], (parse(response.body))(1).extract[Distance])
    }
  }

  def getWattsStream(id: String): Future[WattsStream] = {
    WS.url(s"https://www.strava.com/api/v3/activities/$id/streams/velocity_smooth")
      .withHeaders("Authorization" -> authString)
      .withBody(Map("resolution" -> Seq("high")))
      .get()
      .map { response =>
      WattsStream((parse(response.body))(0).extract[Watts], (parse(response.body))(1).extract[Distance])
    }
  }
}
