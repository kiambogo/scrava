package kiambogo.scrava

import kiambogo.scrava.models._
import org.scalatest.{FlatSpec, Matchers}

class IntegrationTest extends FlatSpec with Matchers {

  private val testToken = "21b4fe41a815dd7de4f0cae7f04bbbf9aa0f9507"
  private val testClient = new ScravaClient(testToken)

  "Scrava" should
    "retrieve Athlete profile" in {
    val ath = testClient.retrieveAthlete().merge.asInstanceOf[DetailedAthlete]
    ath.firstname should equal("Strava")
    ath.lastname should equal("Testing")
    ath.email should equal("paul+test@strava.com")
  }

  it should "retrieve friends" in {
    val friends = testClient.listAthleteFriends()
    friends.exists(_.id == 227615) should equal(true)
  }

  it should "retrieve gear" in {
    val ath = testClient.retrieveAthlete().merge.asInstanceOf[DetailedAthlete]
    val gear: List[GearSummary] = ath.bikes ::: ath.shoes
    gear.size should equal(0)
  }

  it should "retrieve activities" in {
    val activities = testClient.listAthleteActivities()
    activities.exists(_.id == 118293263) should equal(true)
  }

  it should "retrieve activities with dates" in {
    val activities = testClient.listAthleteActivities(before = Some(1501245821), after=Some(1435708800))
    activities.size should equal(2)
  }

  it should "retrieve followers" in {
    val followers = testClient.listAthleteFollowers()
    followers.exists(_.id == 227615) should equal(true)
  }

  it should "retrieve mutual followings" in {
    val followers = testClient.listMutualFollowing(227615)
    followers.exists(_.id == 8758) should equal(true)
  }

  it should "retrieve KOMs" in {
    val ath = testClient.retrieveAthlete().merge.asInstanceOf[DetailedAthlete]
    val koms = testClient.listAthleteKOMs(ath.id)
    koms.size should equal(0)
  }

  it should "retrieve activity comments" in {
    val comments = testClient.listActivityComments(118293263)
    comments.exists(_.text == "test comment") should equal(true)
  }

  it should "retrieve activity kudoers" in {
    val kudoers = testClient.listActivityKudoers(118293263)
    kudoers.exists(_.id == 3776) should equal(true)
  }

  it should "retrieve activity details" in {
    val activity = testClient.retrieveActivity(118293263).asInstanceOf[PersonalDetailedActivity]
    activity.id should equal(118293263)
  }

  it should "retrieve activity summary" in {
    val activity = testClient.retrieveActivity(191823321).asInstanceOf[DetailedActivity]
    activity.id should equal(191823321)
  }

  it should "retrieve activity with all efforts" in {
    val activity = testClient.retrieveActivity(103373338, Some(true)).asInstanceOf[DetailedActivity]
    activity.segment_efforts.size should equal(34)
  }

  it should "retrieve activity with efforts" in {
    val activity = testClient.retrieveActivity(103373338).asInstanceOf[DetailedActivity]
    activity.segment_efforts.size should equal(12)
  }

  it should "retrieve activity photos" in {
    val photos = testClient.listActivityPhotos(118293263)
    photos.size should equal(0)
  }

  it should "retrieve friends activities" in {
    val activities = testClient.listFriendsActivities()
    activities.size should equal(28)
  }

  it should "retrieve activity laps" in {
    val laps = testClient.listActivityLaps(103373338)
    laps.head.name should equal("Lap 1")
  }

  it should "retrieve a club" in {
    val club = testClient.retrieveClub(45255)
    club.name should equal("Test Club")
  }

  it should "retrieve a list of clubs the user is in" in {
    val clubs = testClient.listAthleteClubs
    clubs.size should equal(1)
  }

  it should "retrieve a list of club members" in {
    val members = testClient.listClubMembers(45255)
    members.exists(_.id == 3545423) should equal(true)
  }

  it should "retrieve a list of club activities" in {
    val activities = testClient.listClubActivities(45255)
    activities.size should equal(0)
  }

//  TODO
//  it should "retrieve a gear item" in {
//    val gear = testClient.retrieveAthleteGear("b77076")
//    gear.name should equal("burrito burner")
//  }

  it should "retrieve a list of segments based on bounds (explore)" in {
    val bounds = List[Float](37.674887f, -122.595185f, 37.840461f, -122.280015f)
    val segments = testClient.exploreSegments(bounds)
    segments.size should equal(10)
  }

  it should "retrieve a detailed segment" in {
    val segment = testClient.retrieveSegment(229781)
    segment.name should equal("Hawk Hill")
  }

  it should "retrieve a list of athlete starred segments" in {
    val segments = testClient.listAthleteStarredSegments()
    segments.head.name should equal("Hawk Hill")
  }

  it should "retrieve a list of segment efforts" in {
    val efforts = testClient.listEfforts(229781)
    efforts.head.name should equal("Hawk Hill")
    efforts.head.elapsed_time should equal(769)
  }

  it should "retrieve a segment stream" in {
    val stream = testClient.retrieveSegmentStream("229781", Some("latlng"))
    stream(0).asInstanceOf[LatLng].resolution should equal(Some("high"))
    stream(1).asInstanceOf[Distance].original_size should equal(Some(114))
  }
}
