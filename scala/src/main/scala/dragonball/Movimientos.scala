import Especies._
import Guerreros._
import Items.Item

package object Movimientos {

  case class Pareja(atacante: Guerrero, atacado: Guerrero) {
    def mapAtacante(f: Guerrero => Guerrero): Pareja = {
      Pareja(f(atacante), atacado)
    }

    def mapAtacado(f: Guerrero => Guerrero): Pareja = {
      Pareja(atacante, f(atacado))
    }
  }

  type Movimiento = Pareja => Pareja

  object DejarseFajar extends Movimiento {
    def apply(pareja: Pareja): Pareja = pareja
  }

  object CargarKi extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante( atacante => atacante.especie match {
        case Androide() => atacante
        case Saiyajin(Super(nivel)) => atacante.aumentarEnergia(150 * nivel)
        case _ => atacante.aumentarEnergia(100)
      }
      )
    }
  }

  case class UsarItem(item: Item) extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      if (pareja.atacante.tieneItem(item)) {
        item(pareja)
      } else {
        pareja
      }
    }
  }

}
