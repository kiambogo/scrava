package kiambogo.scrava


import kiambogo.scrava.models._
import net.liftweb.json.parse

import scala.util.{Failure, Success, Try}
import scalaj.http.Http

trait ClientFactory {
  def instance(token: String): Client

  def instance(client_id: String, client_secret: String, code: String): (Client, AthleteSummary)
}

class ClientFactoryImpl extends ClientFactory {

  override def instance(token: String): Client = new ScravaClient(token)

  /**
    * Return a StravaClient from http://strava.github.io/api/v3/oauth/#post-token
    *
    * @param client_id     application’s ID, obtained during registration
    * @param client_secret application’s secret, obtained during registration
    * @param code          authorization code (from callback URL)
    * @return a ScravaClient and the AthleteSummary returned by the API.
    */
  override def instance(client_id: String, client_secret: String, code: String): (Client, AthleteSummary) = {
    val request = Http(s"https://www.strava.com/oauth/token").method("post")
      .postForm(Seq(("client_id", client_id), ("client_secret", client_secret), ("code", code)))
    implicit val formats = net.liftweb.json.DefaultFormats

    Try {
      parse(request.asString.body).extract[TokenExchange]
    } match {
      case Success(TokenExchange(access_token, token_type, athlete)) =>
        (new ScravaClient(access_token), athlete)
      case Failure(error) =>
        throw new RuntimeException(s"Could not get Token Exchange Athlete: $error")
    }
  }
}
