require_relative 'pattern'
require_relative 'matcher'

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
