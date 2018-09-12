module OperacionesMatchers
  def and(*otros_matchers)
    AllMatcher.new(otros_matchers << self)
  end

  def or(*otros_matchers)
    AnyMatcher.new(otros_matchers << self)
  end

  def not
    Matcher.new do
    |un_objeto|
      !self.call(un_objeto)
    end
  end
end


module MatcherFactory
  def val(un_objeto)
    Matcher.new do
    |otro_objeto|
      otro_objeto == un_objeto
    end
  end

  def type(un_tipo)
    Matcher.new do
    |un_objeto|
      un_objeto.is_a? un_tipo
    end
  end

  #todo match de tamaño
  def list(lista, matchear_tamanio = false)
    ListMatcher.new lista, matchear_tamanio
  end

  def duck(*mensajes)
    Matcher.new do
    |un_objeto|
      mensajes_objeto = un_objeto.methods
      mensajes.all? do
      |mensaje|
        mensajes_objeto.include? mensaje
      end
    end
  end
end

class Matcher
  include OperacionesMatchers

  def initialize(&una_lambda)
    @lambda = una_lambda
  end

  def call(un_objeto)
    @lambda.call un_objeto
  end

end

class ListMatcher
  include OperacionesMatchers
  def initialize(list, should_match_size)
    @list = list
    @should_match_size = should_match_size
  end

  def call(un_objeto)
    Matcher.type(Array).call(un_objeto) && self.matchea_con_lista(un_objeto)
  end

  def matchea_con_lista(una_lista)
    @list == una_lista.take(@list.size) && self.matchea_tamanio(una_lista)
  end

  def matchea_tamanio(una_lista)
    !@should_match_size || una_lista.size == @list.size
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