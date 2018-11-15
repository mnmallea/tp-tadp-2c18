package dragonball

import dragonball.Ataques.Explotar
import dragonball.Criterios._
import dragonball.Movimientos._
import org.scalatest.{FreeSpec, Matchers}

class CriteriosTest extends FreeSpec with Matchers {
  "Test para mejor movimiento " - {
    val goku = Guerrero("goku", List(ArmaFilosa), Energia(100, 100),
      Saiyajin(Normal), Vivo, List[Movimiento](DejarseFajar, CargarKi, UsarItem(ArmaFilosa)))
    val vegeta = Guerrero("vegeta", List(), Energia(100, 100),
      Saiyajin(MonoGigante), Vivo, List())

    "vegueta no tiene un mejor movimiento porque no tiene movimientos :(" in {
      vegeta.movimientoMasEfectivoContra(goku)(criterioCagon) shouldBe None
    }

    "El mejor movimiento de goku deberia ser cargar ki con el criterio de daniar mas" in {
      goku.movimientoMasEfectivoContra(vegeta)(criterioMayorDanioRealizado) shouldBe Some(UsarItem(ArmaFilosa))
    }

    "no deberia tener mejor movimiento con el criterio cagon" - {
      val guerrero = goku.copy(movimientos = List[Movimiento](Explotar, Explotar), especie = Androide())

      val parejaFinal = guerrero.realizarMovimientoContra(Explotar, vegeta)
      "el guerrero explota y deberia morir" in {
        parejaFinal.atacante.estado shouldBe Muerto
      }
      "el guerrero deberia quedar con su energia en 0" in {
        parejaFinal.atacante.energiaActual shouldBe 0
      }

      "no deberia tener un mejor movimiento porque todos implican perder energia" in {
        guerrero.movimientoMasEfectivoContra(vegeta)(criterioCagon) shouldBe None
      }

    }

  }


}
