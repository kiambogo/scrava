import org.junit.runner._
import org.scalatestplus.play._
import org.specs2.runner._
import scrava.controllers._
import scrava.models._

import scala.concurrent.{duration, Await}
import scala.concurrent.duration.Duration

@RunWith(classOf[JUnitRunner])
class IntegrationTest extends PlaySpec with OneAppPerSuite {

  val testToken = "21b4fe41a815dd7de4f0cae7f04bbbf9aa0f9507"
  val testClient = new ScravaClient(testToken)

  "Application" should {

    "retrieve Athlete profile" in {
      val futureAth = testClient.retrieveAthlete()
      val ath = Await.result(futureAth, Duration(5, duration.SECONDS)).merge.asInstanceOf[DetailedAthlete]
      ath.firstname must equal("Strava")
      ath.lastname must equal("Testing")
      ath.email must equal("paul+test@strava.com")
    }

    "retrieve friends" in {
      val futureFriends = testClient.listAthleteFriends()
      val friends = Await.result(futureFriends, Duration(5, duration.SECONDS))
      (friends map (_.id == 227615) contains true) must equal(true)
    }

    "retrieve gear" in {
      val futureAth = testClient.retrieveAthlete()
      val ath = Await.result(futureAth, Duration(5, duration.SECONDS)).merge.asInstanceOf[DetailedAthlete]

      val gear: List[GearSummary] = ath.bikes ::: ath.shoes
      gear.size must equal(0)
    }

    "retrieve activities" in {
      val futureActivities = testClient.listAthleteActivities()
      val activities = Await.result(futureActivities, Duration(5, duration.SECONDS))
      (activities map (_.id == 118293263) contains true) must equal(true)
    }

    "retrieve followers" in {
      val futureFollowers = testClient.listAthleteFollowers()
      val followers = Await.result(futureFollowers, Duration(5, duration.SECONDS))
      (followers map (_.id == 227615) contains true) must equal(true)
    }

    "retrieve mutual followings" in {
      val futureFollowers = testClient.listMutualFollowing(227615)
      val followers = Await.result(futureFollowers, Duration(5, duration.SECONDS))
      (followers map (_.id == 8758) contains true) must equal(true)
    }

    "retrieve KOMs" in {
      val futureAth = testClient.retrieveAthlete()
      val ath = Await.result(futureAth, Duration(5, duration.SECONDS)).merge.asInstanceOf[DetailedAthlete]

      val futureKoms = testClient.listAthleteKOMs(ath.id)
      val koms = Await.result(futureKoms, Duration(5, duration.SECONDS))
      koms.size must equal(0)
    }

    "retrieve activity comments" in {
      val futureComments = testClient.listActivityComments(118293263)
      val comments = Await.result(futureComments, Duration(5, duration.SECONDS))
      (comments map (_.text == "test comment") contains true) must equal(true)
    }

    "retrieve activity kudoers " in {
      val futureKudoers = testClient.listActivityKudoers(118293263)
      val kudoers = Await.result(futureKudoers, Duration(5, duration.SECONDS))
      (kudoers map (_.id == 3776) contains true) must equal(true)
    }

    "retrieve activity details " in {
        val futureActivity = testClient.retrieveActivity(118293263)
      val activity = Await.result(futureActivity, Duration(5, duration.SECONDS)).merge.asInstanceOf[Activity]
      activity.id must equal(118293263)
    }

    "retrieve activity photos " in {
      val futurePhotos = testClient.listActivityPhotos(118293263)
      val photos = Await.result(futurePhotos, Duration(5, duration.SECONDS))
      photos.size must equal(0)
    }


  }
}
