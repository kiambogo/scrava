package kiambogo.scrava.models

case class RateLimit(quarterly: Rates, daily: Rates) {
  def rateLimited: Boolean = quarterly.rateLimited || daily.rateLimited
}

case class Rates(limit: Long, usage: Long) {
  def rateLimited: Boolean = usage >= limit
}

case class RateLimitException(message: String = "Application rate limits exceeded") extends RuntimeException

