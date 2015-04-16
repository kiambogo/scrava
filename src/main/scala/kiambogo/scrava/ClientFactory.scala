package kiambogo.scrava

trait ClientFactory {
  def instance(token: String): Client
}

class ClientFactoryImpl extends ClientFactory {
  override def instance(token: String): Client = new ScravaClient(token)
}
