import Especies.Androide
import Guerreros.Inconsciente
import Movimientos.{Movimiento, Pareja}

package object Items {
  type Item = Movimiento

  val ArmaRoma: Pareja => Pareja = (pareja: Pareja) => {
    pareja.mapAtacado { guerrero =>
      guerrero.especie match {
        case Androide() => guerrero
        case _ if guerrero.energia < 300 => guerrero.copy(estado = Inconsciente)
      }
    }
  }

  case object SemillaDelErmitanio extends Item {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(_.recuperarPotencial)
    }
  }

}