import org.junit.runner._
import org.scalatestplus.play._
import org.specs2.runner._
import scrava.controllers._
import scrava.models.{GearSummary, DetailedAthlete}

import scala.concurrent.{duration, Await}
import scala.concurrent.duration.Duration


/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends PlaySpec with OneAppPerSuite {

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
      friends.size must equal(2)
    }

    "retrieve gear" in {
      val futureAth = testClient.retrieveAthlete()
      val ath = Await.result(futureAth, Duration(5, duration.SECONDS)).merge.asInstanceOf[DetailedAthlete]
      val gear: List[GearSummary] = ath.bikes ::: ath.shoes
      gear.size must equal(0)
    }

    "retrieve activity" in {
      val futureAth = testClient.listAthleteActivities()
      val ath = Await.result(futureAth, Duration(5, duration.SECONDS))

      ath.size must equal(7)
    }


  }
}
