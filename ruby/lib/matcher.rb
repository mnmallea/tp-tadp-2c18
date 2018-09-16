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

  def list(lista, matchear_tamanio = false)
    ListMatcher.new lista, matchear_tamanio
  end

  def duck(*mensajes)
    DuckMatcher.new mensajes
  end

  def call(un_objeto)
    val(self).call(un_objeto)
  end

  def bind_to(un_contexto, un_objeto)

  end
end

class Matcher
  include OperacionesMatchers

  def bind_to(un_contexto, un_objeto)

  end

end

class NotMatcher < Matcher
  def initialize(un_matcher)
    @matcher = un_matcher
  end

  def call(un_objeto)
    !@matcher.call(un_objeto)
  end
end

class TypeMatcher < Matcher

  def initialize(un_tipo)
    @tipo = un_tipo
  end

  def call(un_objeto)
    un_objeto.is_a? @tipo
  end

end

class ValMatcher < Matcher

  def initialize(un_objeto)
    @objeto = un_objeto
  end

  def call(un_objeto)
    @objeto == un_objeto
  end
end

class DuckMatcher < Matcher
  def initialize(unos_mensajes)
    @mensajes = unos_mensajes
  end

  def call(un_objeto)
    mensajes_objeto = un_objeto.methods
    @mensajes.all? do
    |mensaje|
      mensajes_objeto.include? mensaje
    end
  end
end

class ListMatcher
  include OperacionesMatchers

  def initialize(list, matchea_tamanio)
    @list = list
    @debe_matchear_tamanio = matchea_tamanio
  end

  def call(un_objeto)
    type(Array).call(un_objeto) && self.matchea_tamanio(un_objeto) && self.matchea_con_lista(un_objeto)
  end

  def matchea_con_lista(una_lista)
    @list.zip(una_lista).reduce(true) do
    |un_bool, arr|
      arr[0].call(arr[1]) && un_bool
    end
  end

  def matchea_tamanio(una_lista)
    (!@debe_matchear_tamanio && una_lista.size >= @list.size) || una_lista.size == @list.size
  end

  def bind_to(un_contexto, una_lista)
    @list.zip(una_lista).each {|matcher, elemento_lista| matcher.bind_to un_contexto, elemento_lista}
  end
end


class AllMatcher
  include OperacionesMatchers

  def initialize(unos_matchers)
    @matchers = unos_matchers
  end

  def call(un_objeto)
    @matchers.all? {|matcher| matcher.call(un_objeto)}
  end
end

class AnyMatcher
  include OperacionesMatchers

  def initialize(unos_matchers)
    @matchers = unos_matchers
  end

  def call(un_objeto)
    @matchers.any? {|matcher| matcher.call(un_objeto)}
  end
end