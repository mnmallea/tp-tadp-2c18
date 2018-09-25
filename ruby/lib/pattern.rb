require_relative 'matcher'

class Pattern
  include MatcherFactory
  attr_accessor :match_status

  def initialize(un_objeto)
    @objeto = un_objeto
    self.match_status = NoMatch.new
  end

  def with(un_matcher, *otros_matchers, &bloque)
    matchers = otros_matchers << un_matcher

    if todos_matchean(matchers)
      contexto = configurar_contexto(matchers)
      match_status.ejecutar_bloque(contexto, self, &bloque)
    end
  end

  def otherwise(&bloque)
    match_status.ejecutar_bloque(Contexto.new, self, &bloque)
  end


  def todos_matchean(unos_matchers)
    unos_matchers.all? {|matcher| matcher.call(@objeto)}
  end

  def configurar_contexto(matchers)
    contexto = Contexto.new
    matchers.each {|matcher| matcher.bind_to contexto, @objeto}
    contexto
  end

  def get_value
    match_status.value
  end
end

class NoMatch
  def value
    raise MatchError, 'Non exhaustive patterns'
  end

  def ejecutar_bloque(un_contexto, un_patron, &bloque)
    resultado = un_contexto.instance_exec(&bloque)
    matchear(resultado, un_patron)
  end

  private

  def matchear(un_valor, un_patron)
    un_patron.match_status = Match.new(un_valor)
  end
end

class Match
  attr_accessor :value

  def initialize(un_valor)
    self.value = un_valor
  end

  def ejecutar_bloque(un_contexto, un_patron, &bloque)
    #nada
  end
end

module Matches
  def matches?(an_object, &block)
    pattern = Pattern.new(an_object)
    pattern.instance_exec &block
    pattern.get_value
  end
end

class MatchError < StandardError
  def initialize(msg= "")
    super
  end
end