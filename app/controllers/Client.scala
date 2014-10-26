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
import play.api.libs.json.JsValue
import play.api.libs.ws.WS
import play.mvc.Http.MultipartFormData

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

  def listActivityLaps(activity_id: String): Future[List[LapEffort]] = {
    val request = WS.url(s"https://www.strava.com/api/v3/activities/$activity_id/laps").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[List[LapEffort]]
    }
  }

  def retrieveClub(club_id: String): Future[Club] = {
    val request = WS.url(s"https://www.strava.com/api/v3/clubs/$club_id").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[Club]
    }
  }

  def listAthleteClubs: Future[List[ClubSummary]] = {
    val request = WS.url(s"https://www.strava.com/api/v3/athlete/clubs").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[List[ClubSummary]]
    }
  }

  def listClubMembers(club_id: String, page: Option[Int], per_page: Option[Int]): Future[List[AthleteSummary]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/clubs/$club_id/members").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.get()
      .map { response =>
      parse(response.body).extract[List[AthleteSummary]]
    }
  }

  def listClubActivities(club_id: String, page: Option[Int], per_page: Option[Int]): Future[List[ActivitySummary]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/clubs/$club_id/activities").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.get()
      .map { response =>
      parse(response.body).extract[List[ActivitySummary]]
    }
  }

  def retrieveAthleteGear(gear_id: String): Future[List[Gear]] = {
    val request = WS.url(s"https://www.strava.com/api/v3/gear/$gear_id").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[List[Gear]]
    }
  }

  def retrieveSegment(segment_id: String): Future[Segment] = {
    val request = WS.url(s"https://www.strava.com/api/v3/segments/$segment_id").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[Segment]
    }
  }

  def listAthleteStarredSegments(page: Option[Int], per_page: Option[Int]): Future[List[SegmentSummary]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/segments/starred").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.get()
      .map { response =>
      parse(response.body).extract[List[SegmentSummary]]
    }
  }

  def listStarredSegments(athlete_id: String, page: Option[Int], per_page: Option[Int]): Future[List[SegmentSummary]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/clubs/$athlete_id/activities").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.get()
      .map { response =>
      parse(response.body).extract[List[SegmentSummary]]
    }
  }

  def listEfforts(segment_id: String, athlete_id: Option[String], start_date_local: Option[DateTime],
                  end_date_local: Option[DateTime], page: Option[Int], per_page: Option[Int]): Future[List[SegmentEffort]] = {
    var request = WS.url(s"https://www.strava.com/api/v3/segments/$segment_id/all_efforts").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Any]]("start_date_local" -> start_date_local, "end_date_local" -> end_date_local, "page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.get()
      .map { response =>
      parse(response.body).extract[List[SegmentEffort]]
    }
  }

  def listSegmentLeaderboards(segment_id: String, gender: Option[String], age_group: Option[String],
                              weight_class: Option[String], following: Option[Boolean], club_id: Option[Int],
                              date_range: Option[String], page: Option[Int], per_page: Option[Int]): Future[SegmentLeaderBoards] = {
    var request = WS.url(s"https://www.strava.com/api/v3/segments/$segment_id/leaderboard").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Any]]("gender" -> gender, "age_group" -> age_group, "weight_class" -> weight_class,
      "following" -> following, "club_id" -> club_id, "date_range" -> date_range, "page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.get()
      .map { response =>
      parse(response.body).extract[SegmentLeaderBoards]
    }
  }

  def segmentExplorer(bounds: List[Float], activity_type: Option[String], min_cat: Option[Int], max_cat: Option[Int]): Future[SegmentCondensed] = {
    var request = WS.url(s"https://www.strava.com/api/v3/segments/explore").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Any]]("activity_type" -> activity_type, "min_cat" -> min_cat, "max_cat" -> max_cat)
    request = request.withQueryString("bounds" -> bounds.mkString(","))
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.get()
      .map { response =>
      parse(response.body).extract[SegmentCondensed]
    }
  }

  def retrieveSegmentEffort(effort_id: BigInt): Future[SegmentEffort] = {
    val request = WS.url(s"https://www.strava.com/api/v3/segment_efforts/$effort_id").withHeaders("Authorization" -> authString)
    request.get()
      .map { response =>
      parse(response.body).extract[SegmentEffort]
    }
  }

  def retrieveActivityStream(activity_id: String, stream_types: Option[String] = None): Future[List[Streams]] = {
    val types = if (!stream_types.isDefined) {
      "time,latlng,distance,altitude,velocity_smooth,heartrate,cadence,watts,temp,moving,grade_smooth"
    } else { stream_types.get }
    WS.url(s"https://www.strava.com/api/v3/activities/$activity_id/streams/"+types).withHeaders("Authorization" -> authString)
      .withQueryString("resolution" -> "high")
      .get()
      .map { response =>
      response.json.as[List[JsValue]].map(parseStream(_))
    }
  }

  def retrieveEffortStream(effort_id: String, stream_types: Option[String] = None): Future[List[Streams]] = {
    val types = if (!stream_types.isDefined) {
      "time,latlng,distance,altitude,velocity_smooth,heartrate,cadence,watts,temp,moving,grade_smooth"
    } else { stream_types.get }
    WS.url(s"https://www.strava.com/api/v3/segment_efforts/$effort_id/streams/"+types).withHeaders("Authorization" -> authString)
      .withQueryString("resolution" -> "high")
      .get()
      .map { response =>
      response.json.as[List[JsValue]].map(parseStream(_))
    }
  }

  def retrieveSegmentStream(segment_id: String, stream_types: Option[String] = None): Future[List[Streams]] = {
    val types = if (!stream_types.isDefined) {
      "latlng,distance,altitude"
    } else { stream_types.get }
    WS.url(s"https://www.strava.com/api/v3/segments/$segment_id/streams/"+types).withHeaders("Authorization" -> authString)
      .withQueryString("resolution" -> "high")
      .get()
      .map { response =>
      response.json.as[List[JsValue]].map(parseStream(_))
    }
  }

  def parseStream(streamData: JsValue): Streams = {
    val dataType = streamData.\("type").toString
    val dataStr = streamData.toString()
    dataType.replace("\"","") match {
      case "time" => parse(dataStr).extract[Time]
      case "latlng" => parse(streamData.toString).extract[LatLng]
      case "distance" => parse(streamData.toString).extract[Distance]
      case "altitude" => parse(streamData.toString).extract[Altitude]
      case "velocity_smooth" => parse(streamData.toString).extract[Velocity]
      case "heartrate" => parse(streamData.toString).extract[Heartrate]
      case "cadence" => parse(streamData.toString).extract[Cadence]
      case "watts" => parse(streamData.toString).extract[Watts]
      case "temp" => parse(streamData.toString).extract[Temp]
      case "moving" => parse(streamData.toString).extract[Moving]
      case "grade_smooth" => parse(streamData.toString).extract[Grade]
    }
  }

  def uploadActivity(activity_type: Option[String], name: Option[String], description: Option[String], `private`: Option[Int],
                      trainer: Option[Int], data_type: String, external_id: Option[String], file: MultipartFormData): Future[Boolean] = {
    var request = WS.url(s"https://www.strava.com/api/v3/uploads").withHeaders("Authorization" -> authString)
    val tempMap = Map[String, Option[Any]]("activity_type" -> activity_type, "name" -> name, "description" -> description,
      "private" -> `private`, "trainer" -> trainer, "external_id" -> external_id)
    request.withQueryString("data_type" -> data_type)
    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
    request.post(file)
    .map { response =>
      response.statusText equals(201)
    }
  }

  def checkUploadStatus(upload_id: Int, external_id: String, activity_id: Option[Int] = None, status: String, error: Option[String] = None): Future[UploadStatus] = {
    WS.url(s"https://www.strava.com/api/v3/uploads/$upload_id").withHeaders("Authorization" -> authString)
      .withQueryString("external_id" -> external_id, "activity_id" -> activity_id.get.toString, "status" -> status, "error" -> error.get)
      .get()
      .map { response =>
        parse(response.body).extract[UploadStatus]
    }
  }

}

