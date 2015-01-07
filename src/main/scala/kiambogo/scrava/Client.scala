/**
 * Created by christopher on 2014-09-15.
 */
package kiambogo.scrava

import net.liftweb.json._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import kiambogo.scrava.models._

import scala.util.{Failure, Success, Try}
import scalaj.http.Http

class ScravaClient(accessToken: String) {

  implicit val formats = DefaultFormats
  val authString = "Bearer " + accessToken

  //  ____ ___ _  _ _    ____ ___ ____
  //  |__|  |  |__| |    |___  |  |___
  //  |  |  |  |  | |___ |___  |  |___

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

  // Update an athlete's properties (requires Write permissions, untested)
  def updateAthlete(city: String, state: String, country: String, sex: String, weight: Float): DetailedAthlete = {
    val request = Http("https://www.strava.com/api/v3/athlete").header("Authorization", authString).method("put")
      .postForm(Seq(("city", city), ("state", state), ("country", country), ("sex", sex), ("weight", weight.toString)))
    Try {
      parse(request.asString.body).extract[DetailedAthlete]
    } match {
      case Success(athlete) => athlete
      case Failure(error) => throw new RuntimeException(s"Could not update Athlete: $error")
    }
  }

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

  // Create an activity (requires Write permissions, untested)
  def createActivity(name: String, `type`: String, startDateLocal: DateTime, elapsedTime: Int,
                     description: Option[String], distance: Option[Float]): Activity = {
    val request = Http("https://www.strava.com/api/v3/activities").header("Authorization", authString).method("post")
      .postForm(Seq(("name", name),("elapsed_time",elapsedTime.toString), ("distance", distance.get.toString), ("start_date_local", startDateLocal.toString(ISODateTimeFormat.dateTime())),
      ("type", `type`)))
    Try { parse(request.asString.body).extract[Activity] } match {
      case Success(activity) => activity
      case Failure(error) => throw new RuntimeException("Could not create activity: $error")
    }
  }

  // Retrieve detailed information about a specified activity
  def retrieveActivity(activity_id: Int, includeEfforts: Option[Boolean] = None): Activity = {
    var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id").header("Authorization", authString)
    Map[String, Option[Boolean]]("includeEfforts" -> includeEfforts)
      .map(params => params._2.map(opt => {
      request = request.param(params._1, params._2.get.toString)
    }))

    //if detailed activity
    if (parse(request.asString.body).\("resource_state").extract[Int] == 3) {
      if (parse(request.asString.body).\("athlete").\("resource_state").extract[Int] == 2) {
        Try {
          parse(request.asString.body).extract[DetailedActivity]
        } match {
          case Success(activity) => activity
          case Failure(error) => throw new RuntimeException(s"Could not parse activity: $error")
        }
      } else {
        Try {
          parse(request.asString.body).extract[PersonalDetailedActivity]
        } match {
          case Success(activitySummary) => activitySummary
          case Failure(error) => throw new RuntimeException(s"Could not parse personal activitySummary: $error")
        }
      }
    } else {
      if (parse(request.asString.body).\("athlete").\("resource_state").extract[Int] == 2) {
        Try {
          parse(request.asString.body).extract[ActivitySummary]
        } match {
          case Success(activity) => activity
          case Failure(error) => throw new RuntimeException(s"Could not parse activity: $error")
        }
      } else {
        Try {
          parse(request.asString.body).extract[PersonalActivitySummary]
        } match {
          case Success(activitySummary) => activitySummary
          case Failure(error) => throw new RuntimeException(s"Could not parse personal activitySummary: $error")
        }
      }
    }
  }

  // Update an activity (requires Write permissions, untested)
  def updateActivity(activity_id: Long, name: Option[String], `type`: Option[String], `private`: Option[Boolean], commute: Option[Boolean],
                     trainer: Option[Boolean], gearId: Option[String], description: Option[String]): Activity = {
    var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id").header("Authorization", authString).method("put")
    val tempMap = Map[String, Option[Any]]("name" -> name, "type" -> `type`, "private" -> `private`, "commute" -> commute,
      "trainer" -> trainer, "gear_id" -> gearId, "description" -> description)
    tempMap.map(params => params._2.map(opt => { request = request.params((params._1, params._2.get.toString)) }))

    Try { parse(request.asString.body).extract[DetailedActivity] } match {
      case Success(activity) => activity
      case Failure(error) => throw new RuntimeException(s"Could not update activity: $error")
    }
  }

