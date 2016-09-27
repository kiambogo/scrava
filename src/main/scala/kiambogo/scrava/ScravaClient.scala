package kiambogo.scrava

import net.liftweb.json._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import kiambogo.scrava.models._
import scala.util.{Failure, Success, Try}
import scalaj.http.{Http, HttpRequest}
import scalaj.http.HttpRequest

class ScravaClient(accessToken: String) extends Client {

  implicit val formats = DefaultFormats
  val authString = "Bearer " + accessToken

  // List an athlete's friends. Returns current athlete's friends if athlete_id left null
  override def listAthleteFriends(athlete_id: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[AthleteSummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = if (!athlete_id.isDefined) {
        //Return current authenticated athlete's friends
        Http(s"https://www.strava.com/api/v3/athlete/friends")
          .header("Authorization", authString)
        } else {
          //Return specified athlete friends
          Http(s"https://www.strava.com/api/v3/athletes/"+athlete_id.get+"/friends")
            .header("Authorization", authString)
        }
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[AthleteSummary]] } match {
          case Success(athletes) => athletes
          case Failure(error) => throw new RuntimeException(s"Could not parse list of athlete friends: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  // List an athlete's followers. Returns current athlete's followers if athlete_id left null
  override def listAthleteFollowers(athlete_id: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[AthleteSummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = if (!athlete_id.isDefined) {
        //Return current authenticated athlete's followers
        Http(s"https://www.strava.com/api/v3/athlete/followers").header("Authorization", authString)
        } else {
          //Return specified athlete followers
          Http(s"https://www.strava.com/api/v3/athletes/"+athlete_id.get+"/followers").header("Authorization", authString)
        }
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[AthleteSummary]] } match {
          case Success(followers) => followers
          case Failure(error) => throw new RuntimeException(s"Could not parse list of athlete followers: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  // List mutual followings for current athlete and specified athlete by athlete_id
  override def listMutualFollowing(athlete_id: Int, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[AthleteSummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/athletes/$athlete_id/both-following").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[AthleteSummary]] } match {
          case Success(followings) => followings
          case Failure(error) => throw new RuntimeException(s"Could not parse list of mutual followers: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  // Retrieve a specified Athlete. Returns current athlete if athlete_id left null
  override def retrieveAthlete(athlete_id: Option[Int] = None): Either[DetailedAthlete, AthleteSummary] = {
    if (!athlete_id.isDefined) {
      //Return current authenticated athlete
      val request = Http(s"https://www.strava.com/api/v3/athlete").header("Authorization", authString)
      Try { Left(parseWithRateLimits(request).extract[DetailedAthlete]) } match {
        case Success(athlete) => athlete
        case Failure(error) => throw new RuntimeException(s"Could not parse Athlete: $error")
      }
    } else {
      //Return specified athlete summary
      val request = Http(s"https://www.strava.com/api/v3/athletes/" + athlete_id.get).header("Authorization", authString)
      Try { Right(parseWithRateLimits(request).extract[AthleteSummary]) } match {
        case Success(athleteSummary) => athleteSummary
        case Failure(error) => throw new RuntimeException(s"Could not parse athleteSummary: $error")
      }
    }
  }

  // Update an athlete's properties (requires Write permissions, untested)
  override def updateAthlete(city: String, state: String, country: String, sex: String, weight: Float): DetailedAthlete = {
    val request = Http("https://www.strava.com/api/v3/athlete").header("Authorization", authString).method("put")
      .postForm(Seq(("city", city), ("state", state), ("country", country), ("sex", sex), ("weight", weight.toString)))
    Try {
      parseWithRateLimits(request).extract[DetailedAthlete]
    } match {
      case Success(athlete) => athlete
      case Failure(error) => throw new RuntimeException(s"Could not update Athlete: $error")
    }
  }

  // List an athlete's KOMs
  override def listAthleteKOMs(athlete_id: Int, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[SegmentEffort] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      //Return specified athlete followers
      var request = Http(s"https://www.strava.com/api/v3/athletes/" + athlete_id + "/koms").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[SegmentEffort]] } match {
          case Success(koms) => koms
          case Failure(error) => throw new RuntimeException(s"Could not parse athlete KOMs: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  // List an athlete's stats
  override def listAthleteStats(athlete_id: Int): Stats = {
    //Return specified athlete followers
    var request = Http(s"https://www.strava.com/api/v3/athletes/" + athlete_id + "/stats").header("Authorization", authString)
    Try { parseWithRateLimits(request).extract[Stats] } match {
      case Success(stats) => stats
      case Failure(error) => throw new RuntimeException(s"Could not parse athlete stats: $error")
    }
  }

  // List all comments from an activity
  override def listActivityComments(activity_id: Int, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[ActivityComments] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/comments").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try {
          parseWithRateLimits(request).extract[List[ActivityComments]]
          } match {
            case Success(comments) => comments
            case Failure(error) => throw new RuntimeException(s"Could not parse activity comments: $error")
          }

    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  // List the athletes who have 'kudosed' the specified activity
  override def listActivityKudoers(activity_id: Int, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[AthleteSummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/kudos").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try {
          parseWithRateLimits(request).extract[List[AthleteSummary]]
          } match {
            case Success(athleteList) => athleteList
            case Failure(error) => throw new RuntimeException(s"Could not parse list of kudosers for activity: $error")
          }

    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  // List photos associated with a specified activity
  override def listActivityPhotos(id: Int): List[Photo] = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$id/photos")
    if (request.asString.isSuccess) {
      Try {
        parseWithRateLimits(request).extract[List[Photo]]
      } match {
        case Success(photos) => photos
        case Failure(error) => throw new RuntimeException(s"Could not parse list of photos: $error")
      }
    } else List()
  }

  // Create an activity (requires Write permissions, untested)
  override def createActivity(name: String, `type`: String, startDateLocal: DateTime, elapsedTime: Int,
                     description: Option[String], distance: Option[Float]): Activity = {
    val request = Http("https://www.strava.com/api/v3/activities").header("Authorization", authString).method("post")
      .postForm(Seq(("name", name),("elapsed_time",elapsedTime.toString), ("distance", distance.get.toString), ("start_date_local", startDateLocal.toString(ISODateTimeFormat.dateTime())),
      ("type", `type`)))
    Try { parseWithRateLimits(request).extract[Activity] } match {
      case Success(activity) => activity
      case Failure(error) => throw new RuntimeException("Could not create activity: $error")
    }
  }

  // Retrieve detailed information about a specified activity
  override def retrieveActivity(activity_id: Int, includeEfforts: Option[Boolean] = None): Activity = {
    var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id").header("Authorization", authString)
    Map[String, Option[Boolean]]("includeEfforts" -> includeEfforts)
      .map(params => params._2.map(opt => {
      request = request.param(params._1, params._2.get.toString)
    }))

    //if detailed activity
    if (parseWithRateLimits(request).\("resource_state").extract[Int] == 3) {
      if (parseWithRateLimits(request).\("athlete").\("resource_state").extract[Int] == 2) {
        Try {
          parseWithRateLimits(request).extract[DetailedActivity]
        } match {
          case Success(activity) => activity
          case Failure(error) => throw new RuntimeException(s"Could not parse activity: $error")
        }
      } else {
        Try {
          parseWithRateLimits(request).extract[PersonalDetailedActivity]
        } match {
          case Success(activitySummary) => activitySummary
          case Failure(error) => throw new RuntimeException(s"Could not parse personal activitySummary: $error")
        }
      }
    } else {
      if (parseWithRateLimits(request).\("athlete").\("resource_state").extract[Int] == 2) {
        Try { parseWithRateLimits(request).extract[ActivitySummary] } match {
          case Success(activity) => activity
          case Failure(error) => throw new RuntimeException(s"Could not parse activity: $error")
        }
      } else {
        Try { parseWithRateLimits(request).extract[PersonalActivitySummary] } match {
          case Success(activitySummary) => activitySummary
          case Failure(error) => throw new RuntimeException(s"Could not parse personal activitySummary: $error")
        }
      }
    }
  }

  // Update an activity (requires Write permissions, untested)
  override def updateActivity(activity_id: Long, name: Option[String], `type`: Option[String], `private`: Option[Boolean], commute: Option[Boolean],
                     trainer: Option[Boolean], gearId: Option[String], description: Option[String]): Activity = {
    var request = Http(s"https://www.strava.com/api/v3/activities/$activity_id").header("Authorization", authString).method("put")
    val tempMap = Map[String, Option[Any]]("name" -> name, "type" -> `type`, "private" -> `private`, "commute" -> commute,
      "trainer" -> trainer, "gear_id" -> gearId, "description" -> description)
    tempMap.map(params => params._2.map(opt => { request = request.params((params._1, params._2.get.toString)) }))

    Try { parseWithRateLimits(request).extract[DetailedActivity] } match {
      case Success(activity) => activity
      case Failure(error) => throw new RuntimeException(s"Could not update activity: $error")
    }
  }

  // Delete an activity (requires Write permissions, untested)
  override def deleteActivity(id: Long): Boolean = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$id").method("delete")
      Try { request.asString.statusLine.equals(204) } match {
        case Success(bool) => bool
        case Failure(error) => throw new RuntimeException(s"Could not delete activity: $error")
      }
  }

  // Lists activities associated with the currently authenticated athlete
  override def listAthleteActivities(before: Option[Int] = None, after: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[PersonalActivitySummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/athlete/activities").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[PersonalActivitySummary]] } match {
          case Success(activities) => activities
          case Failure(error) => throw new RuntimeException(s"Could not parse list of activities: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  // List activities associated with current athlete and his/her friends (activity feed from Strava)
  override def listFriendsActivities(page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[ActivitySummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/activities/following").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[ActivitySummary]] } match {
          case Success(activities) => activities
          case Failure(error) => throw new RuntimeException(s"Could not parse friends' activities: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  override def listActivityZones(id: Int): List[ActivityZones] = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$id/zones").header("Authorization", authString)
    Try {
      parseWithRateLimits(request).extract[List[ActivityZones]]
    } match {
      case Success(zones) => zones
      case Failure(error) => throw new RuntimeException(s"Could not parse activty zones: $error")
    }
  }

  override def listActivityLaps(activity_id: Int): List[LapEffort] = {
    val request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/laps").header("Authorization", authString)
    Try {
      parseWithRateLimits(request).extract[List[LapEffort]]
    } match {
      case Success(laps) => laps
      case Failure(error) => throw new RuntimeException(s"Could not parse activty laps: $error")
    }
  }

  // Retrieve a detailed description of the specified club id
  override def retrieveClub(club_id: Int): Club = {
    val request = Http(s"https://www.strava.com/api/v3/clubs/$club_id").header("Authorization", authString)
    Try {
      parseWithRateLimits(request).extract[Club]
    } match {
      case Success(club) => club
      case Failure(error) => throw new RuntimeException(s"Could not parse club: $error")
    }
  }

  // Return a list of clubs that the authenticated athlete is part of
  override def listAthleteClubs: List[ClubSummary] = {
    val request = Http(s"https://www.strava.com/api/v3/athlete/clubs").header("Authorization", authString)
    Try {
      parseWithRateLimits(request).extract[List[ClubSummary]]
    } match {
      case Success(clubs) => clubs
      case Failure(error) => throw new RuntimeException(s"Could not parse clubs: $error")
    }
  }

  override def listClubMembers(club_id: Int, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[AthleteSummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/clubs/$club_id/members").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[AthleteSummary]] } match {
          case Success(members) => members
          case Failure(error) => throw new RuntimeException(s"Could not parse club members: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  override def listClubActivities(club_id: Int, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[ActivitySummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/clubs/$club_id/activities").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        parseWithRateLimits(request).extract[List[ActivitySummary]]
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  override def retrieveAthleteGear(gear_id: String): Gear = {
    val request = Http(s"https://www.strava.com/api/v3/gear/$gear_id").header("Authorization", authString)
    parseWithRateLimits(request).extract[Gear]
  }

  override def retrieveSegment(segment_id: Int): Segment = {
    val request = Http(s"https://www.strava.com/api/v3/segments/$segment_id").header("Authorization", authString)
    parseWithRateLimits(request).extract[Segment]
  }

  override def listAthleteStarredSegments(page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[SegmentSummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/segments/starred").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        Try { parseWithRateLimits(request).extract[List[SegmentSummary]] } match {
          case Success(segments) => segments
          case Failure(error) => throw new RuntimeException(s"Could not parse starred segments: $error")
        }
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  override def listStarredSegments(athlete_id: Int, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): List[SegmentSummary] = {
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/clubs/$athlete_id/activities").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        parseWithRateLimits(request).extract[List[SegmentSummary]]
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1)).toList.flatten
  }

  override def listEfforts(segment_id: Int, athlete_id: Option[Int] = None, start_date_local: Option[DateTime] = None, end_date_local: Option[DateTime] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[SegmentEffort] = {
    var request = Http(s"https://www.strava.com/api/v3/segments/$segment_id/all_efforts").header("Authorization", authString)
      start_date_local.map(s => request = request.param("start_date_local", s.toString))
      end_date_local.map(s => request = request.param("end_date_local", s.toString))
      page.map(p => request = request.param("page", p.toString))
      per_page.map(pp => request = request.param("per_page", pp.toString))
    parseWithRateLimits(request).extract[List[SegmentEffort]]
  }

  override def listSegmentLeaderboards(segment_id: String, gender: Option[String] = None, age_group: Option[String] = None, weight_class: Option[String] = None, following: Option[Boolean] = None, club_id: Option[Int] = None, date_range: Option[String] = None, context_entries: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None, retrieveAll: Boolean = false): SegmentLeaderBoards = {
    var counter = 0
    var limit = 0
    val entries = Iterator.continually {
      counter = counter + 1
      var request = Http(s"https://www.strava.com/api/v3/segments/$segment_id/leaderboard").header("Authorization", authString)
        if (retrieveAll) {
          request = request.param("page", counter.toString)
          request = request.param("per_page", "200")
        } else {
          page.map(p => request = request.param("page", p.toString))
          per_page.map(pp => request = request.param("per_page", pp.toString))
        }
        gender.map(g => request = request.param("gender", g))
        age_group.map(g => request = request.param("age_group", g))
        weight_class.map(g => request = request.param("weight_class", g))
        following.map(g => request = request.param("following", g.toString))
        club_id.map(g => request = request.param("club_id", g.toString))
        date_range.map(g => request = request.param("date_range", g))
        context_entries.map(g => request = request.param("context_entries", g.toString))

        val body = parseWithRateLimits(request)
        val count = (body \\ "entry_count").extract[Int]
        limit = count/200
        (body \\ "entries").extract[List[LeaderBoardEntry]]
    }.takeWhile(_.size != 0 && (retrieveAll || counter == 1) && counter <= limit).toList.flatten
    SegmentLeaderBoards(entries.size, entries)
  }

  override def exploreSegments(bounds: List[Float], activity_type: Option[String] = None, min_cat: Option[Int] = None, max_cat: Option[Int] = None): List[SegmentCondensed] = {
    var request = Http(s"https://www.strava.com/api/v3/segments/explore").header("Authorization", authString)
    val tempMap = Map[String, Option[Any]]("activity_type" -> activity_type, "min_cat" -> min_cat, "max_cat" -> max_cat)
    request = request.param("bounds", bounds.mkString(","))
    tempMap.map(params => params._2.map(opt => { request = request.param(params._1, params._2.get.toString) }))
    (parseWithRateLimits(request) \\ "segments").extract[List[SegmentCondensed]]
  }

  override def retrieveSegmentEffort(effort_id: BigInt): SegmentEffort = {
    val request = Http(s"https://www.strava.com/api/v3/segment_efforts/$effort_id").header("Authorization", authString)
    parseWithRateLimits(request).extract[SegmentEffort]
  }

  override def retrieveActivityStream(activity_id: String, stream_types: Option[String] = None): List[Streams] = {
    val types = if (!stream_types.isDefined) {
      "time,latlng,distance,altitude,velocity_smooth,heartrate,cadence,watts,temp,moving,grade_smooth"
    } else { stream_types.get }
    val request = Http(s"https://www.strava.com/api/v3/activities/$activity_id/streams/"+types).header("Authorization", authString).param("resolution", "high")
      parseWithRateLimits(request).extract[List[JObject]].map(parseStream(_)).flatten
  }

  override def retrieveRouteStream(route_id: String): List[Streams] = {
    val request = Http(s"https://www.strava.com/api/v3/routes/$route_id/streams").header("Authorization", authString)
    parseWithRateLimits(request).extract[List[JObject]].map(parseStream(_)).flatten
  }

  override def retrieveEffortStream(effort_id: String, stream_types: Option[String] = None): List[Streams] = {
    val types = if (!stream_types.isDefined) {
      "time,latlng,distance,altitude,velocity_smooth,heartrate,cadence,watts,temp,moving,grade_smooth"
    } else { stream_types.get }
    val request = Http(s"https://www.strava.com/api/v3/segment_efforts/$effort_id/streams/"+types).header("Authorization", authString).param("resolution", "high")
      parseWithRateLimits(request).extract[List[JObject]].map(parseStream(_)).flatten
  }

  override def retrieveSegmentStream(segment_id: String, stream_types: Option[String] = None): List[Streams] = {
    val types = if (!stream_types.isDefined) {
      "time,latlng,distance,altitude"
    } else { stream_types.get }
    val request = Http(s"https://www.strava.com/api/v3/segments/$segment_id/streams/"+types).header("Authorization", authString).param("resolution", "high")
    parseWithRateLimits(request).extract[List[JObject]].map(parseStream(_)).flatten
  }

  def parseStream(streamData: JValue): Option[Streams] = {
    Try {
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
        case "watts_calc" => streamData.extract[Watts]
        case "temp" => streamData.extract[Temp]
        case "moving" => streamData.extract[Moving]
        case "grade_smooth" => streamData.extract[Grade]
      }
    } match {
      case Success(a) => Some(a)
      case Failure(e) => None
    }
  }

  // Upload an activity from a file (requires Write permissions, untested)
  override def uploadActivity(activity_type: Option[String], name: Option[String], description: Option[String], `private`: Option[Int],
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
  override def checkUploadStatus(upload_id: Int, external_id: String, activity_id: Option[Int] = None, status: String, error: Option[String] = None): UploadStatus = {
    val request = Http(s"https://www.strava.com/api/v3/uploads/$upload_id").header("Authorization", authString)
      .params(Seq(("external_id", external_id), ("activity_id", activity_id.get.toString), ("status", status), ("error", error.get)))
    Try { parseWithRateLimits(request).extract[UploadStatus] } match {
      case Success(status) => status
      case Failure(error) => throw new RuntimeException(s"Could not parse upload status: $error")
    }
  }

  override def checkRateLimits: RateLimit = {
    val request = Http(s"https://www.strava.com/api/v3/athlete").header("Authorization", authString)
      marshalRateLimits(request.asString.headers)
  }

  private[this] def marshalRateLimits(headers: Map[String, IndexedSeq[String]]): RateLimit = {
    Try {
      val limits = headers("X-RateLimit-Limit").head.split(",")
      val usages = headers("X-RateLimit-Usage").head.split(",")
      RateLimit(
        quarterly= Rates(limits.head.toLong, usages.head.toLong),
        daily= Rates(limits.last.toLong, usages.last.toLong))
    } match {
      case Success(rateLimits) => rateLimits
      case Failure(error) => throw new RuntimeException(s"Could not parse rate limits from headers: $error")
    }
  }

  private[this] def parseWithRateLimits(request: HttpRequest): JValue = {
    val requestString = request.asString
    val rateLimits = marshalRateLimits(requestString.headers)
    if (rateLimits.rateLimited)
      throw new RateLimitException
    else
      parse(requestString.body)
  }
}

