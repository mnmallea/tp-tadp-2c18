package dragonball

sealed trait EstadoSayajin {
  def proximoNivel: Option[Int]
}

case class Super(nivel: Int) extends EstadoSayajin {
  override def proximoNivel: Option[Int] = Some(nivel + 1)
}

case object Normal extends EstadoSayajin {
  override def proximoNivel: Option[Int] = Some(1)
}

case object MonoGigante extends EstadoSayajin {
  override def proximoNivel: Option[Int] = None
}