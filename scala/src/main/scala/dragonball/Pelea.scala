package dragonball

sealed trait Pelea

case class Ganador(guerrero: Guerrero)

case class Peleando(unGuerrero: Guerrero, otroGuerrero: Guerrero)