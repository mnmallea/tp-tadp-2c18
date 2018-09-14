require_relative 'pattern'
require_relative 'matcher'
require_relative 'contexto'

class Object
  include Match
  include Pattern
  include MatcherFactory
end

class Symbol
  def call(un_objeto)
    true
  end
end
