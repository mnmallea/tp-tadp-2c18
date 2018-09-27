module OperacionesMatchers
  def and(*otros_matchers)
    AllMatcher.new(otros_matchers << self)
  end

  def or(*otros_matchers)
    AnyMatcher.new(otros_matchers << self)
  end

  def not
    NotMatcher.new self
  end
end


module MatcherFactory
  def val(un_objeto)
    ValMatcher.new un_objeto
  end

  def type(un_tipo)
    TypeMatcher.new un_tipo
  end

  def list(lista, matchear_tamanio = true)
    ListMatcher.new lista, matchear_tamanio
  end

  def duck(un_mensaje, *unos_mensajes)
    mensajes = unos_mensajes << un_mensaje
    DuckMatcher.new mensajes
  end
end

module Matcher
  include MatcherFactory
  include OperacionesMatchers
end

class NotMatcher
  include Matcher
  def initialize(un_matcher)
    @matcher = un_matcher
  end

  def call(un_objeto)
    !@matcher.call(un_objeto)
  end
end

class TypeMatcher
  include Matcher

  def initialize(un_tipo)
    @tipo = un_tipo
  end

  def call(un_objeto)
    un_objeto.is_a? @tipo
  end

end

class ValMatcher
  include Matcher

  def initialize(un_objeto)
    @objeto = un_objeto
  end

  def call(un_objeto)
    @objeto == un_objeto
  end
end

class DuckMatcher
  include Matcher

  def initialize(unos_mensajes)
    @mensajes = unos_mensajes
  end

  def call(un_objeto)
    @mensajes.all? {|mensaje| un_objeto.methods.include?(mensaje)}
  end
end

class ListMatcher
  include Matcher

  def initialize(list, matchea_tamanio = true)
    @list = list
    @debe_matchear_tamanio = matchea_tamanio
  end

  def call(un_objeto)
    type(Array).call(un_objeto) && self.matchea_tamanio(un_objeto) && self.matchea_con_lista(un_objeto)
  end

  def matchea_con_lista(una_lista)
    @list.zip(una_lista).all? do |matcher, valor|
      if type(Matcher).call(matcher)    ##### ACA PODRIA PREGUNTAR POR TYPE SYMBOL EN VEZ DE INCLUIRLE MATCHER A LA CLASE SYMBOL --> DISCUTIRLO
        matcher.call(valor)
      else
        val(matcher).call(valor)
      end
    end

    def configurar_contexto(un_contexto, un_objeto)   ## SE UTILIZA EN EL PATTERN PARA CUANDO LE LLEGA EN EL WITH UN LISTMATCHER, VERIFICA SI HAY SIMBOLOS Y BINDEA
      @list.each_with_index do |objeto, i|
        if type(Symbol).call(objeto)
          objeto.bind_to(un_contexto, un_objeto[i])
        end
      end
    end
  end

  def matchea_tamanio(una_lista)
    (!@debe_matchear_tamanio && una_lista.size >= @list.size) || una_lista.size == @list.size
  end

  def bind_to(un_contexto, una_lista)
    @list.zip(una_lista).each {|matcher, elemento_lista| matcher.bind_to un_contexto, elemento_lista}
  end
end

class MatcherMultiple
  include Matcher
  def initialize(unos_matchers)
    @matchers = unos_matchers
  end

  def bind_to(un_contexto, un_objeto)
    @matchers.each {|matcher| matcher.bind_to(un_contexto, un_objeto)}
  end

end


class AllMatcher < MatcherMultiple

  def call(un_objeto)
    @matchers.all? {|matcher| matcher.call(un_objeto)}
  end

end

class AnyMatcher < MatcherMultiple

  def call(un_objeto)
    @matchers.any? {|matcher| matcher.call(un_objeto)}
  end

end