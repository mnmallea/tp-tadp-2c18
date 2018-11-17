package dragonball

sealed trait Especie {
  def tipoEnergia: TipoEnergia

  /* no hace nada por defecto */
  def comer(pareja: Pareja): Pareja = pareja
}

case class Saiyajin(estado: EstadoSayajin, tieneCola: Boolean = true) extends Especie with ConKi {
  def perderColaDe(guerrero: Guerrero): Guerrero = {
    val guerreroLastimado = guerrero enegiaActual 1 especie this.perderCola
    this.estado match {
      case MonoGigante => guerreroLastimado.especie(this.setEstado(Normal)).estado(Inconsciente)
      case _ => guerreroLastimado
    }
  }

  def perderCola: Saiyajin = copy(tieneCola = false)

  def setEstado(nuevoEstado: EstadoSayajin): Saiyajin = copy(estado = nuevoEstado)

  def proximoNivelSaiyayin: Int = {
    estado match {
      case MonoGigante => throw new SaiyajinException
      case Super(nivel) => nivel + 1
      case _ => 1
    }
  }

}

case class Humano() extends Especie with ConKi

case class Namekusein() extends Especie with ConKi

//poderes curativos

case class Monstruo(formaDeDigerir: FormaDeDigerir) extends Especie with ConKi{
  override def comer(pareja: Pareja): Pareja = {
    if(pareja.atacante.energiaActual >= pareja.atacado.energiaActual)
      pareja.copy(formaDeDigerir(pareja.atacante)(pareja.atacado), pareja.atacado.estado(Muerto))
    else
      pareja
  }
}

//se comen a sus oponenentes

case class Androide() extends Especie with ConBateria

// no tienen ki, no necesitan comer y no pueden quedar inconscientes

case class Fusionado(especieOriginal: Especie) extends Especie {
  def tipoEnergia: TipoEnergia = especieOriginal.tipoEnergia
}