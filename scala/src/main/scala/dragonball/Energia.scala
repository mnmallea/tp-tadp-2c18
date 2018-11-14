package dragonball


sealed trait TipoEnergia {
  def cantidadDeEnergiaAlExplotar(energiaActual: Int): Int
}

case object Ki extends TipoEnergia {
  def cantidadDeEnergiaAlExplotar(energiaActual: Int): Int = energiaActual * 2
}

case object Bateria extends TipoEnergia {
  def cantidadDeEnergiaAlExplotar(energiaActual: Int): Int = energiaActual * 3
}

trait ConKi {
  def tipoEnergia: TipoEnergia = Ki
}

trait ConBateria {
  def tipoEnergia: TipoEnergia = Bateria
}


case class Energia(actual: Int, maximo: Int) {

  def setActual(n: Int): Energia = copy(actual = n)

  def aumentar(aumento: Int): Energia = copy(actual = (actual + aumento).min(maximo))

  def disminuir(disminucion: Int, unMinimo: Int = 0): Energia = copy(actual = (actual - disminucion).max(unMinimo))

  def cargarAlMaximo: Energia = copy(actual = maximo)

  def bajarAlMinimo: Energia = setActual(0)

  def modificarMaximo(f: Int => Int): Energia = copy(maximo = f(maximo))

  def fusionadaA(otraEnergia: Energia): Energia = copy(actual = actual + otraEnergia.actual,
    maximo = maximo + otraEnergia.maximo)
}