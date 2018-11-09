package dragonball

sealed trait Especie {
  def tipoEnergia: TipoEnergia
}

case class Saiyajin(estado: EstadoSayajin, tieneCola: Boolean = true) extends Especie with ConKi {
  def perderCola(): Saiyajin = estado match {
    case MonoGigante => setEstado(Normal).copy(tieneCola = false)
    case _ => copy(tieneCola = false)
  }

  def setEstado(nuevoEstado: EstadoSayajin): Saiyajin = copy(estado = Normal)

  def aumentarNivelSaiyajin: Saiyajin = {
    estado match {
      case MonoGigante => throw new SaiyajinException
      case Super(nivel) => setEstado(Super(nivel + 1))
      case _ => setEstado(Super(1))
    }
  }

}

case class Humano() extends Especie with ConKi

case class Namekusein() extends Especie with ConKi

//poderes curativos

case class Monstruo(formaDeDigerir: FormaDeDigerir) extends Especie with ConKi

//se comen a sus oponenentes

case class Androide() extends Especie with ConBateria

// no tienen ki, no necesitan comer y no pueden quedar inconscientes

case class Fusionado(especieOriginal: Especie)