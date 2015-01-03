/**
 * Created by christopher on 2014-09-15.
 */

import models.LapEffort
import net.liftweb.json._
import scrava.models._
import org.joda.time.DateTime
import scala.util.{Failure, Success, Try}
import scalaj.http.Http

class ScravaClient(accessToken: String) {

  implicit val formats = DefaultFormats
  val authString = "Bearer " + accessToken

  //  ____ ___ _  _ _    ____ ___ ____
  //  |__|  |  |__| |    |___  |  |___
  //  |  |  |  |  | |___ |___  |  |___

  //Friends + Following

  // List an athlete's friends. Returns current athlete's friends if athlete_id left null
  def listAthleteFriends(athlete_id: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary] = {
    var request = if (!athlete_id.isDefined) {
      //Return current authenticated athlete's friends
      Http(s"https://www.strava.com/api/v3/athlete/friends").header("Authorization", authString)
    } else {
      //Return specified athlete friends
      Http(s"https://www.strava.com/api/v3/athletes/"+athlete_id.get+"/friends").header("Authorization", authString)
    }
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try { parse(request.asString.body).extract[List[AthleteSummary]] } match {
      case Success(athletes) => athletes
      case Failure(error) => throw new RuntimeException(s"Could not parse list of athlete friends: $error")
    }
  }


  // List an athlete's followers. Returns current athlete's followers if athlete_id left null
  def listAthleteFollowers(athlete_id: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary] = {
    var request = if (!athlete_id.isDefined) {
      //Return current authenticated athlete's followers
      Http(s"https://www.strava.com/api/v3/athlete/followers").header("Authorization", authString)
    } else {
      //Return specified athlete followers
      Http(s"https://www.strava.com/api/v3/athletes/"+athlete_id.get+"/followers").header("Authorization", authString)
    }
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try { parse(request.asString.body).extract[List[AthleteSummary]] } match {
      case Success(followers) => followers
      case Failure(error) => throw new RuntimeException(s"Could not parse list of athlete followers: $error")
    }
  }

  // List mutual followings for current athlete and specified athlete by athlete_id
  def listMutualFollowing(athlete_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary] = {
    var request = Http(s"https://www.strava.com/api/v3/athletes/$athlete_id/both-following").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try { parse(request.asString.body).extract[List[AthleteSummary]] } match {
      case Success(followings) => followings
      case Failure(error) => throw new RuntimeException(s"Could not parse list of mutual followers: $error")
    }
  }

  // Retrieve a specified Athlete. Returns current athlete if athlete_id left null
  def retrieveAthlete(athlete_id: Option[Int] = None): Either[DetailedAthlete, AthleteSummary] = {
    if (!athlete_id.isDefined) {
      //Return current authenticated athlete
      val request = Http(s"https://www.strava.com/api/v3/athlete").header("Authorization", authString)
      Try { Left(parse(request.asString.body).extract[DetailedAthlete]) } match {
        case Success(athlete) => athlete
        case Failure(error) => throw new RuntimeException(s"Could not parse Athlete: $error")
      }
    } else {
      //Return specified athlete summary
      val request = Http(s"https://www.strava.com/api/v3/athletes/" + athlete_id.get).header("Authorization", authString)
      Try { Right(parse(request.asString.body).extract[AthleteSummary]) } match {
        case Success(athleteSummary) => athleteSummary
        case Failure(error) => throw new RuntimeException(s"Could not parse athleteSummary: $error")
      }
    }
  }

  //  //TODO
  //  // Update an ahtlete's properties (requires Write) TODO
  //  def updateAthlete(city: String, state: String, country: String, sex: String, weight: Float): Future[DetailedAthlete] = {
  //    WS.url("https://www.strava.com/api/v3/athlete")
  //      .withHeaders("Authorization" -> authString)
  //      .put(Map(
  //      "city" -> Seq(city),
  //      "state" -> Seq(state),
  //      "country" -> Seq(country),
  //      "sex" -> Seq(sex),
  //      "weight" -> Seq(weight.toString)))
  //      .map { response =>
  //      parse(response.body).extract[DetailedAthlete] }
  //  }

