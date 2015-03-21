package kiambogo.scrava

import kiambogo.scrava.models._
import org.joda.time.DateTime

trait Scrava {
  def listAthleteFriends(athlete_id: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary]

  def listAthleteFollowers(athlete_id: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary]

  def listMutualFollowing(athlete_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary]

  def retrieveAthlete(athlete_id: Option[Int] = None): Either[DetailedAthlete, AthleteSummary]

  def updateAthlete(city: String, state: String, country: String, sex: String, weight: Float): DetailedAthlete

  def listAthleteKOMs(athlete_id: Int, page: Option[Int] = None, resultsPerPage: Option[Int] = None): List[SegmentEffort]

  def listAthleteStats(athlete_id: Int): Stats

  def listActivityComments(activity_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[ActivityComments]

  def listActivityKudoers(activity_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary]

  def listActivityPhotos(id: Int): List[Photo]

  def createActivity(name: String, `type`: String, startDateLocal: DateTime, elapsedTime: Int, description: Option[String], distance: Option[Float]): Activity

  def retrieveActivity(activity_id: Int, includeEfforts: Option[Boolean] = None): Activity

  def updateActivity(activity_id: Long, name: Option[String], `type`: Option[String], `private`: Option[Boolean], commute: Option[Boolean], trainer: Option[Boolean], gearId: Option[String], description: Option[String]): Activity

  def deleteActivity(id: Long): Boolean

  def listAthleteActivities(before: Option[Int] = None, after: Option[Int] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[PersonalActivitySummary]

  def listFriendsActivities(page: Option[Int] = None, per_page: Option[Int] = None): List[ActivitySummary]

  def listActivityZones(id: Int): List[ActivityZones]

  def listActivityLaps(activity_id: Int): List[LapEffort]

  def retrieveClub(club_id: Int): Club

  def listAthleteClubs: List[ClubSummary]

  def listClubMembers(club_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[AthleteSummary]

  def listClubActivities(club_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[ActivitySummary]

  def retrieveAthleteGear(gear_id: String): Gear

  def retrieveSegment(segment_id: Int): Segment

  def listAthleteStarredSegments(page: Option[Int] = None, per_page: Option[Int] = None): List[SegmentSummary]
    
  def listStarredSegments(athlete_id: Int, page: Option[Int] = None, per_page: Option[Int] = None): List[SegmentSummary]

  def listEfforts(segment_id: Int, athlete_id: Option[Int] = None, start_date_local: Option[DateTime] = None, end_date_local: Option[DateTime] = None, page: Option[Int] = None, per_page: Option[Int] = None): List[SegmentEffort] 
    
  def listSegmentLeaderboards(segment_id: String, gender: Option[String], age_group: Option[String], weight_class: Option[String], following: Option[Boolean], club_id: Option[Int], date_range: Option[String], context_entries: Option[Int], page: Option[Int], per_page: Option[Int]): SegmentLeaderBoards 
    
  def segmentExplorer(bounds: List[Float], activity_type: Option[String], min_cat: Option[Int], max_cat: Option[Int]): SegmentCondensed
    
  def retrieveSegmentEffort(effort_id: BigInt): SegmentEffort
    
  def retrieveActivityStream(activity_id: String, stream_types: Option[String] = None): List[Streams]
    
  def retrieveEffortStream(effort_id: String, stream_types: Option[String] = None): List[Streams]
    
  def retrieveSegmentStream(segment_id: String, stream_types: Option[String] = None): List[Streams]
    
  def uploadActivity(activity_type: Option[String], name: Option[String], description: Option[String], `private`: Option[Int], trainer: Option[Int], data_type: String, external_id: Option[String], file: Array[Byte]): Boolean 

  def checkUploadStatus(upload_id: Int, external_id: String, activity_id: Option[Int] = None, status: String, error: Option[String] = None): UploadStatus
}

