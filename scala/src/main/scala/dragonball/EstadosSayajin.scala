package dragonball

sealed trait EstadoSayajin

case class Super(nivel: Int) extends EstadoSayajin
case object Normal extends EstadoSayajin
case object MonoGigante extends EstadoSayajin