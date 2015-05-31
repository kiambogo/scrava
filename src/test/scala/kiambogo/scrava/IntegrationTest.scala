package kiambogo.scrava

import kiambogo.scrava.models._
import org.scalatest.{FlatSpec, Matchers}

class IntegrationTest extends FlatSpec with Matchers {

  val testToken = "21b4fe41a815dd7de4f0cae7f04bbbf9aa0f9507"
  val testClient = new ScravaClient(testToken)

  "Scrava" should
    "retrieve Athlete profile" in {
    val ath = testClient.retrieveAthlete().merge.asInstanceOf[DetailedAthlete]
    ath.firstname should equal("Strava")
    ath.lastname should equal("Testing")
    ath.email should equal("paul+test@strava.com")
  }

  it should "retrieve friends" in {
    val friends = testClient.listAthleteFriends()
    (friends map (_.id == 227615) contains true) should equal(true)
  }

  it should "retrieve gear" in {
    val ath = testClient.retrieveAthlete().merge.asInstanceOf[DetailedAthlete]
    val gear: List[GearSummary] = ath.bikes ::: ath.shoes
    gear.size should equal(0)
  }

  it should "retrieve activities" in {
    val activities = testClient.listAthleteActivities()
    (activities map (_.id == 118293263) contains true) should equal(true)
  }

  it should "retrieve followers" in {
    val followers = testClient.listAthleteFollowers()
    (followers map (_.id == 227615) contains true) should equal(true)
  }

  it should "retrieve mutual followings" in {
    val followers = testClient.listMutualFollowing(227615)
    (followers map (_.id == 8758) contains true) should equal(true)
  }

  it should "retrieve KOMs" in {
    val ath = testClient.retrieveAthlete().merge.asInstanceOf[DetailedAthlete]
    val koms = testClient.listAthleteKOMs(ath.id)
    koms.size should equal(0)
  }

  it should "retrieve activity comments" in {
    val comments = testClient.listActivityComments(118293263)
    (comments map (_.text == "test comment") contains true) should equal(true)
  }

  it should "retrieve activity kudoers" in {
    val kudoers = testClient.listActivityKudoers(118293263)
    (kudoers map (_.id == 3776) contains true) should equal(true)
  }

  it should "retrieve activity details" in {
    val activity = testClient.retrieveActivity(118293263).asInstanceOf[PersonalDetailedActivity]
    activity.id should equal(118293263)
  }

  it should "retrieve activity summary" in {
    val activity = testClient.retrieveActivity(191823321).asInstanceOf[DetailedActivity]
    activity.id should equal(191823321)
  }

  it should "retrieve activity photos" in {
    val photos = testClient.listActivityPhotos(118293263)
    photos.size should equal(0)
  }

  it should "retrieve friends activities" in {
    val activities = testClient.listFriendsActivities()
    activities.size should equal(30)
  }

  it should "retrieve activity zones" in {
    val zones = testClient.listActivityZones(103221154)
    zones(0).score should equal(Some(12))
  }

  it should "retrieve activity laps" in {
    val laps = testClient.listActivityLaps(103373338)
    laps(0).name should equal("Lap 1")
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
    (members map (_.id == 3545423) contains true) should equal(true)
  }

  it should "retrieve a list of club activities" in {
    val activities = testClient.listClubActivities(45255)
    println(activities)
    activities.exists(a => a.id == 270922814) should equal(true)
  }

//  TODO
//  it should "retrieve a gear item" in {
//    val gear = testClient.retrieveAthleteGear("b77076")
//    gear.name should equal("burrito burner")
//  }

  it should "retrieve a detailed segment" in {
    val segment = testClient.retrieveSegment(229781)
    segment.name should equal("Hawk Hill")
  }

  it should "retrieve a list of athlete starred segments" in {
    val segments = testClient.listAthleteStarredSegments()
    segments(0).name should equal("Hawk Hill")
  }

  it should "retrieve a list of segment efforts" in {
    val efforts = testClient.listEfforts(229781)
    efforts(0).name should equal("Hawk Hill")
    efforts(0).elapsed_time should equal(769)
  }

  it should "retrieve a segment stream" in {
    val stream = testClient.retrieveSegmentStream("229781", Some("latlng"))
    stream(0).asInstanceOf[LatLng].resolution should equal("high")
    stream(1).asInstanceOf[Distance].original_size should equal(114)
  }

}
