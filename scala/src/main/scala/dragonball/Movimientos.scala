package dragonball

case class Pareja(atacante: Guerrero, atacado: Guerrero) {
  def mapAtacante(f: Guerrero => Guerrero): Pareja = {
    Pareja(f(atacante), atacado)
  }

  def mapAtacado(f: Guerrero => Guerrero): Pareja = {
    Pareja(atacante, f(atacado))
  }
}


object Movimientos {

  object DejarseFajar extends Movimiento {
    def apply(pareja: Pareja): Pareja = pareja
  }

  object CargarKi extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
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

  //solo los monstruos
  object ComerAlOponente extends Movimiento {
    def apply(pareja: Pareja): Pareja =  ???
  }
    
  //solo saiyajin
  object ConvertirseEnMono extends Movimiento { 
    def apply(pareja: Pareja): Pareja =  ???
  } 
   
  //solo saiyajin
  object ConvertirseEnSuperSaiyajin extends Movimiento {
    def apply(pareja: Pareja): Pareja =  ???
  } 
   
  //humano, saiyajin o namekusei
  object Fusion extends Movimiento {
    def apply(pareja: Pareja): Pareja =  ???
  } 
    
  //Namekuseins, monstruos y guerreros que tengan 7 esferas del dragon en su inventario
  object Magia extends Movimiento {
    def apply(pareja: Pareja): Pareja =  ???
  } 
   
  object Ataques extends Movimiento {
    def apply(pareja: Pareja): Pareja =  ???
  }
}