  // List an athlete's KOMs
  def listAthleteKOMs(athlete_id: Int, page: Option[Int] = None, resultsPerPage: Option[Int] = None): List[SegmentEffort] = {
    //Return specified athlete followers
    var request = Http(s"https://www.strava.com/api/v3/athletes/" + athlete_id + "/koms").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "resultsPerPage" -> resultsPerPage)
    tempMap.map(params => params._2 map (opt => { request = request.param(params._1, params._2.get.toString) }))
    Try { parse(request.asString.body).extract[List[SegmentEffort]] } match {
      case Success(koms) => koms
      case Failure(error) => throw new RuntimeException(s"Could not parse athlete KOMs: $error")
    }
  }


  //  ____ ____ ___ _ _  _ _ ___ _   _
  //  |__| |     |  | |  | |  |   \_/
  //  |  | |___  |  |  \/  |  |    |

  // Activity-Related Functions

  // List all comments from an activity
  def listActivityComments(activity_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[ActivityComments] = {
    var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/comments").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try {
      parse(request.asString.body).extract[List[ActivityComments]]
    } match {
      case Success(comments) => comments
      case Failure(error) => throw new RuntimeException(s"Could not parse activity comments: $error")
    }
  }


  // List the athletes who have 'kudosed' the specified activity
  def listActivityKudoers(activity_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary] = {
    var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/kudos").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try {
      parse(request.asString.body).extract[List[AthleteSummary]]
    } match {
      case Success(athleteList) => athleteList
      case Failure(error) => throw new RuntimeException(s"Could not parse list of kudosers for activity: $error")
    }
  }

  // List photos associated with a specified activity
  def listActivityPhotos(id: Int): List[Photo] = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$id/photos")
    if (request.asString.isSuccess) {
      Try {
        parse(request.asString.body).extract[List[Photo]]
      } match {
        case Success(photos) => photos
        case Failure(error) => throw new RuntimeException(s"Could not parse list of photos: $error")
      }
    } else List()
  }

  //  //TODO
  //  def createActivity(name: String, `type`: String, startDateLocal: DateTime, elapsedTime: Int,
  //                     description: Option[String], distance: Option[Float]): Future[Activity] = {
  //    WS.url("https://www.strava.com/api/v3/activities")
  //      .withHeaders("Authorization" -> authString)
  //      .post(Map(
  //      "name" -> Seq(name),
  //      "elapsed_time" -> Seq(elapsedTime.toString),
  //      "distance" -> Seq(distance.get.toString),
  //      "start_date_local" -> Seq(startDateLocal.toString(ISODateTimeFormat.dateTime())),
  //      "type" -> Seq(`type`)
  //    ))
  //      .map { response =>
  //      println(response.json)
  //      Try { parse(response.body).extract[Activity] } match {
  //        case Success(activity) => activity
  //        case Failure(error) => throw new RuntimeException("Could not create activity: $error")
  //      }
  //    }
  //  }
  //
  // Retrieve detailed information about a specified activity
  def retrieveActivity(activity_id: Int, includeEfforts: Option[Boolean] = None): Either[Activity, ActivitySummary] = {
    var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id").header("Authorization", authString)
    Map[String, Option[Boolean]]("includeEfforts" -> includeEfforts)
      .map(params => params._2.map(opt => {
      request = request.param(params._1, params._2.get.toString)
    }))
    if (parse(request.asString.body).\("athlete").\("resource_state").extract[Int] == 3) {
      Try {
        Left(parse(request.asString.body).extract[Activity])
      } match {
        case Success(activity) => activity
        case Failure(error) => throw new RuntimeException(s"Could not parse activity: $error")
      }
    } else {
      Try {
        Right(parse(request.asString.body).extract[ActivitySummary])
      } match {
        case Success(activitySummary) => activitySummary
        case Failure(error) => throw new RuntimeException(s"Could not parse activitySummary: $error")
      }
    }
  }
  //
  //  //TODO
  //  def updateActivity(activity_id: Long, name: Option[String], `type`: Option[String], `private`: Option[Boolean], commute: Option[Boolean],
  //                     trainer: Option[Boolean], gearId: Option[String], description: Option[String]): Future[Activity] = {
  //    var request = WS.url(s"https://www.strava.com/api/v3/activities/$activity_id").withHeaders("Authorization" -> authString)
  //    val tempMap = Map[String, Option[Any]]("name" -> name, "type" -> `type`, "private" -> `private`, "commute" -> commute,
  //      "trainer" -> trainer, "gear_id" -> gearId, "description" -> description)
  //    tempMap.map(params => params._2.map(opt => { request = request.withQueryString(params._1 -> params._2.get.toString) }))
  //    request.put("")
  //      .map { response =>
  //      Try {
  //        parse(response.body).extract[Activity]
  //      } match {
  //        case Success(activity) => activity
  //        case Failure(error) => throw new RuntimeException(s"Could not update activity: $error")
  //      }
  //    }
  //  }
  //
  //  //TODO
  //  def deleteActivity(id: Long): Future[Boolean] = {
  //    WS.url(s"https://www.strava.com/api/v3/activities/$id")
  //      .delete()
  //      .map { response =>
  //      Try {
  //        response.status.equals(204)
  //      } match {
  //        case Success(bool) => bool
  //        case Failure(error) => throw new RuntimeException(s"Could not delete activity: $error")
  //      }
  //    }
  //  }
  //
  // Lists activities associated with the currently authenticated athlete
  def listAthleteActivities(before: Option[Int] = None, after: Option[Int] = None,
                            page: Option[Int] = None, per_page: Option[Int] = None): List[PersonalActivitySummary] = {

    var request = Http(s"https://www.strava.com/api/v3/athlete/activities").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("before" -> before, "after" -> after, "page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try {
      parse(request.asString.body).extract[List[PersonalActivitySummary]]
    } match {
      case Success(activities) => activities
      case Failure(error) => throw new RuntimeException(s"Could not parse list of activities: $error")
    }
  }

  // List activities associated with current athlete and his/her friends (activity feed from Strava)
  def listFriendsActivities(page: Option[Int] = None, per_page: Option[Int] = None): List[ActivitySummary] = {
    var request = Http(s"https://www.strava.com/api/v3/activities/following").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try {
      parse(request.asString.body).extract[List[ActivitySummary]]
    } match {
      case Success(activities) => activities
      case Failure(error) => throw new RuntimeException(s"Could not parse friends' activities: $error")
    }
  }


  def listActivityZones(id: Int): List[ActivityZones] = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$id/zones").header("Authorization", authString)
    Try {
      parse(request.asString.body).extract[List[ActivityZones]]
    } match {
      case Success(zones) => zones
      case Failure(error) => throw new RuntimeException(s"Could not parse activty zones: $error")
    }
  }

  def listActivityLaps(activity_id: Int): List[LapEffort] = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/laps").header("Authorization", authString)
    Try {
      parse(request.asString.body).extract[List[LapEffort]]
    } match {
      case Success(laps) => laps
      case Failure(error) => throw new RuntimeException(s"Could not parse activty laps: $error")
    }
  }

  //  ____ _    _  _ ___
  //  |    |    |  | |__]
  //  |___ |___ |__| |__]

  // Retrieve a detailed description of the specified club id
  def retrieveClub(club_id: Int): Club = {
    val request = Http(s"https://www.strava.com/api/v3/clubs/$club_id").header("Authorization", authString)
    Try {
      parse(request.asString.body).extract[Club]
    } match {
      case Success(club) => club
      case Failure(error) => throw new RuntimeException(s"Could not parse club: $error")
    }
  }


  // Return a list of clubs that the authenticated athlete is part of
  def listAthleteClubs: List[ClubSummary] = {
    val request = Http(s"https://www.strava.com/api/v3/athlete/clubs").header("Authorization", authString)
    Try {
      parse(request.asString.body).extract[List[ClubSummary]]
    } match {
      case Success(clubs) => clubs
      case Failure(error) => throw new RuntimeException(s"Could not parse clubs: $error")
    }
  }


  def listClubMembers(club_id: Int, page: Option[Int], per_page: Option[Int]): List[AthleteSummary] = {
    var request = Http(s"https://www.strava.com/api/v3/clubs/$club_id/members").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    Try {
      parse(request.asString.body).extract[List[AthleteSummary]]
    } match {
      case Success(members) => members
      case Failure(error) => throw new RuntimeException(s"Could not parse club members: $error")
    }
  }


  def listClubActivities(club_id: Int, page: Option[Int], per_page: Option[Int]): List[ActivitySummary] = {
    var request = Http(s"https://www.strava.com/api/v3/clubs/$club_id/activities").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[List[ActivitySummary]]
  }


  //  ____ ____ ____ ____
  //  | __ |___ |__| |__/
  //  |__] |___ |  | |  \

  def retrieveAthleteGear(gear_id: String): Gear = {
    val request = Http(s"https://www.strava.com/api/v3/gear/$gear_id").header("Authorization", authString)
    parse(request.asString.body).extract[Gear]
  }

  //  ____ ____ ____ _  _ ____ _  _ ___ ____
  //  [__  |___ | __ |\/| |___ |\ |  |  [__
  //  ___] |___ |__] |  | |___ | \|  |  ___]


  def retrieveSegment(segment_id: Int): Segment = {
    val request = Http(s"https://www.strava.com/api/v3/segments/$segment_id").header("Authorization", authString)
    parse(request.asString.body).extract[Segment]
  }

  def listAthleteStarredSegments(page: Option[Int], per_page: Option[Int]): List[SegmentSummary] = {
    var request = Http(s"https://www.strava.com/api/v3/segments/starred").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[List[SegmentSummary]]
  }

  def listStarredSegments(athlete_id: Int, page: Option[Int], per_page: Option[Int]): List[SegmentSummary] = {
    var request = Http(s"https://www.strava.com/api/v3/clubs/$athlete_id/activities").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[List[SegmentSummary]]
  }

  def listEfforts(segment_id: Int, athlete_id: Option[Int], start_date_local: Option[DateTime],
                  end_date_local: Option[DateTime], page: Option[Int], per_page: Option[Int]): List[SegmentEffort] = {
    var request = Http(s"https://www.strava.com/api/v3/segments/$segment_id/all_efforts").header("Authorization", authString)
    val tempMap = Map[String, Option[Any]]("start_date_local" -> start_date_local, "end_date_local" -> end_date_local, "page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[List[SegmentEffort]]
  }

  def listSegmentLeaderboards(segment_id: String, gender: Option[String], age_group: Option[String],
                              weight_class: Option[String], following: Option[Boolean], club_id: Option[Int],
                              date_range: Option[String], page: Option[Int], per_page: Option[Int]): SegmentLeaderBoards = {
    var request = Http(s"https://www.strava.com/api/v3/segments/$segment_id/leaderboard").header("Authorization", authString)
    val tempMap = Map[String, Option[Any]]("gender" -> gender, "age_group" -> age_group, "weight_class" -> weight_class,
      "following" -> following, "club_id" -> club_id, "date_range" -> date_range, "page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[SegmentLeaderBoards]
  }

  def segmentExplorer(bounds: List[Float], activity_type: Option[String], min_cat: Option[Int], max_cat: Option[Int]): SegmentCondensed = {
    var request = Http(s"https://www.strava.com/api/v3/segments/explore").header("Authorization", authString)
    val tempMap = Map[String, Option[Any]]("activity_type" -> activity_type, "min_cat" -> min_cat, "max_cat" -> max_cat)
    request = request.param("bounds", bounds.mkString(","))
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[SegmentCondensed]
  }

  def retrieveSegmentEffort(effort_id: BigInt): SegmentEffort = {
    val request = Http(s"https://www.strava.com/api/v3/segment_efforts/$effort_id").header("Authorization", authString)
    parse(request.asString.body).extract[SegmentEffort]
  }

  //  ____ ___ ____ ____ ____ _  _ ____
  //  [__   |  |__/ |___ |__| |\/| [__
  //  ___]  |  |  \ |___ |  | |  | ___]


  def retrieveActivityStream(activity_id: String, stream_types: Option[String] = None): List[Streams] = {
    val types = if (!stream_types.isDefined) {
      "time,latlng,distance,altitude,velocity_smooth,heartrate,cadence,watts,temp,moving,grade_smooth"
    } else { stream_types.get }
    val request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/streams/"+types).header("Authorization", authString).param("resolution", "high")
    parse(request.asString.body).extract[List[JObject]].map(parseStream(_))
  }

  def retrieveEffortStream(effort_id: String, stream_types: Option[String] = None): List[Streams] = {
    val types = if (!stream_types.isDefined) {
      "time,latlng,distance,altitude,velocity_smooth,heartrate,cadence,watts,temp,moving,grade_smooth"
    } else { stream_types.get }
    val request = Http(s"https://www.strava.com/api/v3/segment_efforts/$effort_id/streams/"+types).header("Authorization", authString).param("resolution", "high")
    parse(request.asString.body).extract[List[JObject]].map(parseStream(_))
  }


  def retrieveSegmentStream(segment_id: String, stream_types: Option[String] = None): List[Streams] = {
    val types = if (!stream_types.isDefined) {
      "latlng,distance,altitude"
    } else { stream_types.get }
    val request = Http(s"https://www.strava.com/api/v3/segments/$segment_id/streams/"+types).header("Authorization", authString).param("resolution", "high")
    parse(request.asString.body).extract[List[JObject]].map(parseStream(_))
  }

  def parseStream(streamData: JObject): Streams = {
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
  //
  //  /*def uploadActivity(activity_type: Option[String], name: Option[String], description: Option[String], `private`: Option[Int],
  //                     trainer: Option[Int], data_type: String, external_id: Option[String], file: FilePart): Boolean] = {
  //    var request = Http(s"https://www.strava.com/api/v3/uploads").header("Authorization", authString)
  //    val tempMap = Map[String, Option[Any]]("activity_type" -> activity_type, "name" -> name, "description" -> description,
  //      "private" -> `private`, "trainer" -> trainer, "external_id" -> external_id)
  //    request.param("data_type" -> data_type)
  //    tempMap.map(params => params._2.map(opt => { request = request.param(params._1 -> params._2.get.toString) }))
  //    request.post()
  //      .map { request.asString =>
  //      request.asString.statusText equals(201)
  //    }
  //  }*/
  //
  //  def checkUploadStatus(upload_id: Int, external_id: String, activity_id: Option[Int] = None, status: String, error: Option[String] = None): UploadStatus] = {
  //    Http(s"https://www.strava.com/api/v3/uploads/$upload_id").header("Authorization", authString)
  //      .param("external_id" -> external_id, "activity_id" -> activity_id.get.toString, "status" -> status, "error" -> error.get)
  //      .get()
  //      .map { request.asString =>
  //      parse(request.asString.body).extract[UploadStatus]
  //    }
  //  }

}

