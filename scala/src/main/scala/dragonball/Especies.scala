package dragonball

sealed trait Especie

sealed trait EstadoSayajin

case class Super(nivel: Int) extends EstadoSayajin

case class Normal() extends EstadoSayajin

case class MonoGigante() extends EstadoSayajin

case class Humano() extends Especie

case class Saiyajin(estado: EstadoSayajin) extends Especie // tiene cola y estado supersayayin
case class Namekusein() extends Especie //poderes curativos
case class Monstruo() extends Especie //se comen a sus oponenentes

case class Androide() extends Especie // no tienen ki, no necesitan comer y no pueden quedar inconscientes


//Opción para manejar energia

sealed trait TipoEnergia
case object Ki extends TipoEnergia
case object Bateria extends TipoEnergia

case class Energia(actual: Int, maxima: Int, tipo: TipoEnergia) {
  def aumentar(aumento: Int): Energia = copy(actual = (actual + aumento).min(maxima))
}