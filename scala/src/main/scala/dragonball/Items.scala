package dragonball

trait Item

trait ItemUsable extends Item {
  def apply(pareja: Pareja): Pareja
}


case object SemillaDelErmitanio extends ItemUsable {
  def apply(pareja: Pareja): Pareja = {
    pareja.mapAtacante(_.recuperarPotencial)
  }
}

trait Arma extends ItemUsable

case object ArmaRoma extends Arma {
  def apply(pareja: Pareja): Pareja = {
    pareja.mapAtacado { guerrero =>
      guerrero.especie match {
        case _: Androide => guerrero
        case _ if guerrero.energia.actual < 300 => guerrero.copy(estado = Inconsciente)
      }
    }
  }
}

case object ArmaFilosa extends Arma {
  def apply(pareja: Pareja): Pareja = {
    pareja.mapAtacado(_.disminuirEnergia(pareja.atacante.energiaActual % 100))
      .mapAtacado(atacado => atacado.especie match {
        case saiyajin: Saiyajin if saiyajin.tieneCola =>
          saiyajin perderColaDe atacado
        case _ => atacado
      })
  }
}

case object ArmaDeFuego extends Arma {
  def apply(pareja: Pareja): Pareja = {
    if (pareja.atacante.tieneMunicionDe(this)) {
      pareja.mapAtacado(atacado => atacado.especie match {
        case _: Humano => atacado.disminuirEnergia(20)
        case _: Namekusein if atacado.estado == Inconsciente => atacado.disminuirEnergia(10)
        case _ => atacado
      }).mapAtacante(_.gastarMunicion(this))
    } else
      pareja
  }
}

case object EsferaDeDragon extends Item

case object FotoDeLuna extends Item

case class Municion(arma: Arma, cantidad: Int) extends Item