  // Delete an activity (requires Write permissions, untested)
  def deleteActivity(id: Long): Boolean = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$id").method("delete")
    Try {
      request.asString.statusLine.equals(204)
    } match {
      case Success(bool) => bool
      case Failure(error) => throw new RuntimeException(s"Could not delete activity: $error")
    }

  }

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

  //  ____ _    _  _ ___  ____
  //  |    |    |  | |__] |__
  //  |___ |___ |__| |__] ___|

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

  def listClubMembers(club_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary] = {
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

  def listClubActivities(club_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[ActivitySummary] = {
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

  def listAthleteStarredSegments(page: Option[Int] = None, per_page: Option[Int] = None): List[SegmentSummary] = {
    var request = Http(s"https://www.strava.com/api/v3/segments/starred").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[List[SegmentSummary]]
  }

  def listStarredSegments(athlete_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[SegmentSummary] = {
    var request = Http(s"https://www.strava.com/api/v3/clubs/$athlete_id/activities").header("Authorization", authString)
    val tempMap = Map[String, Option[Int]]("page" -> page, "per_page" -> per_page)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    parse(request.asString.body).extract[List[SegmentSummary]]
  }

  def listEfforts(segment_id: Int, athlete_id: Option[Int] = None, start_date_local: Option[DateTime] = None,
                  end_date_local: Option[DateTime] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[SegmentEffort] = {
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
      "time,latlng,distance,altitude"
    } else { stream_types.get }
    val request = Http(s"https://www.strava.com/api/v3/segments/$segment_id/streams/"+types).header("Authorization", authString).param("resolution", "high")
    parse(request.asString.body).extract[List[JObject]].map(parseStream(_))
  }

  def parseStream(streamData: JValue): Streams = {
    val dataType = streamData.\("type").extract[String]
    dataType.replace("\"","") match {
      case "time" => streamData.extract[Time]
      case "latlng" => streamData.extract[LatLng]
      case "distance" => streamData.extract[Distance]
      case "altitude" => streamData.extract[Altitude]
      case "velocity_smooth" => streamData.extract[Velocity]
      case "heartrate" => streamData.extract[Heartrate]
      case "cadence" => streamData.extract[Cadence]
      case "watts" => streamData.extract[Watts]
      case "temp" => streamData.extract[Temp]
      case "moving" => streamData.extract[Moving]
      case "grade_smooth" => streamData.extract[Grade]
    }
  }

  //  _  _ ___  _    ____ ____ ___  ____
  //  |  | |__] |    |  | |__| |  \ [__
  //  |__| |    |___ |__| |  | |__/ ___]

  // Upload an activity from a file (requires Write permissions, untested)
  def uploadActivity(activity_type: Option[String], name: Option[String], description: Option[String], `private`: Option[Int],
                     trainer: Option[Int], data_type: String, external_id: Option[String], file: Array[Byte]): Boolean = {
    var request = Http(s"https://www.strava.com/api/v3/uploads").header("Authorization", authString).method("post")
    val tempMap = Map[String, Option[Any]]("activity_type" -> activity_type, "name" -> name, "description" -> description,
      "private" -> `private`, "trainer" -> trainer, "external_id" -> external_id)
    request.param("data_type", data_type)
    request.postData(file)
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    request.asString.statusLine equals("201")
  }

  // Check the upload status of the activity (untested)
  def checkUploadStatus(upload_id: Int, external_id: String, activity_id: Option[Int] = None, status: String, error: Option[String] = None): UploadStatus = {
    val request = Http(s"https://www.strava.com/api/v3/uploads/$upload_id").header("Authorization", authString)
      .params(Seq(("external_id", external_id), ("activity_id", activity_id.get.toString), ("status", status), ("error", error.get)))
    Try { parse(request.asString.body).extract[UploadStatus] } match {
      case Success(status) => status
      case Failure(error) => throw new RuntimeException(s"Could not parse upload status: $error")
    }
  }

  //  ____ _  _ ___ ____    ___  ____ ____ _ _  _ ____ ___ _ ____ _  _
  //  |__| |  |  |  |  | __ |__] |__| | __ | |\ | |__|  |  | |  | |\ |
  //  |  | |__|  |  |__|    |    |  | |__] | | \| |  |  |  | |__| | \|

  def getAll[A, B](f: (Option[Int], Option[Int], Option[Int], Option[Int]) => List[B]): List[B] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      curry4(f)(None)(None)(Some(200))(Some(counter))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def getAll[A, B](f: (Option[Int], Option[Int], Option[Int]) => List[B], id: Option[Int]): List[B] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      curry2optId(f)(Some(200))(id)(Some(counter))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def getAll[A, B](f: (Int, Option[Int], Option[Int]) => List[B], id: Int): List[B] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      curry2id(f)(Some(200))(id)(Some(counter))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def getAll[A, B](f: (Option[Int], Option[Int]) => List[B]): List[B] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      curry2(f)(Some(200))(Some(counter))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def curry4[A, B, C, E, D](f: (A, B, C, D) => E): A => B => D => C => E = {
    { (a: A) => { (b: B) => { (d: D) => { (c: C) => f(a, b, c, d) } } } }
  }

  def curry2id[A, B, C, D](f: (Int, B, C) => D): C => Int => B => D = {
    { (c: C) => { (a: Int) => { (b: B) => f(a, b, c) } } }
  }

  def curry2optId[A, B, C, D](f: (Option[Int], B, C) => D): C => Option[Int] => B => D = {
    { (c: C) => { (a: Option[Int]) => { (b: B) => f(a, b, c) } } }
  }

  def curry2[A, B, C](f: (A, B) => C): B => A => C = {
    { (b: B) => { (a: A) => f(a, b) } }
  }
}

