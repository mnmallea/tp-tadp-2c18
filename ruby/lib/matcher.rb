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

class Matcher
  include OperacionesMatchers

  def initialize(&una_lambda)
    @lambda = una_lambda
  end

  def call(un_objeto)
    @lambda.call un_objeto
  end

  def self.val(un_objeto)
    Matcher.new do
    |otro_objeto|
      otro_objeto == un_objeto
    end
  end

  def self.type(un_tipo)
    Matcher.new do
    |un_objeto|
      un_objeto.is_a? un_tipo
    end
  end

  #todo match de tamaño
  def self.list(lista, matchear_tamanio = false)
    tamanio = lista.size
    Matcher.new do
    |una_lista|
      type(Array).call(lista) && una_lista.take(tamanio) == lista
    end
  end

  def self.duck(*mensajes)
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