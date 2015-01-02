import org.scalatest.{FlatSpec, Matchers}
import scrava.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, duration}

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

//  it should "retrieve activity details" in {
//    val activity = testClient.retrieveActivity(118293263).merge.asInstanceOf[Activity]
//    activity.id should equal(118293263)
//  }

  it should "retrieve activity summary" in {
    val activity = testClient.retrieveActivity(191823321).merge.asInstanceOf[ActivitySummary]
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
}
