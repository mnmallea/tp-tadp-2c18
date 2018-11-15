package dragonball

import org.scalatest.{FreeSpec, Matchers}
import Criterios._
import dragonball.Ataques.Explotar
import dragonball.Movimientos.{CargarKi, UsarItem}

class PlanDeAtaqueTest extends FreeSpec with Matchers {
  "planes" - {
    val androide = Guerrero("androide1785",
      List(Municion(ArmaDeFuego, 3), ArmaDeFuego, Municion(ArmaRoma, 0), ArmaRoma),
      Energia(100, 200), Androide(), Vivo, List[Movimiento](UsarItem(ArmaDeFuego), Explotar, CargarKi))
    val humano = Guerrero("iron man", List(Municion(ArmaDeFuego, 2), Municion(ArmaDeFuego, 1)),
      Energia(100, 200), Humano(), Vivo, List())

    "el androide no puede obtener su plan de ataque porque no tiene movimientos" in {
      androide.copy(movimientos = List())
        .planDeAtaqueContra(humano, 1)(criterioCagon) shouldBe None
    }

    val androide2 = androide.copy(movimientos = List(Explotar,Explotar,Explotar))

    "mejor movimiento del androide2" in {
      androide2.movimientoMasEfectivoContra(humano)(criterioCagon)
    }

    "el androide no puede obtener su plan de ataque porque sus movimientos son malos" in {
      androide2.planDeAtaqueContra(humano, 3)(criterioCagon) shouldBe None
    }

    "Deberia crear un plan de ataque" in {
      androide.planDeAtaqueContra(humano, 10)(criterioOptimista) should not be empty
    }

    "El tamanio del plan deberia ser 10" in {
      androide.planDeAtaqueContra(humano, 10)(criterioOptimista).get.size shouldBe 10
    }

  }

}
