package dragonball

import org.scalatest.{FreeSpec, Matchers}
import dragonball.Ataques._
import dragonball.Criterios._

class PeleasTest extends FreeSpec with Matchers {
  "Test de peleas entre guerreros" - {
    val drGero = Guerrero("Dr Gero", List(), Energia(100, 100), Androide(), Vivo, List(Onda(10)))
    val yamcha = Guerrero("Yamcha", List(), Energia(50, 100), Humano(), Vivo, List(GolpesNinja))
    val planDeDrGero = drGero.planDeAtaqueContra(yamcha, 6)(criterioOptimista).get

    "El Dr Gero gano la pelea" in {
      /* El androide le va a pegar tres ondas y por cada una yamcha le responde con un golpe ninja,
        perdiendo 10 puntos por cada uno por lo que me devuelve el androide con 30 puntos menos */
      drGero.pelearContra(yamcha)(planDeDrGero) shouldBe Ganador(drGero.disminuirEnergia(30))
    }

    val yamchaSS = yamcha.copy(energia = Energia(1000, 1000))

    "YamchaSS le dio pelea y no murio" in {
      drGero.pelearContra(yamchaSS)(planDeDrGero) shouldBe Peleando(Pareja(drGero, yamchaSS.disminuirEnergia(80)))
    }
  }
}
