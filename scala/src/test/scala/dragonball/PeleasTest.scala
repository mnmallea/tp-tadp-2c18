package dragonball

import org.scalatest.{FreeSpec, Matchers}
import dragonball.Ataques._
import dragonball.Criterios._

class PeleasTest extends FreeSpec with Matchers {
  "Test de peleas entre guerreros" - {
    val drGero = Guerrero("Dr Gero", List(), Energia(100, 100), Androide(), Vivo, List(Onda(10)))
    val yamcha = Guerrero("Yamcha", List(), Energia(50, 100), Humano(), Vivo, List(GolpesNinja))
    val planDeDrGeroContraYamcha = drGero.planDeAtaqueContra(yamcha, 6)(criterioOptimista).get

    "El Dr Gero gano la pelea" in {
      /* El Dr Gero le va a pegar tres ondas y por cada una yamcha le responde con un golpe ninja,
        perdiendo 10 puntos por cada uno por lo que me devuelve el Dr Gero como ganador con 30 puntos menos */
      drGero.pelearContra(yamcha)(planDeDrGeroContraYamcha) shouldBe Ganador(drGero.disminuirEnergia(30))
    }

    val yamchaSS = yamcha.copy(energia = Energia(1000, 1000))

    "YamchaSS le dio pelea y no murio" in {
      /* Dr Gero va a pegar 6 ondas por lo que significa que va a perder 60 de energia y yamchaSS pierde 120 por las ondas
        mas 80 de los golpes ninjas */
      drGero.pelearContra(yamchaSS)(planDeDrGeroContraYamcha) shouldBe Peleando(Pareja(drGero.disminuirEnergia(60), yamchaSS.disminuirEnergia(180)))
    }

    val androide17 = Guerrero("A-17", List(), Energia(500, 500), Androide(), Vivo, List(GolpesNinja))
    val planDeDrContraA17 = drGero.planDeAtaqueContra(androide17, 6)(criterioOptimista).get

    "Dr Gero quiso pasarse de vivo y perdio" in {
      /* Dr Gero solo sabe tirar ondas que recargan a A-17 y hacen que el primero reste su energia y ademas A-17 responde con
         golpes por lo que con 5 rounds ya muere Dr Gero*/
      drGero.pelearContra(androide17)(planDeDrContraA17) shouldBe Ganador(androide17)
    }

  }
}
