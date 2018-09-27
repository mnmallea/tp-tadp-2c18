require_relative 'pattern'
require_relative 'matcher'
require_relative 'contexto'

class Object
  include Matches
end

class Symbol
  include Matcher
  def call(un_objeto)
    true
  end

  def bind_to(un_contexto, un_objeto)
    un_contexto.bind self, un_objeto
  end
end